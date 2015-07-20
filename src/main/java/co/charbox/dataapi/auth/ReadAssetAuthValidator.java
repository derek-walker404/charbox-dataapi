package co.charbox.dataapi.auth;

import org.springframework.stereotype.Component;

import co.charbox.domain.model.RoleModel;
import co.charbox.domain.model.auth.DeviceAuthModel;

import com.tpofof.core.security.IAuthModel;
import com.tpofof.dwa.auth.IAuthValidator;
import com.tpofof.dwa.error.HttpUnauthorizedException;
import com.tpofof.dwa.resources.AuthRequestPermisionType;

@Component
public class ReadAssetAuthValidator implements IAuthValidator<IAuthModel<RoleModel>, String, AuthRequestPermisionType> {

	public void validate(IAuthModel<RoleModel> authModel, String assetKey, AuthRequestPermisionType permType) throws HttpUnauthorizedException {
		if (!authModel.isActivated()) {
			throw new HttpUnauthorizedException("Credentials are not activated");
		}
		if (authModel.isAdmin()) {
			return;
		}
		switch (permType) {
		case COUNT:
		case READ_ONE:
		case READ:
			if (!authModel.is(DeviceAuthModel.class)) {
				throw new HttpUnauthorizedException("Required to be a activated device.");
			}
			break;
		case CREATE:
		case UPDATE:
		case DELETE:
			throw new HttpUnauthorizedException("Requires admin permissions");
		}
	}
}
