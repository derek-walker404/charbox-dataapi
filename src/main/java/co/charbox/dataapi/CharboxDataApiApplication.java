package co.charbox.dataapi;

import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.auth.AuthFactory;
import io.dropwizard.auth.ChainedAuthFactory;
import io.dropwizard.auth.basic.BasicAuthFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.core.mm.MaxMindService;
import co.charbox.dataapi.auth.AdminAuthenticator;
import co.charbox.dataapi.auth.DeviceAuthenticator;
import co.charbox.dataapi.auth.ServerAuthenticator;
import co.charbox.dataapi.auth.TokenAuthenticator;
import co.charbox.dataapi.config.CharboxConfiguration;
import co.charbox.dataapi.health.GuavaCacheMetrics;
import co.charbox.domain.model.auth.CharbotAuthModel;

import com.codahale.metrics.MetricSet;
import com.tpofof.core.App;
import com.tpofof.core.bootstrap.DefaultInitializer;
import com.tpofof.dwa.DwaApp;

@Component
public class CharboxDataApiApplication extends DwaApp<CharboxConfiguration> {

	public static void main(String[] args) throws Exception {
		App.getContext().getBean(CharboxDataApiApplication.class).run(args);
	}
	
	@Autowired private DefaultInitializer initializer;
	
	/*
	 * SERVICES
	 */
	@Autowired private MaxMindService maxMindService;
	
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
		
		initializer.initializeApplicationBeans();
		
		// TODO: find a better place for this... DateTimeInitializable?
		DateTimeZone.setDefault(DateTimeZone.UTC);
		
		/* METRICS */
		MetricSet maxMindCacheMetrics = GuavaCacheMetrics.metricsFor("mm.service", maxMindService.getCache());
		env.metrics().registerAll(maxMindCacheMetrics);
		
		/* AUTH */
		BasicAuthFactory<CharbotAuthModel> deviceAuthFactory = new BasicAuthFactory<CharbotAuthModel>(deviceAuthenticator, "charbot.co", CharbotAuthModel.class);
		BasicAuthFactory<CharbotAuthModel> adminAuthFactory = new BasicAuthFactory<CharbotAuthModel>(adminAuthenticator, "charbot.co", CharbotAuthModel.class);
		BasicAuthFactory<CharbotAuthModel> serverAuthFactory = new BasicAuthFactory<CharbotAuthModel>(serverAuthenticator, "charbot.co", CharbotAuthModel.class);
		BasicAuthFactory<CharbotAuthModel> tokenAuthFactory = new BasicAuthFactory<CharbotAuthModel>(tokenAuthenticator, "charbot.co", CharbotAuthModel.class);
		@SuppressWarnings("unchecked")
		ChainedAuthFactory<CharbotAuthModel> authChainFactory = new ChainedAuthFactory<CharbotAuthModel>(deviceAuthFactory, adminAuthFactory,
				serverAuthFactory, tokenAuthFactory);
		env.jersey().register(AuthFactory.binder(authChainFactory));
	}

}
