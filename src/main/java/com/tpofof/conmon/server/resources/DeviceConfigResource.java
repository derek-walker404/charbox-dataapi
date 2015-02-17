package com.tpofof.conmon.server.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.pofof.conmon.model.DeviceConfiguration;
import com.tpofof.conmon.server.mongo.DeviceConfigDAO;

@Path("/configs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DeviceConfigResource extends GenericCrudResource<DeviceConfiguration, DeviceConfigDAO> {
	
	public DeviceConfigResource(DeviceConfigDAO dao) {
		super(dao, DeviceConfiguration.class);
	}
}
