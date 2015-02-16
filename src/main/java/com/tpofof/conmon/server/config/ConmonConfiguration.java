package com.tpofof.conmon.server.config;

import io.dropwizard.Configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConmonConfiguration extends Configuration {

	private MongoConfig mongo = new MongoConfig();

	@JsonProperty
	public MongoConfig getMongo() {
		return mongo;
	}

	@JsonProperty
	public void setMongo(MongoConfig mongo) {
		this.mongo = mongo;
	}
}
