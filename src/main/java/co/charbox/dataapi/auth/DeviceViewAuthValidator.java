package co.charbox.dataapi.auth;

import org.springframework.stereotype.Component;

import co.charbox.domain.model.auth.DeviceAuthModel;
import co.charbox.domain.model.auth.IAuthModel;

import com.tpofof.dwa.auth.IAuthValidator;
import com.tpofof.dwa.error.HttpUnauthorizedException;
import com.tpofof.dwa.resources.AuthRequestPermisionType;

@Component
public class DeviceViewAuthValidator implements IAuthValidator<IAuthModel, String, AuthRequestPermisionType> {

	public void validate(IAuthModel authModel, String assetKey, AuthRequestPermisionType permType) throws HttpUnauthorizedException {
		if (!authModel.isActivated()) {
			throw new HttpUnauthorizedException("Credentials are not activated");
		}
		if (authModel.isAdmin()) {
			return;
		}
		switch (permType) {
		case READ_ONE:
			if (authModel.is(DeviceAuthModel.class)) {
				DeviceAuthModel devAuth = authModel.to(DeviceAuthModel.class);
				if (!assetKey.equals(devAuth.getDeviceId())) {
					throw new HttpUnauthorizedException("Device keys do not match");
				}
			}
			break;
		case COUNT:
		case READ:
		case UPDATE:
		case CREATE:
		case DELETE:
			throw new HttpUnauthorizedException("Requires admin permissions");
		}
	}
}
