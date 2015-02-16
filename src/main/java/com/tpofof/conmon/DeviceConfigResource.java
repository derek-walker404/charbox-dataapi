package com.tpofof.conmon;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.JsonNode;
import com.pofof.conmon.model.DeviceConfiguration;
import com.tpofof.conmon.server.mongo.DeviceConfigDAO;
import com.tpofof.utils.JsonUtils;

@Path("/configs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DeviceConfigResource {
	
	private DeviceConfigDAO dao;
	
	public DeviceConfigResource(DeviceConfigDAO dao) {
		this.dao = dao;
	}
	
	@POST
	@Timed
	public JsonNode postConfiguration(DeviceConfiguration config) {
		DeviceConfiguration insertedConfig = dao.insert(config);
		return insertedConfig == null
				? ResponseUtils.failure("Could not insert device configuration.")
				: ResponseUtils.success(JsonUtils.toJsonNode(insertedConfig));
	}
	
	@Path("/{_id}")
	@GET
	@Timed
	public JsonNode getConfig(String _id) {
		DeviceConfiguration config = dao.get(_id);
		return config == null
				? ResponseUtils.failure("Could not find config with id: " + _id, 404)
				: ResponseUtils.success(JsonUtils.toJsonNode(config));
 	}
}
