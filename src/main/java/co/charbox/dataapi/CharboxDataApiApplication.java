package co.charbox.dataapi;

import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.auth.AuthFactory;
import io.dropwizard.auth.ChainedAuthFactory;
import io.dropwizard.auth.basic.BasicAuthFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.auth.AdminAuthenticator;
import co.charbox.dataapi.auth.DeviceAuthenticator;
import co.charbox.dataapi.config.CharboxConfiguration;
import co.charbox.dataapi.health.GuavaCacheMetrics;
import co.charbox.dataapi.health.MaxMindAccountHealth;
import co.charbox.dataapi.health.MaxMindConnectionHealth;
import co.charbox.dataapi.resources.MaxMindResource;
import co.charbox.dataapi.resources.crud.DeviceAuthResource;
import co.charbox.dataapi.resources.crud.DeviceConfigResource;
import co.charbox.dataapi.resources.crud.DeviceResource;
import co.charbox.dataapi.resources.crud.HeartbeatResource;
import co.charbox.dataapi.resources.crud.OutageResource;
import co.charbox.dataapi.resources.crud.TestCaseResource;
import co.charbox.dataapi.resources.crud.TimerResultResource;
import co.charbox.domain.mm.MaxMindService;
import co.charbox.domain.model.auth.IAuthModel;

import com.codahale.metrics.MetricSet;
import com.tpofof.core.App;
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
	@Autowired private MaxMindResource maxMindResource;
	@Autowired private DeviceAuthResource authResource;
	
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
		env.jersey().register(maxMindResource);
		env.jersey().register(authResource);
		
		/* HEALTH CHECKS */
		env.healthChecks().register("mm.account", maxMindAccountHealth);
		env.healthChecks().register("mm.connection", maxMindConnectionHealth);
		
		/* METRICS */
		MetricSet maxMindCacheMetrics = GuavaCacheMetrics.metricsFor("mm.service", maxMindService.getCache());
		env.metrics().registerAll(maxMindCacheMetrics);
		
		/* AUTH */
		BasicAuthFactory<IAuthModel> deviceAuthFactory = new BasicAuthFactory<IAuthModel>(deviceAuthenticator, "chatbot.co", IAuthModel.class);
		BasicAuthFactory<IAuthModel> adminAuthFactory = new BasicAuthFactory<IAuthModel>(adminAuthenticator, "chatbot.co", IAuthModel.class);
		@SuppressWarnings("unchecked")
		ChainedAuthFactory<IAuthModel> authChainFactory = new ChainedAuthFactory<IAuthModel>(deviceAuthFactory, adminAuthFactory);
		env.jersey().register(AuthFactory.binder(authChainFactory));
		
		super.addCorsSupport(env); // TODO: change to config values.
	}

}
