package co.charbox.dataapi.resources.crud;

import java.util.Set;

import org.elasticsearch.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;

import co.charbox.dataapi.auth.CharbotRoleValidator;
import co.charbox.dataapi.managers.UserManager;
import co.charbox.domain.model.RoleModel;
import co.charbox.domain.model.UserModel;
import co.charbox.domain.model.auth.CharbotAuthModel;

import com.tpofof.dwa.auth.IAuthValidator;
import com.tpofof.dwa.error.HttpUnauthorizedException;
import com.tpofof.dwa.resources.AuthRequestPermisionType;

public class UserResource extends CharbotAuthProtectedCrudResource<UserModel, UserManager> {

	@Autowired private CharbotRoleValidator authValidator;
	
	public UserResource(UserManager man) {
		super(man, UserModel.class);
	}

	@Override
	protected IAuthValidator<CharbotAuthModel, Integer, AuthRequestPermisionType> getValidator() {
		return null;
	}
	
	@Override
	protected void validate(CharbotAuthModel auth, Integer assetKey, AuthRequestPermisionType permType) throws HttpUnauthorizedException {
		Set<RoleModel> requiredRoles = Sets.newHashSet();
		switch (permType) {
		case READ_ONE:
			requiredRoles = Sets.newHashSet(RoleModel.getAdminRole(), RoleModel.getUserRole(assetKey));
		case CREATE:
		case COUNT:
		case DELETE:
		case READ:
		case UPDATE:
			requiredRoles = Sets.newHashSet(RoleModel.getAdminRole());
		}
		authValidator.validate(auth, assetKey, requiredRoles);
	}
}
