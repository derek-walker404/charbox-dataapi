package com.tpofof.conmon.server.resources.crud;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.pofof.conmon.model.TimerResult;
import com.tpofof.conmon.server.managers.TimerResultManager;

@Path("/results")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TimerResultResource extends GenericCrudResource<TimerResult, TimerResultManager> {

	public TimerResultResource(TimerResultManager man) {
		super(man, TimerResult.class);
	}
}
