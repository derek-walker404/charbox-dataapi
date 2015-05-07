package co.charbox.dataapi.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Optional;

import co.charbox.dataapi.managers.DeviceAuthManager;
import co.charbox.domain.model.DeviceAuthModel;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

@Component
public class DeviceAuthenticator implements Authenticator<BasicCredentials, DeviceAuthModel> {

	@Autowired DeviceAuthManager deviceAuthManager;
	
	@Override
	public Optional<DeviceAuthModel> authenticate(BasicCredentials credentials)
			throws AuthenticationException {
		DeviceAuthModel auth = DeviceAuthModel.builder()
				.deviceId(credentials.getUsername())
				.apiKey(credentials.getPassword())
				.build();
		boolean validAuth = deviceAuthManager.checkAuth(auth);
		return validAuth ? Optional.of(auth) : Optional.<DeviceAuthModel>absent();
	}

}
