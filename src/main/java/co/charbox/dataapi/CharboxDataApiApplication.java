package co.charbox.dataapi;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.util.EnumSet;
import java.util.List;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;

import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import co.charbox.core.utils.Config;
import co.charbox.dataapi.config.CharboxConfiguration;
import co.charbox.dataapi.config.ElasticsearchConfiguration;
import co.charbox.dataapi.config.MongoConfig;
import co.charbox.dataapi.data.elasticsearch.TimerResultEsDAO;
import co.charbox.dataapi.data.mongo.DeviceConfigDAO;
import co.charbox.dataapi.data.mongo.DeviceDAO;
import co.charbox.dataapi.data.mongo.HeartbeatDAO;
import co.charbox.dataapi.data.mongo.OutageDAO;
import co.charbox.dataapi.data.mongo.TestCaseDAO;
import co.charbox.dataapi.health.GuavaCacheMetrics;
import co.charbox.dataapi.health.MaxMindAccountHealth;
import co.charbox.dataapi.health.MaxMindConnectionHealth;
import co.charbox.dataapi.managers.DeviceConfigurationManager;
import co.charbox.dataapi.managers.DeviceManager;
import co.charbox.dataapi.managers.HeartbeatManager;
import co.charbox.dataapi.managers.OutageManager;
import co.charbox.dataapi.managers.TestCaseManager;
import co.charbox.dataapi.managers.TimerResultManager;
import co.charbox.dataapi.resources.DeviceResource;
import co.charbox.dataapi.resources.MaxMindResource;
import co.charbox.dataapi.resources.crud.DeviceConfigResource;
import co.charbox.dataapi.resources.crud.HeartbeatResource;
import co.charbox.dataapi.resources.crud.OutageResource;
import co.charbox.dataapi.resources.crud.TestCaseResource;
import co.charbox.dataapi.resources.crud.TimerResultResource;
import co.charbox.domain.mm.MaxMindService;

import com.codahale.metrics.MetricSet;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class CharboxDataApiApplication extends Application<CharboxConfiguration> {

	public static void main(String[] args) throws Exception {
		new CharboxDataApiApplication().run(args);
	}
	
	@Override
	public String getName() {
		return "charbox-dataapi";
	}
	
	@Override
	public void initialize(Bootstrap<CharboxConfiguration> bootstrap) {
		bootstrap.addBundle(new AssetsBundle("/assets", "/c/", "index.html"));
	}

	@Override
	public void run(CharboxConfiguration config, Environment env) throws Exception {
		/* MONGO */
		final MongoConfig mdbConfig = config.getMongo();
		final MongoClient mongoClient = new MongoClient(mdbConfig.getHost(), mdbConfig.getPort());
		final DB conmonDb = mongoClient.getDB(mdbConfig.getDbname());
		/* COLLECTIONS */
		final DBCollection deviceConfigCollection = conmonDb.getCollection("deviceConfig");
		final DBCollection testCaseCollection = conmonDb.getCollection("testCase");
		final DBCollection deviceCollection = conmonDb.getCollection("device");
		final DBCollection hbCollection = conmonDb.getCollection("hb");
		final DBCollection outagesCollection = conmonDb.getCollection("outages");
		/* ELASTICSEARCH */
		List<ElasticsearchConfiguration> esConfigs = config.getEsConfigs();
		TransportClient esClient = new TransportClient();
		for (ElasticsearchConfiguration c : esConfigs) {
        	esClient.addTransportAddress(new InetSocketTransportAddress(c.getHost(), c.getPort()));
		}
		/* MAXMIND */
		MaxMindService maxMindService = new MaxMindService(); // pull args out of constructor?
		
		/* MONGO DAO */
		final DeviceConfigDAO deviceConfigDao = new DeviceConfigDAO(deviceConfigCollection);
		final TestCaseDAO testCaseDao = new TestCaseDAO(testCaseCollection);
		final DeviceDAO deviceDao = new DeviceDAO(deviceCollection);
		final HeartbeatDAO hbDao = new HeartbeatDAO(hbCollection);
		final OutageDAO outagesDAO = new OutageDAO(outagesCollection);
		/* ES DAO */
		final TimerResultEsDAO timerResultEsDao = new TimerResultEsDAO(esClient);
		
		/* MANAGERS */
		final TestCaseManager testCaseMan = new TestCaseManager(testCaseDao);
		final DeviceConfigurationManager deviceConfigMan = new DeviceConfigurationManager(deviceConfigDao, testCaseMan);
		final TimerResultManager timerResultMan = new TimerResultManager(timerResultEsDao);
		final HeartbeatManager hbManager = new HeartbeatManager(hbDao);
		final OutageManager outagesManager = new OutageManager(outagesDAO);
	 	final DeviceManager deviceMan = new DeviceManager(deviceDao, deviceConfigMan, testCaseMan, timerResultMan, hbManager);
		
		/* RESOURCES */
		final DeviceConfigResource deviceConfigResource = new DeviceConfigResource(deviceConfigMan);
		env.jersey().register(deviceConfigResource);
		final TestCaseResource testCaseResource = new TestCaseResource(testCaseMan);
		env.jersey().register(testCaseResource);
		final DeviceResource deviceResource = new DeviceResource(deviceMan);
		env.jersey().register(deviceResource);
		final TimerResultResource timerResultResource = new TimerResultResource(timerResultMan);
		env.jersey().register(timerResultResource);
		final HeartbeatResource hbResource = new HeartbeatResource(hbManager);
		env.jersey().register(hbResource);
		final OutageResource outagesResource = new OutageResource(outagesManager);
		env.jersey().register(outagesResource);
		final MaxMindResource maxMindResource = new MaxMindResource(maxMindService);
		env.jersey().register(maxMindResource);
		
		/* HEALTH CHECKS */
		MaxMindAccountHealth maxMindAccountHealth = new MaxMindAccountHealth(maxMindService, Config.get().getInt("location.api.requests.minThreshold"));
		env.healthChecks().register("mm.account", maxMindAccountHealth);
		MaxMindConnectionHealth maxMindConnectionHealth = new MaxMindConnectionHealth(maxMindService);
		env.healthChecks().register("mm.connection", maxMindConnectionHealth);
		
		/* METRICS */
		MetricSet maxMindCacheMetrics = GuavaCacheMetrics.metricsFor("mm.service", maxMindService.getCache());
		env.metrics().registerAll(maxMindCacheMetrics);
		
		/* CORS */
		final FilterRegistration.Dynamic cors = env.servlets().addFilter("CORS", CrossOriginFilter.class);

	    // Configure CORS parameters
	    cors.setInitParameter("allowedOrigins", "*");
	    cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
	    cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");

	    // Add URL mapping
	    cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
	}

}
