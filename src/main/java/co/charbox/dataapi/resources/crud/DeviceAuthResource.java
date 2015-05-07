package co.charbox.dataapi.resources.crud;

import io.dropwizard.auth.Auth;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.managers.DeviceAuthManager;
import co.charbox.domain.model.DeviceAuthModel;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.JsonNode;
import com.tpofof.dwa.resources.AbstractCrudResource;
import com.tpofof.dwa.utils.RequestUtils;
import com.tpofof.dwa.utils.ResponseUtils;

@Path("/auth")
@Component
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DeviceAuthResource extends AbstractCrudResource<DeviceAuthModel, String, DeviceAuthManager> {

	@Autowired private ResponseUtils responseUtils;
	@Autowired private RequestUtils requestUtils;
	
	@Autowired
	public DeviceAuthResource(DeviceAuthManager man) {
		super(man, DeviceAuthModel.class);
	}
	
	@Path("/validate")
	@GET
	@Timed
	public JsonNode validate(@Auth DeviceAuthModel auth) {
		return responseUtils.success(responseUtils.rawData("valid", true));
	}
}
