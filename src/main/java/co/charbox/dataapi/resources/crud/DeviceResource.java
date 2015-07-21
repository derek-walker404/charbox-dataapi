package co.charbox.dataapi.resources.crud;

import static com.tpofof.dwa.resources.AuthRequestPermisionType.READ_ONE;
import io.dropwizard.auth.Auth;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.elasticsearch.common.collect.Sets;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.auth.CharbotRoleValidator;
import co.charbox.dataapi.managers.DeviceConfigurationManager;
import co.charbox.dataapi.managers.DeviceManager;
import co.charbox.dataapi.managers.auth.TokenAuthManager;
import co.charbox.domain.model.DeviceConfigurationModel;
import co.charbox.domain.model.DeviceModel;
import co.charbox.domain.model.HeartbeatModel;
import co.charbox.domain.model.OutageModel;
import co.charbox.domain.model.PingResultModel;
import co.charbox.domain.model.RoleModel;
import co.charbox.domain.model.SstResultsModel;
import co.charbox.domain.model.auth.CharbotAuthModel;
import co.charbox.domain.model.auth.TokenAuthModel;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;
import com.tpofof.core.data.dao.ResultsSet;
import com.tpofof.core.utils.Config;
import com.tpofof.dwa.auth.IAuthValidator;
import com.tpofof.dwa.error.HttpBadRequestException;
import com.tpofof.dwa.error.HttpCodeException;
import com.tpofof.dwa.error.HttpInternalServerErrorException;
import com.tpofof.dwa.error.HttpNotFoundException;
import com.tpofof.dwa.error.HttpUnauthorizedException;
import com.tpofof.dwa.resources.AuthRequestPermisionType;

@Path("/devices")
@Component
@Consumes(MediaType.APPLICATION_JSON)
public class DeviceResource extends CharbotAuthProtectedCrudResource<DeviceModel, DeviceManager> {

	@Autowired private CharbotRoleValidator authValidator;
	@Autowired private TokenAuthManager tokenAuthManager;
	@Autowired private DeviceConfigurationManager configMan;
	@Autowired private Config config;
	
	@Autowired
	public DeviceResource(DeviceManager man) {
		super(man, DeviceModel.class);
	}

	@Override
	protected IAuthValidator<CharbotAuthModel, Integer, AuthRequestPermisionType> getValidator() {
		return null;
	}
	
	@Override
	protected void validate(CharbotAuthModel auth, Integer assetKey, AuthRequestPermisionType permType) throws HttpUnauthorizedException {
		Set<RoleModel> requiredRoles = Sets.newHashSet();
		switch (permType) {
		case CREATE:
		case COUNT:
		case DELETE:
		case READ:
		case READ_ONE:
		case UPDATE:
			requiredRoles = Sets.newHashSet(RoleModel.getAdminRole());
		}
		authValidator.validate(auth, assetKey, requiredRoles);
	}
	
	@Path("/id/{deviceId}")
	@GET
	@Timed
	public Response findByDeviceId(@Auth CharbotAuthModel authModel, @PathParam("deviceId") Integer deviceId) throws HttpCodeException {
		validate(authModel, deviceId, READ_ONE);
		DeviceModel device = getManager().find(getAuthContext(authModel), deviceId);
		return res().success(res().modelData(device));
	}
	
	@Path("/id/{deviceId}/register")
	@POST
	@Timed
	public Response register(@Auth CharbotAuthModel authModel, @PathParam("deviceId") Integer deviceId) throws HttpCodeException {
		authValidator.validate(authModel, deviceId, Sets.newHashSet(RoleModel.getAdminRole(), RoleModel.getDeviceRole(deviceId)));
		DeviceModel device = getManager().find(getAuthContext(authModel), deviceId);
		if (device != null) {
			DeviceConfigurationModel conf = configMan.findByDeviceId(getContext(authModel), deviceId);
			if (conf == null || !conf.isRegistered()) {
				device = getManager().register(getAuthContext(authModel), device);
			}
		}
		return res().success(res().modelData(device));
	}
	
