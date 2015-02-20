package com.tpofof.conmon.server.config;

import java.util.List;

import io.dropwizard.Configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;

public class ConmonConfiguration extends Configuration {

	private MongoConfig mongo = new MongoConfig();
	private List<ElasticsearchConfiguration> esConfigs = Lists.newArrayList();

	@JsonProperty
	public MongoConfig getMongo() {
		return mongo;
	}

	@JsonProperty
	public ConmonConfiguration setMongo(MongoConfig mongo) {
		this.mongo = mongo;
		return this;
	}

	@JsonProperty
	public List<ElasticsearchConfiguration> getEsConfigs() {
		return esConfigs;
	}

	@JsonProperty
	public ConmonConfiguration setEsConfigs(List<ElasticsearchConfiguration> esConfigs) {
		this.esConfigs = esConfigs;
		return this;
	}
}
