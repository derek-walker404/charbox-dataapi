package co.charbox.dataapi.auth;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.managers.DeviceAuthManager;
import co.charbox.domain.model.auth.IAuthModel;

import com.google.common.base.Optional;

@Component
public class DeviceAuthenticator implements Authenticator<BasicCredentials, IAuthModel> {

	@Autowired DeviceAuthManager deviceAuthManager;
	
	@Override
	public Optional<IAuthModel> authenticate(BasicCredentials credentials)
			throws AuthenticationException {
		IAuthModel iAuth = deviceAuthManager.isValid(credentials.getUsername(), credentials.getPassword());
		return iAuth != null ? Optional.of(iAuth) : Optional.<IAuthModel>absent();
	}

}
