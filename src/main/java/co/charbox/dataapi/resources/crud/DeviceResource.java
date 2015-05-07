package co.charbox.dataapi.resources.crud;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.managers.DeviceManager;
import co.charbox.domain.model.Device;
import co.charbox.domain.model.Heartbeat;
import co.charbox.domain.model.TestCase;
import co.charbox.domain.model.TimerResult;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Optional;
import com.tpofof.core.data.dao.ResultsSet;
import com.tpofof.dwa.error.HttpBadRequestException;
import com.tpofof.dwa.error.HttpCodeException;
import com.tpofof.dwa.error.HttpInternalServerErrorException;
import com.tpofof.dwa.error.HttpNotFoundException;
import com.tpofof.dwa.resources.AbstractCrudResource;
import com.tpofof.dwa.utils.RequestUtils;
import com.tpofof.dwa.utils.ResponseUtils;

@Path("/devices")
@Component
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DeviceResource extends AbstractCrudResource<Device, String, DeviceManager> {

	@Autowired private ResponseUtils responseUtils;
	@Autowired private RequestUtils requestUtils;
	
	@Autowired
	public DeviceResource(DeviceManager man) {
		super(man, Device.class);
	}
	
	@Path("/{_id}/testcases")
	@GET
	@Timed
	public JsonNode getTestCases(@PathParam("_id") String id) throws HttpNotFoundException {
		Device model = getManager().find(id);
		if (model == null) {
			throw new HttpNotFoundException("Could not find Device with id " + id);
		}
		List<TestCase> testCases = getManager().getTestCases(model);
		return responseUtils.success(responseUtils.listData(testCases, -1, -1, testCases.size()));
	}
	
	@Path("/id/{deviceId}")
	@GET
	@Timed
	public JsonNode findByDeviceId(@PathParam("deviceId") String deviceId) {
		Device device = getManager().findByDeviceId(deviceId);
		return responseUtils.success(responseUtils.modelData(device));
	}
	
	@Path("/id/{deviceId}/testcases")
	@GET
	@Timed
	public JsonNode getTestCasesByDeviceId(@PathParam("deviceId") String deviceId) throws HttpNotFoundException {
		Device model = getManager().findByDeviceId(deviceId);
		if (model == null) {
			throw new HttpNotFoundException("Could not find Device with device id " + deviceId);
		}
		List<TestCase> testCases = getManager().getTestCases(model);
		return responseUtils.success(responseUtils.listData(testCases, -1, -1, testCases.size()));
	}
	
	@Path("/id/{deviceId}/register")
	@POST
	@Timed
	public JsonNode register(@PathParam("deviceId") String deviceId) {
		Device device = getManager().register(deviceId);
		return responseUtils.success(responseUtils.modelData(device));
	}
	
	@Path("/id/{deviceId}/results")
	@GET
	@Timed
	public JsonNode results(@PathParam("deviceId") String deviceId,
			@QueryParam("limit") Optional<Integer> limit,
			@QueryParam("offset") Optional<Integer> offset) {
		ResultsSet<TimerResult> results = getManager().getResults(deviceId, requestUtils.limit(limit), requestUtils.offset(offset));
		return responseUtils.success(responseUtils.listData(results));
	}
	
	@Path("/id/{deviceId}/hb")
	@POST
	@Timed
	public JsonNode heartbeat(@PathParam("deviceId") String deviceId) throws HttpCodeException {
		if (getManager().findByDeviceId(deviceId) == null) {
			throw new HttpNotFoundException("Could not find device with id " + deviceId);
		}
		Heartbeat heartBeat = getManager().heartbeat(deviceId, System.currentTimeMillis());;
		if (heartBeat == null) {
			throw new HttpInternalServerErrorException("Could not register heartbeat for device with id " + deviceId);
		}
		return responseUtils.success(responseUtils.modelData(heartBeat));
	}
	
	@Path("/id/{deviceId}/hb")
	@GET
	@Timed
	public JsonNode getDeviceHeartbeats(@PathParam("deviceId") String deviceId) throws HttpCodeException {
		Heartbeat hb = getManager().getHeartbeats(deviceId);
		if (hb == null) {
			throw new HttpNotFoundException("Cannot find heartbeat for device with id " + deviceId);
		}
		return responseUtils.success(responseUtils.modelData(hb));
	}
	
	@Override
	public JsonNode post(Device model, @Context HttpServletRequest request) throws HttpCodeException {
		throw new HttpBadRequestException("POST not supported on Device collection. Use /device/register instead."); 
	}
}
