package co.charbox.dataapi.managers.auth;

import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.managers.CharbotModelManager;
import co.charbox.domain.data.mysql.auth.TokenAuthDAO;
import co.charbox.domain.model.auth.TokenAuthModel;

import com.tpofof.core.data.dao.ResultsSet;
import com.tpofof.core.data.dao.context.PrincipalSearchContext;

@Slf4j
@Component
public class TokenAuthManager extends CharbotModelManager<TokenAuthModel, TokenAuthDAO> {
	
	@Autowired
	public TokenAuthManager(TokenAuthDAO dao) {
		super(dao);
	}

	@Override
	public int getDefualtLimit() {
		return 10;
	}

	@Override
	protected boolean hasDefaultSort() {
		return false;
	}
	
	public TokenAuthModel getNewToken(String serviceName, Integer assetId) {
		// TODO: validate service Id. e.g. sst
		DateTime expiration = new DateTime().plusMillis(10 * 60 * 1000); // TODO move to config
		return getDao().insert(TokenAuthModel.builder()
				.token(UUID.randomUUID().toString())
				.authAssetId(assetId)
				.serviceName(serviceName)
				.expiration(expiration)
				.build());
	}
	
	public TokenAuthModel isValid(String serviceName, Integer authAssetId, String token) {
		TokenAuthModel auth = TokenAuthModel.builder()
				.token(token)
				.authAssetId(authAssetId)
				.serviceName(serviceName)
				.build();
		TokenAuthModel tokenAuth = find(auth);
		boolean tokenFound = tokenAuth != null;
		boolean validAuth = false;
		if (!tokenFound) {
			log.debug("token not found: " + authAssetId + "@" + serviceName + ":" + token);
		} else {
			validAuth = tokenAuth.isValid(authAssetId, serviceName);
			if (!validAuth) {
				log.debug("invalid auth: " + authAssetId + "@" + serviceName + ":" + token);
			}
		}
		return validAuth ? tokenAuth : null;
	}
	
	public TokenAuthModel find(TokenAuthModel auth) {
		return auth != null ? getDao().find(auth) : null;
	}
	
	public boolean deleteByToken(String token) {
		TokenAuthModel auth = getDao().findByToken(token);
		return auth == null ? false : getDao().delete(auth.getId());
	}
	
	public ResultsSet<TokenAuthModel> findExprired(PrincipalSearchContext context) {
		return getDao().findExpired(context);
	}
	
	public int deleteExpired() {
		int count = 0;
		PrincipalSearchContext defaultContext = PrincipalSearchContext.builder()
				.window(getDefualtWindow())
				.build();
		ResultsSet<TokenAuthModel> expired = findExprired(defaultContext);
		while (expired.getResults().size() > 0) {
			for (TokenAuthModel auth : expired.getResults()) {
				if (delete(null, auth.getId())) { // TODO: pass in context or call internal method
					count++;
				}
			}
			expired = findExprired(defaultContext);
		} 
		return count;
	}
}
