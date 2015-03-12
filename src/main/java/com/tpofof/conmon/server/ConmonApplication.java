package com.tpofof.conmon.server;

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

import com.google.common.base.Joiner;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.tpofof.conmon.server.config.ConmonConfiguration;
import com.tpofof.conmon.server.config.ElasticsearchConfiguration;
import com.tpofof.conmon.server.config.MongoConfig;
import com.tpofof.conmon.server.data.elasticsearch.TimerResultEsDAO;
import com.tpofof.conmon.server.data.mongo.DeviceConfigDAO;
import com.tpofof.conmon.server.data.mongo.DeviceDAO;
import com.tpofof.conmon.server.data.mongo.TestCaseDAO;
import com.tpofof.conmon.server.managers.DeviceConfigurationManager;
import com.tpofof.conmon.server.managers.DeviceManager;
import com.tpofof.conmon.server.managers.TestCaseManager;
import com.tpofof.conmon.server.managers.TimerResultManager;
import com.tpofof.conmon.server.resources.DeviceResource;
import com.tpofof.conmon.server.resources.crud.DeviceConfigResource;
import com.tpofof.conmon.server.resources.crud.TestCaseResource;
import com.tpofof.conmon.server.resources.crud.TimerResultResource;

public class ConmonApplication extends Application<ConmonConfiguration> {

	public static void main(String[] args) throws Exception {
		new ConmonApplication().run(args);
	}
	
	@Override
	public String getName() {
		return "conmon";
	}
	
	@Override
	public void initialize(Bootstrap<ConmonConfiguration> bootstrap) {
		bootstrap.addBundle(new AssetsBundle("/assets", "/c/", "index.html"));
	}

	@Override
	public void run(ConmonConfiguration config, Environment env) throws Exception {
		/* MONGO */
		final MongoConfig mdbConfig = config.getMongo();
		final MongoClient mongoClient = new MongoClient(mdbConfig.getHost(), mdbConfig.getPort());
		final DB conmonDb = mongoClient.getDB(mdbConfig.getDbname());
		/* COLLECTIONS */
		final DBCollection deviceConfigCollection = conmonDb.getCollection("deviceConfig");
		final DBCollection testCaseCollection = conmonDb.getCollection("testCase");
		final DBCollection deviceCollection = conmonDb.getCollection("device");
		/* ELASTICSEARCH */
		List<ElasticsearchConfiguration> esConfigs = config.getEsConfigs();
		TransportClient esClient = new TransportClient();
		for (ElasticsearchConfiguration c : esConfigs) {
        	esClient.addTransportAddress(new InetSocketTransportAddress(c.getHost(), c.getPort()));
		}
		
		/* DAO */
		final DeviceConfigDAO deviceConfigDao = new DeviceConfigDAO(deviceConfigCollection);
		final TestCaseDAO testCaseDao = new TestCaseDAO(testCaseCollection);
		final DeviceDAO deviceDao = new DeviceDAO(deviceCollection);
		/* ES DAO */
		final TimerResultEsDAO timerResultEsDao = new TimerResultEsDAO(esClient);
		
		/* MANAGERS */
		final TestCaseManager testCaseMan = new TestCaseManager(testCaseDao);
		final DeviceConfigurationManager deviceConfigMan = new DeviceConfigurationManager(deviceConfigDao, testCaseMan);
		final DeviceManager deviceMan = new DeviceManager(deviceDao, deviceConfigMan, testCaseMan);
		final TimerResultManager timerResultMan = new TimerResultManager(timerResultEsDao);
		
		/* RESOURCES */
		final DeviceConfigResource deviceConfigResource = new DeviceConfigResource(deviceConfigMan);
		env.jersey().register(deviceConfigResource);
		final TestCaseResource testCaseResource = new TestCaseResource(testCaseMan);
		env.jersey().register(testCaseResource);
		final DeviceResource deviceResource = new DeviceResource(deviceMan);
		env.jersey().register(deviceResource);
		final TimerResultResource timerResultResource = new TimerResultResource(timerResultMan);
		env.jersey().register(timerResultResource);
		
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
