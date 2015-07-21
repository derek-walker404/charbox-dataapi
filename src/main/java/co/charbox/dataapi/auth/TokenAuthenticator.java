package co.charbox.dataapi.auth;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.managers.auth.TokenAuthManager;
import co.charbox.domain.data.CharbotSearchContext;
import co.charbox.domain.model.auth.CharbotAuthModel;
import co.charbox.domain.model.auth.TokenAuthModel;

import com.google.common.base.Optional;

@Slf4j
@Component
public class TokenAuthenticator implements Authenticator<BasicCredentials, CharbotAuthModel> {
	
	@Autowired private TokenAuthManager tokenAuthManager;
	
	@Override
	public Optional<CharbotAuthModel> authenticate(BasicCredentials credentials)
			throws AuthenticationException {
		String[] keys = credentials.getUsername().split("@");
		if (keys.length != 2) {
			log.debug("invalid token credentials: " + credentials.getUsername() + ":" + credentials.getPassword());
			return Optional.<CharbotAuthModel>absent();
		}
		Integer deviceId = -1;
		try {
			deviceId = Integer.parseInt(keys[0]);
		} catch (NumberFormatException e) {
			return Optional.<CharbotAuthModel>absent();
		}
		String service = keys[1];
		TokenAuthModel tokenAuth = TokenAuthModel.builder()
				.serviceName(service)
				.authAssetId(deviceId)
				.token(credentials.getPassword())
				.build();
		CharbotAuthModel iAuth = null;
		if (tokenAuthManager.isValid(CharbotSearchContext.getSystemContext(), tokenAuth)) {
			iAuth = tokenAuthManager.find(CharbotSearchContext.getSystemContext(), tokenAuth);
		}
		if (iAuth == null) {
			log.debug("token not valid: " + credentials.getUsername() + ":" + credentials.getPassword());
			return Optional.<CharbotAuthModel>absent();
		}
		return Optional.of(iAuth);
	}
}
