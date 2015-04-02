package co.charbox.dataapi.config;

import java.util.List;

import io.dropwizard.Configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;

public class CharboxConfiguration extends Configuration {

	private MongoConfig mongo = new MongoConfig();
	private List<ElasticsearchConfiguration> esConfigs = Lists.newArrayList();

	@JsonProperty
	public MongoConfig getMongo() {
		return mongo;
	}

	@JsonProperty
	public CharboxConfiguration setMongo(MongoConfig mongo) {
		this.mongo = mongo;
		return this;
	}

	@JsonProperty
	public List<ElasticsearchConfiguration> getEsConfigs() {
		return esConfigs;
	}

	@JsonProperty
	public CharboxConfiguration setEsConfigs(List<ElasticsearchConfiguration> esConfigs) {
		this.esConfigs = esConfigs;
		return this;
	}
}
