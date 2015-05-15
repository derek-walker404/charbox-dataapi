package co.charbox.dataapi.resources.crud.auth;

import io.dropwizard.auth.Auth;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.auth.AdminAuthValidator;
import co.charbox.dataapi.managers.auth.TokenAuthManager;
import co.charbox.domain.model.auth.AdminAuthModel;
import co.charbox.domain.model.auth.DeviceAuthModel;
import co.charbox.domain.model.auth.IAuthModel;
import co.charbox.domain.model.auth.ServerAuthModel;
import co.charbox.domain.model.auth.TokenAuthModel;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.JsonNode;
import com.tpofof.dwa.auth.IAuthValidator;
import com.tpofof.dwa.error.HttpCodeException;
import com.tpofof.dwa.error.HttpUnauthorizedException;
import com.tpofof.dwa.resources.AuthRequestPermisionType;
import com.tpofof.dwa.utils.RequestUtils;
import com.tpofof.dwa.utils.ResponseUtils;

@Path("/auth")
@Component
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

	@Autowired private ResponseUtils responseUtils;
	@Autowired private RequestUtils requestUtils;
	@Autowired private AdminAuthValidator authValidator;
	@Autowired private TokenAuthManager tokenManager;
	
	protected IAuthValidator<IAuthModel, String, AuthRequestPermisionType> getValidator() {
		return authValidator;
	}
	
	@Path("/validate/device")
	@GET
	@Timed
	public JsonNode validateDevice(@Auth IAuthModel auth) throws HttpCodeException {
		if (!auth.isActivated()) {
			throw new HttpUnauthorizedException("Credentials are not activated");
		}
		if (!auth.is(DeviceAuthModel.class)) {
			throw new HttpUnauthorizedException("Not authorized as device");
		}
		return responseUtils.success(responseUtils.rawData("valid", true));
	}
	
	@Path("/validate/admin")
	@GET
	@Timed
	public JsonNode validateAdmin(@Auth IAuthModel auth) throws HttpCodeException {
		if (!auth.isActivated()) {
			throw new HttpUnauthorizedException("Credentials are not activated");
		}
		if (!auth.is(AdminAuthModel.class)) {
			throw new HttpUnauthorizedException("Not authorized as admin");
		}
		return responseUtils.success(responseUtils.rawData("valid", true));
	}
	
	@Path("/validate/server")
	@GET
	@Timed
	public JsonNode validateServer(@Auth IAuthModel auth) throws HttpCodeException {
		if (!auth.isActivated()) {
			throw new HttpUnauthorizedException("Credentials are not activated");
		}
		if (!auth.is(ServerAuthModel.class)) {
			throw new HttpUnauthorizedException("Not authorized as server");
		}
		return responseUtils.success(responseUtils.rawData("valid", true));
	}
	
	@Path("/validate/token")
	@GET
	@Timed
	public JsonNode validateToken(@Auth IAuthModel auth) throws HttpCodeException {
		if (!auth.isActivated()) {
			throw new HttpUnauthorizedException("Credentials are not activated");
		}
		if (!auth.is(TokenAuthModel.class)) {
			throw new HttpUnauthorizedException("Not authorized as token");
		}
		return responseUtils.success(responseUtils.rawData("valid", true));
	}
}
