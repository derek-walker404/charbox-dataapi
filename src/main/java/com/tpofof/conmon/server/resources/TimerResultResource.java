package com.tpofof.conmon.server.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Path("/results")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TimerResultResource {

	private static final ObjectMapper mapper = new ObjectMapper();
	
	@POST
	@Timed
	public JsonNode postResult(Object result) {
		ObjectNode node = mapper.createObjectNode();
		
		return node;
	}
}
