package com.tpofof.conmon.server.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;

public class MongoConfig extends Configuration {

	private String host = "localhost";
	private int port = 27017;
	private String dbname = "conmon";

	@JsonProperty
	public String getHost() {
		return host;
	}

	@JsonProperty
	public void setHost(String host) {
		this.host = host;
	}

	@JsonProperty
	public int getPort() {
		return port;
	}

	@JsonProperty
	public void setPort(int port) {
		this.port = port;
	}

	@JsonProperty
	public String getDbname() {
		return dbname;
	}

	@JsonProperty
	public void setDbname(String dbname) {
		this.dbname = dbname;
	}
}
