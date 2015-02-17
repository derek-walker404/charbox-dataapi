package com.tpofof.conmon.server;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.tpofof.conmon.server.config.ConmonConfiguration;
import com.tpofof.conmon.server.config.MongoConfig;
import com.tpofof.conmon.server.managers.DeviceConfigurationManager;
import com.tpofof.conmon.server.managers.DeviceManager;
import com.tpofof.conmon.server.mongo.DeviceConfigDAO;
import com.tpofof.conmon.server.mongo.DeviceDAO;
import com.tpofof.conmon.server.mongo.TestCaseDAO;
import com.tpofof.conmon.server.resources.DeviceResource;
import com.tpofof.conmon.server.resources.crud.DeviceConfigResource;
import com.tpofof.conmon.server.resources.crud.TestCaseResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

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
	}

	@Override
	public void run(ConmonConfiguration config, Environment env) throws Exception {
		/* MONGO */
		final MongoConfig mdbConfig = new MongoConfig(); //config.getMongo();
		final MongoClient mongoClient = new MongoClient(mdbConfig.getHost(), mdbConfig.getPort());
		final DB conmonDb = mongoClient.getDB(mdbConfig.getDbname());
		final DBCollection deviceConfigCollection = conmonDb.getCollection("deviceConfig");
		final DeviceConfigDAO deviceConfigDAO = new DeviceConfigDAO(deviceConfigCollection);
		final DBCollection testCaseCollection = conmonDb.getCollection("testCase");
		final TestCaseDAO testCaseDAO = new TestCaseDAO(testCaseCollection);
		final DBCollection deviceCollection = conmonDb.getCollection("device");
		final DeviceDAO deviceDao = new DeviceDAO(deviceCollection);
		
		/* MANAGERS */
		final DeviceConfigurationManager deviceConfigMan = new DeviceConfigurationManager(deviceConfigDAO, testCaseDAO);
		final DeviceManager deviceMan = new DeviceManager(deviceDao, deviceConfigMan);
		
		/* RESOURCES */
		final DeviceConfigResource deviceConfigResource = new DeviceConfigResource(deviceConfigDAO);
		env.jersey().register(deviceConfigResource);
		final TestCaseResource testCaseResource = new TestCaseResource(testCaseDAO);
		env.jersey().register(testCaseResource);
		final DeviceResource deviceResource = new DeviceResource(deviceMan);
		env.jersey().register(deviceResource);
	}

}
