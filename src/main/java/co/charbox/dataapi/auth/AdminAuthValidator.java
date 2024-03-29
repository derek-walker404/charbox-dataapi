package co.charbox.dataapi.auth;

import org.springframework.stereotype.Component;

import co.charbox.domain.model.RoleModel;

import com.tpofof.core.security.IAuthModel;
import com.tpofof.dwa.auth.IAuthValidator;
import com.tpofof.dwa.error.HttpUnauthorizedException;
import com.tpofof.dwa.resources.AuthRequestPermisionType;

@Component
public class AdminAuthValidator implements IAuthValidator<IAuthModel<RoleModel>, String, AuthRequestPermisionType> {

	@Override
	public void validate(IAuthModel<RoleModel> authModel, String assetKey, AuthRequestPermisionType permType) throws HttpUnauthorizedException {
		if (!authModel.isActivated()) {
			throw new HttpUnauthorizedException("Credentials are not activated.");
		}
		if (!authModel.isAdmin()) {
			throw new HttpUnauthorizedException("Requires admin permissions.");
		}
	}
}
