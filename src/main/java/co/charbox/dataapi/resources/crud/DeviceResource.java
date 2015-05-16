package co.charbox.dataapi.resources.crud;

import static com.tpofof.dwa.resources.AuthRequestPermisionType.READ_ONE;
import io.dropwizard.auth.Auth;

import java.util.List;
import java.util.Set;

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

import org.elasticsearch.common.collect.Sets;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.managers.DeviceManager;
import co.charbox.dataapi.managers.auth.TokenAuthManager;
import co.charbox.domain.model.Device;
import co.charbox.domain.model.Heartbeat;
import co.charbox.domain.model.TestCase;
import co.charbox.domain.model.TimerResult;
import co.charbox.domain.model.auth.TokenAuthModel;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Optional;
import com.tpofof.core.data.dao.ResultsSet;
import com.tpofof.core.security.IAuthModel;
import com.tpofof.dwa.auth.IAuthValidator;
import com.tpofof.dwa.auth.RoleValidator;
import com.tpofof.dwa.error.HttpBadRequestException;
import com.tpofof.dwa.error.HttpCodeException;
import com.tpofof.dwa.error.HttpInternalServerErrorException;
import com.tpofof.dwa.error.HttpNotFoundException;
import com.tpofof.dwa.error.HttpUnauthorizedException;
import com.tpofof.dwa.resources.AbstractAuthProtectedCrudResource;
import com.tpofof.dwa.resources.AuthRequestPermisionType;

@Path("/devices")
@Component
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DeviceResource extends AbstractAuthProtectedCrudResource<Device, String, DeviceManager, IAuthModel> {

	@Autowired private RoleValidator authValidator;
	@Autowired private TokenAuthManager tokenAuthManager;
	
	@Autowired
	public DeviceResource(DeviceManager man) {
		super(man, Device.class);
	}

	@Override
	protected IAuthValidator<IAuthModel, String, AuthRequestPermisionType> getValidator() {
		return null;
	}
	
	@Override
	protected void validate(IAuthModel auth, String assetKey, AuthRequestPermisionType permType) throws HttpUnauthorizedException {
		Set<String> requiredRoles = Sets.newHashSet();
		switch (permType) {
		case CREATE:
		case COUNT:
		case DELETE:
		case READ:
		case READ_ONE:
		case UPDATE:
			requiredRoles = Sets.newHashSet("ADMIN");
		}
		authValidator.validate(auth, assetKey, requiredRoles);
	}
	
	@Path("/{deviceId}/testcases")
	@GET
	@Timed
	public JsonNode getTestCases(@Auth IAuthModel authModel, @PathParam("deviceId") String deviceId) throws HttpCodeException {
		authValidator.validate(authModel, deviceId, Sets.newHashSet("ADMIN", deviceId));
		Device model = getManager().find(deviceId);
		if (model == null) {
			throw new HttpNotFoundException("Could not find Device with id " + deviceId);
		}
		List<TestCase> testCases = getManager().getTestCases(model);
		return res().success(res().listData(testCases, -1, -1, testCases.size()));
	}
	
	@Path("/id/{deviceId}")
	@GET
	@Timed
	public JsonNode findByDeviceId(@Auth IAuthModel authModel, @PathParam("deviceId") String deviceId) throws HttpCodeException {
		getValidator().validate(authModel, deviceId, READ_ONE);
		Device device = getManager().findByDeviceId(deviceId);
		return res().success(res().modelData(device));
	}
	
	@Path("/id/{deviceId}/testcases")
	@GET
	@Timed
	public JsonNode getTestCasesByDeviceId(@Auth IAuthModel authModel, @PathParam("deviceId") String deviceId) throws HttpCodeException {
		authValidator.validate(authModel, deviceId, Sets.newHashSet("ADMIN", deviceId));
		Device model = getManager().findByDeviceId(deviceId);
		if (model == null) {
			throw new HttpNotFoundException("Could not find Device with device id " + deviceId);
		}
		List<TestCase> testCases = getManager().getTestCases(model);
		return res().success(res().listData(testCases, -1, -1, testCases.size()));
	}
	
	@Path("/id/{deviceId}/register")
	@POST
	@Timed
	public JsonNode register(@Auth IAuthModel authModel, @PathParam("deviceId") String deviceId) throws HttpCodeException {
		authValidator.validate(authModel, deviceId, Sets.newHashSet("ADMIN", deviceId));
		Device device = getManager().findByDeviceId(deviceId);
		if (!device.isRegistered()) {
			device = getManager().register(deviceId);
		}
		return res().success(res().modelData(device));
	}
	
	@Path("/id/{deviceId}/results")
	@GET
	@Timed
	public JsonNode results(@Auth IAuthModel authModel, @PathParam("deviceId") String deviceId,
			@QueryParam("limit") Optional<Integer> limit,
			@QueryParam("offset") Optional<Integer> offset,
			@QueryParam("sort") Optional<String> sort) throws HttpCodeException {
		getValidator().validate(authModel, deviceId, READ_ONE);
		ResultsSet<TimerResult> results = getManager().getResults(deviceId, req().searchWindow(limit, offset), req().simpleSort(sort));
		return res().success(res().listData(results));
	}
	
	@Path("/id/{deviceId}/hb")
	@POST
	@Timed
	public JsonNode heartbeat(@Auth IAuthModel authModel, @PathParam("deviceId") String deviceId) throws HttpCodeException {
		authValidator.validate(authModel, deviceId, Sets.newHashSet("ADMIN", deviceId));
		if (getManager().findByDeviceId(deviceId) == null) {
			throw new HttpNotFoundException("Could not find device with id " + deviceId);
		}
		Heartbeat heartBeat = getManager().heartbeat(deviceId, new DateTime());;
		if (heartBeat == null) {
			throw new HttpInternalServerErrorException("Could not register heartbeat for device with id " + deviceId);
		}
		return res().success(res().modelData(heartBeat));
	}
	
	@Path("/id/{deviceId}/hb")
	@GET
	@Timed
	public JsonNode getDeviceHeartbeats(@Auth IAuthModel authModel, @PathParam("deviceId") String deviceId) throws HttpCodeException {
		getValidator().validate(authModel, deviceId, READ_ONE);
		Heartbeat hb = getManager().getHeartbeats(deviceId);
		if (hb == null) {
			throw new HttpNotFoundException("Cannot find heartbeat for device with id " + deviceId);
		}
		return res().success(res().modelData(hb));
	}
	
	@Override
	public JsonNode post(@Auth IAuthModel authModel, Device model, 
			@Context HttpServletRequest request) throws HttpCodeException {
		throw new HttpBadRequestException("POST not supported on Device collection. Use /device/register instead."); 
	}
	
	@Path("/id/{deviceId}/token/{serviceId}")
	@POST
	@Timed
	public JsonNode getServiceToken(@Auth IAuthModel auth, 
			@PathParam(value="deviceId") String deviceId,
			@PathParam(value="serviceId") String serviceId) {
		authValidator.validate(auth, deviceId, Sets.newHashSet("ADMIN", deviceId));
		TokenAuthModel token = tokenAuthManager.getNewToken(serviceId, deviceId);
		if (token == null) {
			throw new HttpInternalServerErrorException("Error creating token for service:" + serviceId + "\tdevice:" + deviceId);
		}
		return res().success(res().modelData(token));
	}
}
