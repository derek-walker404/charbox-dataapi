package co.charbox.dataapi.resources.crud.auth;

import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.elasticsearch.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.auth.CharbotRoleValidator;
import co.charbox.dataapi.managers.auth.ServerAuthManager;
import co.charbox.dataapi.resources.crud.CharbotAuthProtectedCrudResource;
import co.charbox.domain.model.RoleModel;
import co.charbox.domain.model.auth.CharbotAuthModel;
import co.charbox.domain.model.auth.ServerAuthModel;

import com.tpofof.dwa.auth.IAuthValidator;
import com.tpofof.dwa.error.HttpUnauthorizedException;
import com.tpofof.dwa.resources.AuthRequestPermisionType;
import com.tpofof.dwa.utils.RequestUtils;
import com.tpofof.dwa.utils.ResponseUtils;

@Path("/serverauth")
@Component
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ServiceAuthResource extends CharbotAuthProtectedCrudResource<ServerAuthModel, ServerAuthManager> {

	@Autowired private ResponseUtils responseUtils;
	@Autowired private RequestUtils requestUtils;
	@Autowired private CharbotRoleValidator authValidator;
	
	@Autowired
	public ServiceAuthResource(ServerAuthManager man) {
		super(man, ServerAuthModel.class);
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
}
