package com.tpofof.conmon.server.resources;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Optional;
import com.pofof.conmon.model.Device;
import com.tpofof.conmon.server.managers.DeviceManager;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/devices")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DeviceResource {

	private final DeviceManager deviceMan;
	
	public DeviceResource(DeviceManager deviceMan) {
		this.deviceMan = deviceMan;
	}
	
	@GET
	@Timed
	public JsonNode find(@QueryParam("limit") Optional<Integer> limit,
			@QueryParam("offset") Optional<Integer> offset) {
		int limitVal = limit.isPresent() && limit.get() > 0 ? limit.get() : 10;
		int offsetVal = offset.isPresent() && offset.get() >= 0 ? offset.get() : 0;
		List<Device> devices = deviceMan.getDevices(limitVal, offsetVal);
		return ResponseUtils.success(ResponseUtils.listData(devices, limitVal, offsetVal));
	}
	
	@Path("/{_id}")
	@GET
	@Timed
	public JsonNode find(@PathParam("_id") String id) {
		Device device = deviceMan.getDevice(id);
		return device != null
				? ResponseUtils.success(ResponseUtils.modelData(device))
				: ResponseUtils.failure("Could not find Device with id " + id, 404);
	}
	
	@Path("/id/{deviceId}")
	@GET
	@Timed
	public JsonNode findByDeviceId(@PathParam("deviceId") Integer deviceId) {
		Device device = deviceMan.findByDeviceId(deviceId);
		return ResponseUtils.success(ResponseUtils.modelData(device));
	}
	
	@Path("/register/{deviceId}")
	@POST
	@Timed
	public JsonNode register(@PathParam("deviceId") Integer deviceId) {
		Device device = deviceMan.register(deviceId);
		return ResponseUtils.success(ResponseUtils.modelData(device));
	}
}
