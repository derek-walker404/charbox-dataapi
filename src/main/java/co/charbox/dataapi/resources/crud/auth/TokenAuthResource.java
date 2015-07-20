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
import javax.ws.rs.core.Response;

import org.elasticsearch.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.auth.CharbotRoleValidator;
import co.charbox.dataapi.managers.auth.TokenAuthManager;
import co.charbox.dataapi.resources.crud.CharbotAuthProtectedCrudResource;
import co.charbox.domain.model.RoleModel;
import co.charbox.domain.model.auth.CharbotAuthModel;
import co.charbox.domain.model.auth.DeviceAuthModel;
import co.charbox.domain.model.auth.TokenAuthModel;

import com.tpofof.dwa.auth.IAuthValidator;
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
	@Autowired private CharbotRoleValidator authValidator;
	@Autowired private TokenAuthManager tokenAuthManager;
	
	@Autowired
	public TokenAuthResource(TokenAuthManager man) {
		super(man, TokenAuthModel.class);
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
	
	@Path("/{serviceId}/new")
	@POST
	public Response newToken(@Auth CharbotAuthModel auth, @PathParam("serviceId") String serviceId) {
		authValidator.validate(auth, null, Sets.newHashSet(RoleModel.getDeviceRole()));
		DeviceAuthModel deviceAuth = auth.to(DeviceAuthModel.class);
		TokenAuthModel token = tokenAuthManager.getNewToken(serviceId, deviceAuth.getDeviceId());
		return res().success(res().modelData(token));
	}
	
	@Path("/expired")
	@DELETE
	public Response deleteExpired(@Auth CharbotAuthModel auth) {
		validate(auth, null, DELETE);
		int count = tokenAuthManager.deleteExpired();
		return res().success(res().rawData("deleted", count));
	}
}
