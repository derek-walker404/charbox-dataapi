package co.charbox.dataapi.auth;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.managers.auth.TokenAuthManager;

import com.google.common.base.Optional;
import com.tpofof.core.security.IAuthModel;

@Slf4j
@Component
public class TokenAuthenticator implements Authenticator<BasicCredentials, IAuthModel> {
	
	@Autowired private TokenAuthManager tokenAuthManager;
	
	@Override
	public Optional<IAuthModel> authenticate(BasicCredentials credentials)
			throws AuthenticationException {
		String[] keys = credentials.getUsername().split("@");
		if (keys.length != 2) {
			log.debug("invalid token credentials: " + credentials.getUsername() + ":" + credentials.getPassword());
			return Optional.<IAuthModel>absent();
		}
		Integer deviceId = -1;
		try {
			deviceId = Integer.parseInt(keys[0]);
		} catch (NumberFormatException e) {
			return Optional.<IAuthModel>absent();
		}
		String service = keys[1];
		IAuthModel iAuth = tokenAuthManager.isValid(service, deviceId, credentials.getPassword());
		if (iAuth == null) {
			log.debug("token not valid: " + credentials.getUsername() + ":" + credentials.getPassword());
			return Optional.<IAuthModel>absent();
		}
		return Optional.of(iAuth);
	}
}
