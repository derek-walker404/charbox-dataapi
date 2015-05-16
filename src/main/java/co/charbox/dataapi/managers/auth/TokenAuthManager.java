package co.charbox.dataapi.managers.auth;

import java.util.UUID;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.data.elasticsearch.auth.TokenAuthDAO;
import co.charbox.domain.model.auth.TokenAuthModel;

import com.tpofof.core.data.dao.ResultsSet;
import com.tpofof.core.data.dao.SearchWindow;
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
	
	@Override
	protected boolean hasDefaultSort() {
		return false;
	}
	
	public TokenAuthModel getNewToken(String serviceId, String deviceId) {
		// TODO: validate service Id. e.g. sst
		DateTime expiration = new DateTime().plusMillis(10 * 60 * 1000); // TODO move to config
		return getDao().insert(TokenAuthModel.builder()
				.token(UUID.randomUUID().toString())
				.authAssetId(deviceId)
				.serviceId(serviceId)
				.expiration(expiration)
				.build());
	}
	
	public TokenAuthModel isValid(String serviceId, String authAssetId, String token) {
		TokenAuthModel auth = TokenAuthModel.builder()
				.token(token)
				.authAssetId(authAssetId)
				.serviceId(serviceId)
				.build();
		TokenAuthModel tokenAuth = find(auth);
		boolean validAuth = tokenAuth != null && tokenAuth.isValid(authAssetId, serviceId);
		return validAuth ? tokenAuth : null;
	}
	
	public TokenAuthModel find(TokenAuthModel auth) {
		return auth != null ? getDao().find(auth) : null;
	}
	
	public boolean deleteByToken(String token) {
		TokenAuthModel auth = getDao().findByToken(token);
		return auth == null ? false : getDao().delete(auth.getId());
	}
	
	public ResultsSet<TokenAuthModel> findExprired(SearchWindow window) {
		return getDao().findExpired(window);
	}
	
	public int deleteExpired() {
		int count = 0;
		ResultsSet<TokenAuthModel> expired = findExprired(getDefualtWindow());
		while (expired.getResults().size() > 0) {
			for (TokenAuthModel auth : expired.getResults()) {
				if (delete(auth.getId())) {
					count++;
				}
			}
			expired = findExprired(getDefualtWindow());
		} 
		return count;
	}
}