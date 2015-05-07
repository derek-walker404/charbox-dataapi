package co.charbox.dataapi.auth;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.domain.model.auth.AdminAuthModel;
import co.charbox.domain.model.auth.IAuthModel;

import com.google.common.base.Optional;
import com.tpofof.core.utils.Config;

@Component
public class AdminAuthenticator implements Authenticator<BasicCredentials, IAuthModel> {

	private final String username;
	private final String password;
	
	@Autowired
	public AdminAuthenticator(Config config) {
		this.username = config.getString("auth.admin.username", "admin");
		this.password = config.getString("auth.admin.password", "arfarf");
	}
	
	@Override
	public Optional<IAuthModel> authenticate(BasicCredentials credentials) throws AuthenticationException {
		boolean validAuth = this.username.equals(credentials.getUsername()) && this.password.equals(credentials.getPassword());
		IAuthModel iAuth = new AdminAuthModel();
		return validAuth ? Optional.of(iAuth) : Optional.<IAuthModel>absent();
	}
}
