package co.charbox.dataapi.managers.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.data.elasticsearch.auth.TokenAuthDAO;
import co.charbox.domain.model.auth.TokenAuthModel;

import com.tpofof.core.managers.AbstractEsModelManager;

@Component
public class TokenAuthManager extends AbstractEsModelManager<TokenAuthModel, TokenAuthDAO> {

	@Autowired
	public TokenAuthManager(TokenAuthDAO dao) {
		super(dao);
	}

	@Override
	public int getDefualtLimit() {
		return 10;
	}

	@Override
	public String getDefaultId() {
		return "";
	}
	
	public TokenAuthModel isValid(String authAssetId, String token) {
		TokenAuthModel auth = TokenAuthModel.builder()
				.token(token)
				.authAssetId(authAssetId)
				.build();
		TokenAuthModel sstTokenAuth = find(auth);
		boolean validAuth = sstTokenAuth != null && sstTokenAuth.isValid(authAssetId);
		return validAuth ? sstTokenAuth : null;
	}
	
	public TokenAuthModel find(TokenAuthModel auth) {
		return auth != null ? getDao().find(auth) : null;
	}

	@Override
	protected boolean hasDefualtSort() {
		return false;
	}
}
