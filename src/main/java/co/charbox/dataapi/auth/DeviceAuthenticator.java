package co.charbox.dataapi.auth;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.managers.auth.DeviceAuthManager;

import com.google.common.base.Optional;
import com.tpofof.core.security.IAuthModel;

@Component
public class DeviceAuthenticator implements Authenticator<BasicCredentials, IAuthModel> {

	@Autowired DeviceAuthManager deviceAuthManager;
	
	@Override
	public Optional<IAuthModel> authenticate(BasicCredentials credentials)
			throws AuthenticationException {
		int deviceId = -1;
		try {
			deviceId = Integer.parseInt(credentials.getUsername());
		} catch (NumberFormatException e) {
			return Optional.<IAuthModel>absent();
		}
		IAuthModel iAuth = deviceAuthManager.isValid(deviceId, credentials.getPassword());
		return iAuth != null ? Optional.of(iAuth) : Optional.<IAuthModel>absent();
	}

}
