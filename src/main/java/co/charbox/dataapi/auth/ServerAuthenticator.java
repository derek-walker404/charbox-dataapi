package co.charbox.dataapi.auth;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.managers.auth.ServerAuthManager;
import co.charbox.domain.data.CharbotSearchContext;
import co.charbox.domain.model.auth.CharbotAuthModel;
import co.charbox.domain.model.auth.ServerAuthModel;

import com.google.common.base.Optional;

@Component
public class ServerAuthenticator implements Authenticator<BasicCredentials, CharbotAuthModel> {

	@Autowired ServerAuthManager serverAuthManager;
	
	@Override
	public Optional<CharbotAuthModel> authenticate(BasicCredentials credentials)
			throws AuthenticationException {
		String[] names = credentials.getUsername().split("@");
		if (names.length != 2) {
			return Optional.<CharbotAuthModel>absent();
		}
		String serviceId = names[0];
		String service = names[1];
		ServerAuthModel serverAuth = ServerAuthModel.builder()
				.serverId(serviceId)
				.key(credentials.getPassword())
				.serviceName(service)
				.build();
		CharbotAuthModel iAuth = null;
		if (serverAuthManager.isValid(CharbotSearchContext.getSystemContext(), serverAuth)) {
			iAuth = serverAuthManager.find(serverAuth);
		}
		return iAuth != null ? Optional.of(iAuth) : Optional.<CharbotAuthModel>absent();
	}
}
