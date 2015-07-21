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

import co.charbox.dataapi.managers.auth.DeviceAuthManager;
import co.charbox.dataapi.managers.auth.ServerAuthManager;
import co.charbox.dataapi.managers.auth.TokenAuthManager;
import co.charbox.domain.data.CharbotSearchContext;
import co.charbox.domain.model.RoleModel;
import co.charbox.domain.model.auth.CharbotAuthModel;
import co.charbox.domain.model.auth.DeviceAuthModel;
import co.charbox.domain.model.auth.ServerAuthModel;
import co.charbox.domain.model.auth.TokenAuthModel;

import com.codahale.metrics.annotation.Timed;
import com.tpofof.dwa.error.HttpCodeException;
import com.tpofof.dwa.error.HttpUnauthorizedException;
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
	@Autowired private DeviceAuthManager deviceAuthMan;
	@Autowired private TokenAuthManager tokenManager;
	@Autowired private ServerAuthManager serverAuthMan;
	
	@Path("/validate/device")
	@GET
	@Timed
	public Response validateDevice(@Auth CharbotAuthModel auth) throws HttpCodeException {
		if (!auth.is(DeviceAuthModel.class)) {
			throw new HttpUnauthorizedException("Non Device");
		}
		DeviceAuthModel deviceAuth = auth.to(DeviceAuthModel.class);
		if (!deviceAuthMan.isValid(CharbotSearchContext.getSystemContext(), deviceAuth)) {
			throw new HttpUnauthorizedException("Non Valid Device");
		}
		return responseUtils.success(responseUtils.rawData("valid", true));
	}
	
	@Path("/validate/admin")
	@GET
	@Timed
	public Response validateAdmin(@Auth CharbotAuthModel auth) throws HttpCodeException {
		if (!auth.getRoles().contains(RoleModel.getAdminRole())) {
			throw new HttpUnauthorizedException("Non Admin User.");
		}
		return responseUtils.success(responseUtils.rawData("valid", true));
	}
	
	@Path("/validate/server")
	@GET
	@Timed
	public Response validateServer(@Auth CharbotAuthModel auth) throws HttpCodeException {
		if (!auth.is(ServerAuthModel.class)) {
			throw new HttpUnauthorizedException("Non Device");
		}
		ServerAuthModel serverAuth = auth.to(ServerAuthModel.class);
		if (serverAuthMan.isValid(CharbotSearchContext.getSystemContext(), serverAuth)) {
			throw new HttpUnauthorizedException("Non Valid Server");
		}
		return responseUtils.success(responseUtils.rawData("valid", true));
	}
	
	@Path("/validate/token")
	@GET
	@Timed
	public Response validateToken(@Auth CharbotAuthModel auth) throws HttpCodeException {
		if (!auth.is(TokenAuthModel.class)) {
			throw new HttpUnauthorizedException("Non Device");
		}
		TokenAuthModel tokenAuth = auth.to(TokenAuthModel.class);
		if (!tokenManager.isValid(CharbotSearchContext.getSystemContext(), tokenAuth)) {
			throw new HttpUnauthorizedException("Non Valid Token");
		}
		return responseUtils.success(responseUtils.rawData("valid", true));
	}
}