	@Path("/id/{deviceId}/hb")
	@POST
	@Timed
	public Response heartbeat(@Auth CharbotAuthModel authModel,
			@PathParam("deviceId") Integer deviceId,
			@Context HttpServletRequest request) throws HttpCodeException {
		if (getManager().find(getAuthContext(authModel), deviceId) == null) {
			throw new HttpNotFoundException("Could not find device with id " + deviceId);
		}
		String clientIp = request.getRemoteAddr();
		if (clientIp == null || clientIp.isEmpty() || "127.0.0.1".equals(clientIp)) {
			// might be a proxy or local host, but something is better than nothing.
			clientIp = config.getString("location.ip.override");
		}
		clientIp = config.getString("location.ip.override", clientIp);
		HeartbeatModel heartBeat = getManager().heartbeat(getContext(authModel), deviceId, new DateTime(), clientIp);
		if (heartBeat == null) {
			throw new HttpInternalServerErrorException("Could not register heartbeat for device with id " + deviceId);
		}
		return res().success(res().modelData(heartBeat));
	}
	
	@Path("/id/{deviceId}/hb")
	@GET
	@Timed
	public Response getDeviceHeartbeats(@Auth CharbotAuthModel authModel, @PathParam("deviceId") Integer deviceId) throws HttpCodeException {
		validate(authModel, deviceId, READ_ONE);
		HeartbeatModel hb = getManager().getHeartbeat(getAuthContext(authModel), deviceId);
		if (hb == null) {
			throw new HttpNotFoundException("Cannot find heartbeat for device with id " + deviceId);
		}
		return res().success(res().modelData(hb));
	}
	
	@Path("/id/{deviceId}/pingres")
	@GET
	@Timed
	public Response getDevicePingResults(@Auth CharbotAuthModel authModel, 
			@PathParam("deviceId") Integer deviceId,
			@QueryParam("limit") Optional<Integer> limit,
			@QueryParam("offset") Optional<Integer> offset,
			@QueryParam("sort") Optional<String> sort) throws HttpCodeException {
		validate(authModel, deviceId, READ_ONE);
		ResultsSet<PingResultModel> pings = getManager().getPingResults(getContext(authModel, limit, offset, sort), deviceId);
		if (pings == null) {
			throw new HttpNotFoundException("Cannot find ping results for device with id " + deviceId);
		}
		return res().success(res().listData(pings));
	}
	
	@Path("/id/{deviceId}/outages")
	@GET
	@Timed
	public Response getDeviceOutages(@Auth CharbotAuthModel authModel, 
			@PathParam("deviceId") Integer deviceId,
			@QueryParam("limit") Optional<Integer> limit,
			@QueryParam("offset") Optional<Integer> offset,
			@QueryParam("sort") Optional<String> sort) throws HttpCodeException {
		validate(authModel, deviceId, READ_ONE);
		ResultsSet<OutageModel> outage = getManager().getOutages(getContext(authModel, limit, offset, sort), deviceId);
		if (outage == null) {
			throw new HttpNotFoundException("Cannot find outages for device with id " + deviceId);
		}
		return res().success(res().listData(outage));
	}
	
	@Path("/id/{deviceId}/sst")
	@GET
	@Timed
	public Response getDeviceSstResults(@Auth CharbotAuthModel authModel, 
			@PathParam("deviceId") Integer deviceId,
			@QueryParam("limit") Optional<Integer> limit,
			@QueryParam("offset") Optional<Integer> offset,
			@QueryParam("sort") Optional<String> sort) throws HttpCodeException {
		validate(authModel, deviceId, READ_ONE);
		ResultsSet<SstResultsModel> outage = getManager().getSstResults(getContext(authModel, limit, offset, sort), deviceId);
		if (outage == null) {
			throw new HttpNotFoundException("Cannot find outages for device with id " + deviceId);
		}
		return res().success(res().listData(outage));
	}
	
	@Override
	public Response post(@Auth CharbotAuthModel authModel, DeviceModel model, 
			@Context HttpServletRequest request) throws HttpCodeException {
		throw new HttpBadRequestException("POST not supported on Device collection. Use /device/register instead."); 
	}
	
	@Path("/id/{deviceId}/token/{serviceId}")
	@POST
	@Timed
	public Response getServiceToken(@Auth CharbotAuthModel auth, 
			@PathParam(value="deviceId") Integer deviceId,
			@PathParam(value="serviceId") String serviceId) {
		authValidator.validate(auth, deviceId, Sets.newHashSet(RoleModel.getAdminRole(), RoleModel.getDeviceRole(deviceId)));
		TokenAuthModel token = tokenAuthManager.getNewToken(getAuthContext(auth), serviceId, deviceId);
		if (token == null) {
			throw new HttpInternalServerErrorException("Error creating token for service:" + serviceId + "\tdevice:" + deviceId);
		}
		return res().success(res().modelData(token));
	}
}
