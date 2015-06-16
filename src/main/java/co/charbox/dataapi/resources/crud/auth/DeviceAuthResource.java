package co.charbox.dataapi.resources.crud.auth;

import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.elasticsearch.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.managers.auth.DeviceAuthManager;
import co.charbox.dataapi.resources.crud.CharbotAuthProtectedCrudResource;
import co.charbox.domain.model.auth.DeviceAuthModel;

import com.tpofof.core.security.IAuthModel;
import com.tpofof.dwa.auth.IAuthValidator;
import com.tpofof.dwa.auth.RoleValidator;
import com.tpofof.dwa.error.HttpUnauthorizedException;
import com.tpofof.dwa.resources.AuthRequestPermisionType;
import com.tpofof.dwa.utils.RequestUtils;
import com.tpofof.dwa.utils.ResponseUtils;

@Path("/deviceauth")
@Component
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DeviceAuthResource extends CharbotAuthProtectedCrudResource<DeviceAuthModel, DeviceAuthManager> {

	@Autowired private ResponseUtils responseUtils;
	@Autowired private RequestUtils requestUtils;
	@Autowired private RoleValidator authValidator;
	
	@Autowired
	public DeviceAuthResource(DeviceAuthManager man) {
		super(man, DeviceAuthModel.class);
	}
	
	@Override
	protected IAuthValidator<IAuthModel, String, AuthRequestPermisionType> getValidator() {
		return null;
	}
	
	@Override
	protected void validate(IAuthModel auth, String assetKey, AuthRequestPermisionType permType) throws HttpUnauthorizedException {
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
}
