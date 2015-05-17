package co.charbox.dataapi.auth;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.managers.auth.TokenAuthManager;

import com.google.common.base.Optional;
import com.tpofof.core.security.IAuthModel;

@Component
public class TokenAuthenticator implements Authenticator<BasicCredentials, IAuthModel> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TokenAuthenticator.class);

	@Autowired private TokenAuthManager tokenAuthManager;
	
	@Override
	public Optional<IAuthModel> authenticate(BasicCredentials credentials)
			throws AuthenticationException {
		String[] keys = credentials.getUsername().split("@");
		if (keys.length != 2) {
			LOGGER.debug("invalid token credentials: " + credentials.getUsername() + ":" + credentials.getPassword());
			return Optional.<IAuthModel>absent();
		}
		String deviceId = keys[0];
		String service = keys[1];
		IAuthModel iAuth = tokenAuthManager.isValid(service, deviceId, credentials.getPassword());
		if (iAuth == null) {
			LOGGER.debug("token not valid: " + credentials.getUsername() + ":" + credentials.getPassword());
			return Optional.<IAuthModel>absent();
		}
		return Optional.of(iAuth);
	}
}
