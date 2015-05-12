package co.charbox.dataapi.resources.crud;

import static com.tpofof.dwa.resources.AuthRequestPermisionType.READ;
import static com.tpofof.dwa.resources.AuthRequestPermisionType.READ_ONE;
import io.dropwizard.auth.Auth;

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

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.auth.DeviceViewAuthValidator;
import co.charbox.dataapi.managers.DeviceManager;
import co.charbox.domain.model.Device;
import co.charbox.domain.model.Heartbeat;
import co.charbox.domain.model.TestCase;
import co.charbox.domain.model.TimerResult;
import co.charbox.domain.model.auth.IAuthModel;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Optional;
import com.tpofof.core.data.dao.ResultsSet;
import com.tpofof.core.data.dao.es.EsQuery;
import com.tpofof.dwa.auth.IAuthValidator;
import com.tpofof.dwa.error.HttpBadRequestException;
import com.tpofof.dwa.error.HttpCodeException;
import com.tpofof.dwa.error.HttpInternalServerErrorException;
import com.tpofof.dwa.error.HttpNotFoundException;
import com.tpofof.dwa.resources.AbstractAuthProtectedCrudResource;
import com.tpofof.dwa.resources.AuthRequestPermisionType;
import com.tpofof.dwa.utils.RequestUtils;
import com.tpofof.dwa.utils.ResponseUtils;

@Path("/devices")
@Component
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DeviceResource extends AbstractAuthProtectedCrudResource<Device, String, DeviceManager, EsQuery, QueryBuilder, SortBuilder, IAuthModel> {

	@Autowired private ResponseUtils responseUtils;
	@Autowired private RequestUtils requestUtils;
	@Autowired private DeviceViewAuthValidator authValidator;
	
	@Autowired
	public DeviceResource(DeviceManager man) {
		super(man, Device.class);
	}

	@Override
	protected IAuthValidator<IAuthModel, String, AuthRequestPermisionType> getValidator() {
		return authValidator;
	}
	
	@Override
	protected EsQuery getDefaultQuery(int limit, int offset) {
		return EsQuery.builder()
				.limit(limit)
				.offset(offset)
				.build();
	}
	
	@Path("/{deviceId}/testcases")
	@GET
	@Timed
	public JsonNode getTestCases(@Auth IAuthModel authModel, @PathParam("deviceId") String deviceId) throws HttpCodeException {
		getValidator().validate(authModel, deviceId, READ_ONE);
		Device model = getManager().find(deviceId);
		if (model == null) {
			throw new HttpNotFoundException("Could not find Device with id " + deviceId);
		}
		List<TestCase> testCases = getManager().getTestCases(model);
		return responseUtils.success(responseUtils.listData(testCases, -1, -1, testCases.size()));
	}
	
	@Path("/id/{deviceId}")
	@GET
	@Timed
	public JsonNode findByDeviceId(@Auth IAuthModel authModel, @PathParam("deviceId") String deviceId) throws HttpCodeException {
		getValidator().validate(authModel, deviceId, READ_ONE);
		Device device = getManager().findByDeviceId(deviceId);
		return responseUtils.success(responseUtils.modelData(device));
	}
	
	@Path("/id/{deviceId}/testcases")
	@GET
	@Timed
	public JsonNode getTestCasesByDeviceId(@Auth IAuthModel authModel, @PathParam("deviceId") String deviceId) throws HttpCodeException {
		getValidator().validate(authModel, deviceId, READ_ONE);
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
	public JsonNode register(@Auth IAuthModel authModel, @PathParam("deviceId") String deviceId) throws HttpCodeException {
		getValidator().validate(authModel, deviceId, READ_ONE);
		Device device = getManager().findByDeviceId(deviceId);
		if (!device.isRegistered()) {
			device = getManager().register(deviceId);
		}
		return responseUtils.success(responseUtils.modelData(device));
	}
	
	@Path("/id/{deviceId}/results")
	@GET
	@Timed
	public JsonNode results(@Auth IAuthModel authModel, @PathParam("deviceId") String deviceId,
			@QueryParam("limit") Optional<Integer> limit,
			@QueryParam("offset") Optional<Integer> offset) throws HttpCodeException {
		getValidator().validate(authModel, deviceId, READ);
		ResultsSet<TimerResult> results = getManager().getResults(deviceId, requestUtils.limit(limit), requestUtils.offset(offset));
		return responseUtils.success(responseUtils.listData(results));
	}
	
	@Path("/id/{deviceId}/hb")
	@POST
	@Timed
	public JsonNode heartbeat(@Auth IAuthModel authModel, @PathParam("deviceId") String deviceId) throws HttpCodeException {
		getValidator().validate(authModel, deviceId, READ_ONE);
		if (getManager().findByDeviceId(deviceId) == null) {
			throw new HttpNotFoundException("Could not find device with id " + deviceId);
		}
		Heartbeat heartBeat = getManager().heartbeat(deviceId, new DateTime());;
		if (heartBeat == null) {
			throw new HttpInternalServerErrorException("Could not register heartbeat for device with id " + deviceId);
		}
		return responseUtils.success(responseUtils.modelData(heartBeat));
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
		return responseUtils.success(responseUtils.modelData(hb));
	}
	
	@Override
	public JsonNode post(@Auth IAuthModel authModel, Device model, 
			@Context HttpServletRequest request) throws HttpCodeException {
		throw new HttpBadRequestException("POST not supported on Device collection. Use /device/register instead."); 
	}
}
