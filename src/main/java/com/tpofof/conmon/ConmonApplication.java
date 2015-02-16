package com.tpofof.conmon;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.tpofof.conmon.server.config.ConmonConfiguration;
import com.tpofof.conmon.server.config.MongoConfig;
import com.tpofof.conmon.server.mongo.DeviceConfigDAO;

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
		MongoConfig mdbConfig = config.getMongo();
		MongoClient mongoClient = new MongoClient(mdbConfig.getHost(), mdbConfig.getPort());
		DB conmonDb = mongoClient.getDB(mdbConfig.getDbname());
		DBCollection deviceConfigCollection = conmonDb.getCollection("deviceConfig");
		DeviceConfigDAO deviceConfigDAO = new DeviceConfigDAO(deviceConfigCollection);
		
		/* RESOURCES */
		final DeviceConfigResource deviceConfigResource = new DeviceConfigResource(deviceConfigDAO);
		env.jersey().register(deviceConfigResource);
	}

}
