package co.charbox.dataapi.resources.crud.auth;

import io.dropwizard.auth.Auth;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.auth.AdminAuthValidator;
import co.charbox.dataapi.managers.auth.TokenAuthManager;
import co.charbox.domain.model.auth.DeviceAuthModel;
import co.charbox.domain.model.auth.IAuthModel;
import co.charbox.domain.model.auth.TokenAuthModel;

import com.fasterxml.jackson.databind.JsonNode;
import com.tpofof.dwa.auth.IAuthValidator;
import com.tpofof.dwa.error.HttpUnauthorizedException;
import com.tpofof.dwa.resources.AbstractAuthProtectedCrudResource;
import com.tpofof.dwa.resources.AuthRequestPermisionType;
import com.tpofof.dwa.utils.RequestUtils;
import com.tpofof.dwa.utils.ResponseUtils;

@Path("/tokenauth")
@Component
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TokenAuthResource extends AbstractAuthProtectedCrudResource<TokenAuthModel, String, TokenAuthManager, IAuthModel> {

	@Autowired private ResponseUtils responseUtils;
	@Autowired private RequestUtils requestUtils;
	@Autowired private AdminAuthValidator authValidator;
	@Autowired private TokenAuthManager tokenAuthManager;
	
	@Autowired
	public TokenAuthResource(TokenAuthManager man) {
		super(man, TokenAuthModel.class);
	}
	
	@Override
	protected IAuthValidator<IAuthModel, String, AuthRequestPermisionType> getValidator() {
		return authValidator;
	}
	
	@Path("/{serviceId}/new")
	@POST
	public JsonNode newToken(@Auth IAuthModel auth, @PathParam("serviceId") String serviceId) {
		if (!auth.isActivated()) {
			throw new HttpUnauthorizedException("Authorization is not activated");
		}
		if (!auth.is(DeviceAuthModel.class)) {
			throw new HttpUnauthorizedException("Tokens are only available for devices.");
		}
		DeviceAuthModel deviceAuth = auth.to(DeviceAuthModel.class);
		TokenAuthModel token = tokenAuthManager.getNewToken(serviceId, deviceAuth.getDeviceId());
		return res().success(res().modelData(token));
	}
}
