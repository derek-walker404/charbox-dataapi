package com.tpofof.conmon.server.resources.crud;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.pofof.conmon.model.Heartbeat;
import com.tpofof.conmon.server.managers.HeartbeatManager;

@Path("/hb")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HeartbeatResource extends GenericCrudResource<Heartbeat, HeartbeatManager> {

	public HeartbeatResource(HeartbeatManager man) {
		super(man, Heartbeat.class);
	}
}
