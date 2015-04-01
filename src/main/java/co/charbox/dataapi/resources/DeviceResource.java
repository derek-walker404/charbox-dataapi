package co.charbox.dataapi.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import co.charbox.core.data.SearchResults;
import co.charbox.dataapi.managers.DeviceManager;
import co.charbox.domain.model.Device;
import co.charbox.domain.model.Heartbeat;
import co.charbox.domain.model.TestCase;
import co.charbox.domain.model.TimerResult;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Optional;

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
		SearchResults<Device> results = deviceMan.find(limitVal, offsetVal);
		return ResponseUtils.success(ResponseUtils.listData(results));
	}
	
	@Path("/{_id}")
	@GET
	@Timed
	public JsonNode find(@PathParam("_id") String id) {
		Device device = deviceMan.find(id);
		return device != null
				? ResponseUtils.success(ResponseUtils.modelData(device))
				: ResponseUtils.failure("Could not find Device with id " + id, 404);
	}
	
	@Path("/{_id}/testcases")
	@GET
	@Timed
	public JsonNode getTestCases(@PathParam("_id") String id) {
		Device model = deviceMan.find(id);
		if (model != null) {
			List<TestCase> testCases = deviceMan.getTestCases(model);
			return ResponseUtils.success(ResponseUtils.listData(testCases, -1, -1));
		}
		return ResponseUtils.failure("Could not find Device with id " + id, 404);
	}
	
	@Path("/id/{deviceId}")
	@GET
	@Timed
	public JsonNode findByDeviceId(@PathParam("deviceId") Integer deviceId) {
		Device device = deviceMan.findByDeviceId(deviceId);
		return ResponseUtils.success(ResponseUtils.modelData(device));
	}
	
	@Path("/id/{deviceId}/testcases")
	@GET
	@Timed
	public JsonNode getTestCasesByDeviceId(@PathParam("deviceId") Integer deviceId) {
		Device model = deviceMan.findByDeviceId(deviceId);
		if (model != null) {
			List<TestCase> testCases = deviceMan.getTestCases(model);
			return ResponseUtils.success(ResponseUtils.listData(testCases, -1, -1));
		}
		return ResponseUtils.failure("Could not find Device with device id " + deviceId, 404);
	}
	
	@Path("/id/{deviceId}/register")
	@POST
	@Timed
	public JsonNode register(@PathParam("deviceId") Integer deviceId) {
		Device device = deviceMan.register(deviceId);
		return ResponseUtils.success(ResponseUtils.modelData(device));
	}
	
	@Path("/id/{deviceId}/results")
	@GET
	@Timed
	public JsonNode results(@PathParam("deviceId") Integer deviceId,
			@QueryParam("limit") Optional<Integer> limit,
			@QueryParam("offset") Optional<Integer> offset) {
		int lim = -1;
		int off = -1;
		SearchResults<TimerResult> results;
		if (limit.isPresent() && limit.get() > 0 && offset.isPresent() && offset.get() >= 0) {
			lim = limit.get();
			off = offset.get();
			results = deviceMan.getResults(deviceId, lim, off);
		} else {
			results = deviceMan.getResults(deviceId);
		}
		return ResponseUtils.success(ResponseUtils.listData(results));
	}
	
	@Path("/id/{deviceId}/hb")
	@POST
	@Timed
	public JsonNode heartbeat(@PathParam("deviceId") Integer deviceId) {
		if (deviceMan.findByDeviceId(deviceId) == null) {
			return ResponseUtils.failure("Could not find device with id " + deviceId, 404);
		}
		Heartbeat heartBeat = deviceMan.heartbeat(deviceId, System.currentTimeMillis());;
		return heartBeat != null 
				? ResponseUtils.success(ResponseUtils.modelData(heartBeat))
				: ResponseUtils.failure("Could not register heartbeat for device with id " + deviceId, 500);
	}
	
	@Path("/id/{deviceId}/hb")
	@GET
	@Timed
	public JsonNode getDeviceHeartbeats(@PathParam("deviceId") Integer deviceId) {
		SearchResults<Heartbeat> results = deviceMan.getHeartbeats(deviceId);
		return ResponseUtils.success(ResponseUtils.listData(results));
	}
}
