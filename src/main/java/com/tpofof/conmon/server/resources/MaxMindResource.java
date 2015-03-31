package com.tpofof.conmon.server.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.JsonNode;
import com.pofof.conmon.mm.MaxMindService;
import com.pofof.conmon.model.mm.ConnectionInfoModel;

@Path("/mm")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MaxMindResource {

	private final MaxMindService mms;
	
	public MaxMindResource(MaxMindService mms) {
		this.mms = mms;
	}
	
	@Path("/{ip}")
	@GET
	@Timed
	public JsonNode getIp(@PathParam("ip") String ip) {
		ConnectionInfoModel conInfo = mms.get(ip);
		return conInfo != null
				? ResponseUtils.success(ResponseUtils.modelData(conInfo))
				: ResponseUtils.failure("Could not retrieve info for " + ip, 500);
	}
}
