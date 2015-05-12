package co.charbox.dataapi.auth;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.managers.auth.ServerAuthManager;
import co.charbox.domain.model.auth.IAuthModel;

import com.google.common.base.Optional;

@Component
public class ServerAuthenticator implements Authenticator<BasicCredentials, IAuthModel> {

	@Autowired ServerAuthManager serverAuthManager;
	
	@Override
	public Optional<IAuthModel> authenticate(BasicCredentials credentials)
			throws AuthenticationException {
		IAuthModel iAuth = serverAuthManager.isValid(credentials.getUsername(), credentials.getPassword());
		return iAuth != null ? Optional.of(iAuth) : Optional.<IAuthModel>absent();
	}
}
