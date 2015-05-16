package co.charbox.dataapi.resources.crud.auth;

import io.dropwizard.auth.Auth;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.managers.auth.TokenAuthManager;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Sets;
import com.tpofof.core.security.IAuthModel;
import com.tpofof.dwa.auth.RoleValidator;
import com.tpofof.dwa.error.HttpCodeException;
import com.tpofof.dwa.utils.RequestUtils;
import com.tpofof.dwa.utils.ResponseUtils;

@Path("/auth")
@Component
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

	@Autowired private ResponseUtils responseUtils;
	@Autowired private RequestUtils requestUtils;
	@Autowired private RoleValidator authValidator;
	@Autowired private TokenAuthManager tokenManager;
	
	@Path("/validate/device")
	@GET
	@Timed
	public JsonNode validateDevice(@Auth IAuthModel auth) throws HttpCodeException {
		authValidator.validate(auth, null, Sets.newHashSet("DEVICE"));
		return responseUtils.success(responseUtils.rawData("valid", true));
	}
	
	@Path("/validate/admin")
	@GET
	@Timed
	public JsonNode validateAdmin(@Auth IAuthModel auth) throws HttpCodeException {
		authValidator.validate(auth, null, Sets.newHashSet("ADMIN"));
		return responseUtils.success(responseUtils.rawData("valid", true));
	}
	
	@Path("/validate/server")
	@GET
	@Timed
	public JsonNode validateServer(@Auth IAuthModel auth) throws HttpCodeException {
		authValidator.validate(auth, null, Sets.newHashSet("SERVER"));
		return responseUtils.success(responseUtils.rawData("valid", true));
	}
	
	@Path("/validate/token")
	@GET
	@Timed
	public JsonNode validateToken(@Auth IAuthModel auth) throws HttpCodeException {
		authValidator.validate(auth, null, Sets.newHashSet("TOKEN"));
		return responseUtils.success(responseUtils.rawData("valid", true));
	}
}
