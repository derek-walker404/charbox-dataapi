package co.charbox.dataapi.resources.crud.auth;

import io.dropwizard.auth.Auth;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.auth.CharbotRoleValidator;
import co.charbox.dataapi.managers.auth.TokenAuthManager;
import co.charbox.domain.model.RoleModel;
import co.charbox.domain.model.auth.CharbotAuthModel;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.Sets;
import com.tpofof.dwa.error.HttpCodeException;
import com.tpofof.dwa.resources.IDwaResource;
import com.tpofof.dwa.utils.RequestUtils;
import com.tpofof.dwa.utils.ResponseUtils;

@Path("/auth")
@Component
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource implements IDwaResource {

	@Autowired private ResponseUtils responseUtils;
	@Autowired private RequestUtils requestUtils;
	@Autowired private CharbotRoleValidator authValidator;
	@Autowired private TokenAuthManager tokenManager;
	
	@Path("/validate/device")
	@GET
	@Timed
	public Response validateDevice(@Auth CharbotAuthModel auth) throws HttpCodeException {
		authValidator.validate(auth, null, Sets.newHashSet(RoleModel.getDeviceRole()));
		return responseUtils.success(responseUtils.rawData("valid", true));
	}
	
	@Path("/validate/admin")
	@GET
	@Timed
	public Response validateAdmin(@Auth CharbotAuthModel auth) throws HttpCodeException {
		authValidator.validate(auth, null, Sets.newHashSet(RoleModel.getAdminRole()));
		return responseUtils.success(responseUtils.rawData("valid", true));
	}
	
	@Path("/validate/server")
	@GET
	@Timed
	public Response validateServer(@Auth CharbotAuthModel auth) throws HttpCodeException {
		authValidator.validate(auth, null, Sets.newHashSet(RoleModel.getServiceRole()));
		return responseUtils.success(responseUtils.rawData("valid", true));
	}
	
	@Path("/validate/token")
	@GET
	@Timed
	public Response validateToken(@Auth CharbotAuthModel auth) throws HttpCodeException {
		authValidator.validate(auth, null, Sets.newHashSet(RoleModel.getTokenRole()));
		return responseUtils.success(responseUtils.rawData("valid", true));
	}
}
