package co.charbox.dataapi.resources.crud.auth;

import static com.tpofof.dwa.resources.AuthRequestPermisionType.DELETE;
import io.dropwizard.auth.Auth;

import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.elasticsearch.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.managers.auth.TokenAuthManager;
import co.charbox.dataapi.resources.crud.CharbotAuthProtectedCrudResource;
import co.charbox.domain.model.auth.DeviceAuthModel;
import co.charbox.domain.model.auth.TokenAuthModel;

import com.fasterxml.jackson.databind.JsonNode;
import com.tpofof.core.security.IAuthModel;
import com.tpofof.dwa.auth.IAuthValidator;
import com.tpofof.dwa.auth.RoleValidator;
import com.tpofof.dwa.error.HttpUnauthorizedException;
import com.tpofof.dwa.resources.AuthRequestPermisionType;
import com.tpofof.dwa.utils.RequestUtils;
import com.tpofof.dwa.utils.ResponseUtils;

@Path("/tokenauth")
@Component
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TokenAuthResource extends CharbotAuthProtectedCrudResource<TokenAuthModel, TokenAuthManager> {

	@Autowired private ResponseUtils responseUtils;
	@Autowired private RequestUtils requestUtils;
	@Autowired private RoleValidator authValidator;
	@Autowired private TokenAuthManager tokenAuthManager;
	
	@Autowired
	public TokenAuthResource(TokenAuthManager man) {
		super(man, TokenAuthModel.class);
	}
	
	@Override
	protected IAuthValidator<IAuthModel, Integer, AuthRequestPermisionType> getValidator() {
		return null;
	}
	
	@Override
	protected void validate(IAuthModel auth, Integer assetKey, AuthRequestPermisionType permType) throws HttpUnauthorizedException {
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
	
	@Path("/{serviceId}/new")
	@POST
	public JsonNode newToken(@Auth IAuthModel auth, @PathParam("serviceId") String serviceId) {
		authValidator.validate(auth, null, Sets.newHashSet("DEVICE"));
		DeviceAuthModel deviceAuth = auth.to(DeviceAuthModel.class);
		TokenAuthModel token = tokenAuthManager.getNewToken(serviceId, deviceAuth.getDeviceId());
		return res().success(res().modelData(token));
	}
	
	@Path("/expired")
	@DELETE
	public JsonNode deleteExpired(@Auth IAuthModel auth) {
		validate(auth, null, DELETE);
		int count = tokenAuthManager.deleteExpired();
		return res().success(res().rawData("deleted", count));
	}
}
