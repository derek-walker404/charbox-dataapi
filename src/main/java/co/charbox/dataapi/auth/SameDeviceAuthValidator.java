package co.charbox.dataapi.auth;

import org.springframework.stereotype.Component;

import co.charbox.domain.model.auth.DeviceAuthModel;
import co.charbox.domain.model.auth.IAuthModel;

import com.tpofof.dwa.auth.IAuthValidator;
import com.tpofof.dwa.error.HttpUnauthorizedException;
import com.tpofof.dwa.resources.AuthRequestPermisionType;

@Component
public class SameDeviceAuthValidator implements IAuthValidator<IAuthModel, String, AuthRequestPermisionType> {

	public void validate(IAuthModel authModel, String assetKey, AuthRequestPermisionType permType) throws HttpUnauthorizedException {
		if (!authModel.isActivated()) {
			throw new HttpUnauthorizedException("Credentials are not activated");
		}
		if (authModel.isAdmin()) {
			return;
		}
		if (assetKey == null || !authModel.is(DeviceAuthModel.class) || !assetKey.equals(authModel.to(DeviceAuthModel.class).getDeviceId())) {
			throw new HttpUnauthorizedException("Device id's do not match.");
		}
	}
}
