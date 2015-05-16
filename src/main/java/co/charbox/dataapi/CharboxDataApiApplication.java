package co.charbox.dataapi;

import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.auth.AuthFactory;
import io.dropwizard.auth.ChainedAuthFactory;
import io.dropwizard.auth.basic.BasicAuthFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.core.mm.MaxMindService;
import co.charbox.dataapi.auth.AdminAuthenticator;
import co.charbox.dataapi.auth.DeviceAuthenticator;
import co.charbox.dataapi.auth.ServerAuthenticator;
import co.charbox.dataapi.auth.TokenAuthenticator;
import co.charbox.dataapi.config.CharboxConfiguration;
import co.charbox.dataapi.health.GuavaCacheMetrics;
import co.charbox.dataapi.health.MaxMindAccountHealth;
import co.charbox.dataapi.health.MaxMindConnectionHealth;
import co.charbox.dataapi.resources.MaxMindResource;
import co.charbox.dataapi.resources.crud.DeviceConfigResource;
import co.charbox.dataapi.resources.crud.DeviceResource;
import co.charbox.dataapi.resources.crud.HeartbeatResource;
import co.charbox.dataapi.resources.crud.OutageResource;
import co.charbox.dataapi.resources.crud.SstResultResource;
import co.charbox.dataapi.resources.crud.TestCaseResource;
import co.charbox.dataapi.resources.crud.TimerResultResource;
import co.charbox.dataapi.resources.crud.auth.AuthResource;
import co.charbox.dataapi.resources.crud.auth.DeviceAuthResource;
import co.charbox.dataapi.resources.crud.auth.ServiceAuthResource;
import co.charbox.dataapi.resources.crud.auth.TokenAuthResource;

import com.codahale.metrics.MetricSet;
import com.tpofof.core.App;
import com.tpofof.core.security.IAuthModel;
import com.tpofof.dwa.DwaApp;

@Component
public class CharboxDataApiApplication extends DwaApp<CharboxConfiguration> {

	public static void main(String[] args) throws Exception {
		App.getContext().getBean(CharboxDataApiApplication.class).run(args);
	}
	
	/*
	 * SERVICES
	 */
	@Autowired private MaxMindService maxMindService;
	
	/*
	 * RESOURCES
	 */
	@Autowired private DeviceConfigResource deviceConfigResource;
	@Autowired private TestCaseResource testCaseResource;
	@Autowired private DeviceResource deviceResource;
	@Autowired private TimerResultResource timerResultResource;
	@Autowired private HeartbeatResource heartbeatResource;
	@Autowired private OutageResource outageResource;
	@Autowired private SstResultResource sstResource;
	@Autowired private MaxMindResource maxMindResource;
	
	@Autowired private AuthResource authResource;
	@Autowired private DeviceAuthResource deviceAuthResource;
	@Autowired private ServiceAuthResource serverAuthResource;
	@Autowired private TokenAuthResource tokenAuthResource;
	
	/*
	 * HEALTH CHECKS
	 */
	@Autowired private MaxMindAccountHealth maxMindAccountHealth;
	@Autowired private MaxMindConnectionHealth maxMindConnectionHealth;
	
	/*
	 * AUTH
	 */
	@Autowired private DeviceAuthenticator deviceAuthenticator;
	@Autowired private AdminAuthenticator adminAuthenticator;
	@Autowired private ServerAuthenticator serverAuthenticator;
	@Autowired private TokenAuthenticator tokenAuthenticator;
	
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
		super.run(config, env);
		
		/* RESOURCES */
		env.jersey().register(deviceConfigResource);
		env.jersey().register(testCaseResource);
		env.jersey().register(deviceResource);
		env.jersey().register(timerResultResource);
		env.jersey().register(heartbeatResource);
		env.jersey().register(outageResource);
		env.jersey().register(sstResource);
		env.jersey().register(maxMindResource);
		
		env.jersey().register(authResource);
		env.jersey().register(deviceAuthResource);
		env.jersey().register(serverAuthResource);
		env.jersey().register(tokenAuthResource);
		
		/* HEALTH CHECKS */
		env.healthChecks().register("mm.account", maxMindAccountHealth);
		env.healthChecks().register("mm.connection", maxMindConnectionHealth);
		
		/* METRICS */
		MetricSet maxMindCacheMetrics = GuavaCacheMetrics.metricsFor("mm.service", maxMindService.getCache());
		env.metrics().registerAll(maxMindCacheMetrics);
		
		/* AUTH */
		BasicAuthFactory<IAuthModel> deviceAuthFactory = new BasicAuthFactory<IAuthModel>(deviceAuthenticator, "charbot.co", IAuthModel.class);
		BasicAuthFactory<IAuthModel> adminAuthFactory = new BasicAuthFactory<IAuthModel>(adminAuthenticator, "charbot.co", IAuthModel.class);
		BasicAuthFactory<IAuthModel> serverAuthFactory = new BasicAuthFactory<IAuthModel>(serverAuthenticator, "charbot.co", IAuthModel.class);
		BasicAuthFactory<IAuthModel> tokenAuthFactory = new BasicAuthFactory<IAuthModel>(tokenAuthenticator, "charbot.co", IAuthModel.class);
		@SuppressWarnings("unchecked")
		ChainedAuthFactory<IAuthModel> authChainFactory = new ChainedAuthFactory<IAuthModel>(deviceAuthFactory, adminAuthFactory,
				serverAuthFactory, tokenAuthFactory);
		env.jersey().register(AuthFactory.binder(authChainFactory));
		
		super.addCorsSupport(env); // TODO: change to config values.
	}

}
