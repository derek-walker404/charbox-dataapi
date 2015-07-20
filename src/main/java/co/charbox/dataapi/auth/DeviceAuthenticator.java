package co.charbox.dataapi.auth;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.managers.auth.DeviceAuthManager;
import co.charbox.domain.model.auth.CharbotAuthModel;

import com.google.common.base.Optional;

@Component
public class DeviceAuthenticator implements Authenticator<BasicCredentials, CharbotAuthModel> {

	@Autowired DeviceAuthManager deviceAuthManager;
	
	@Override
	public Optional<CharbotAuthModel> authenticate(BasicCredentials credentials)
			throws AuthenticationException {
		int deviceId = -1;
		try {
			deviceId = Integer.parseInt(credentials.getUsername());
		} catch (NumberFormatException e) {
			return Optional.<CharbotAuthModel>absent();
		}
		CharbotAuthModel iAuth = deviceAuthManager.isValid(deviceId, credentials.getPassword());
		return iAuth != null ? Optional.of(iAuth) : Optional.<CharbotAuthModel>absent();
	}

}
