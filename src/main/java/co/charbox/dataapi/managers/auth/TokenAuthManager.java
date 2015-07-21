package co.charbox.dataapi.managers.auth;

import java.util.HashSet;
import java.util.UUID;

import jersey.repackaged.com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.managers.CharbotModelManager;
import co.charbox.domain.data.CharbotSearchContext;
import co.charbox.domain.data.mysql.auth.TokenAuthDAO;
import co.charbox.domain.model.RoleModel;
import co.charbox.domain.model.auth.TokenAuthModel;

import com.tpofof.core.data.dao.ResultsSet;

@Slf4j
@Component
public class TokenAuthManager extends CharbotModelManager<TokenAuthModel, TokenAuthDAO> {
	
	private static final HashSet<RoleModel> SYSTEM_ROLES = Sets.newHashSet(RoleModel.getAdminRole(), RoleModel.getSystemRole());
	
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
	
	public TokenAuthModel getNewToken(CharbotSearchContext context, String serviceName, Integer assetId) {
		check(context, Sets.newHashSet(RoleModel.getAdminRole(), RoleModel.getSystemRole(), RoleModel.getDeviceRole(assetId)));
		// TODO: validate service Id. e.g. sst
		DateTime expiration = new DateTime().plusMillis(10 * 60 * 1000); // TODO move to config
		return getDao().insert(TokenAuthModel.builder()
				.token(UUID.randomUUID().toString())
				.authAssetId(assetId)
				.serviceName(serviceName)
				.expiration(expiration)
				.build());
	}
	
	public boolean isValid(CharbotSearchContext context, TokenAuthModel auth) {
		TokenAuthModel tokenAuth = find(context, auth);
		boolean tokenFound = tokenAuth != null;
		boolean validAuth = false;
		if (!tokenFound) {
			log.debug("token not found: " + tokenAuth.getAuthAssetId() + "@" + tokenAuth.getServiceName() + ":" + tokenAuth.getToken());
		} else {
			validAuth = tokenAuth.isValid(auth.getAuthAssetId(), auth.getServiceName());
			if (!validAuth) {
				log.debug("invalid auth: " + tokenAuth.getAuthAssetId() + "@" + tokenAuth.getServiceName() + ":" + tokenAuth.getToken());
			}
		}
		return validAuth;
	}
	
	public TokenAuthModel find(CharbotSearchContext context, TokenAuthModel auth) {
		check(context, Sets.newHashSet(RoleModel.getAdminRole(), RoleModel.getSystemRole(), RoleModel.getDeviceRole(auth.getAuthAssetId())));
		return auth != null ? getDao().find(auth) : null;
	}
	
	public boolean deleteByToken(CharbotSearchContext context, String token) {
		check(context, SYSTEM_ROLES);
		TokenAuthModel model = getDao().findByToken(token);
		if (model == null) {
			return false;
		}
		return getDao().delete(model.getId());
	}
	
	public ResultsSet<TokenAuthModel> findExprired(CharbotSearchContext context) {
		check(context, SYSTEM_ROLES);
		checkCanViewCollection(context);
		return getDao().findExpired(context);
	}
	
	public int deleteExpired(CharbotSearchContext context) {
		check(context, SYSTEM_ROLES);
		int count = 0;
		context.setWindow(getDefualtWindow());
		ResultsSet<TokenAuthModel> expired = findExprired(context);
		while (expired.getResults().size() > 0) {
			for (TokenAuthModel auth : expired.getResults()) {
				if (delete(null, auth.getId())) { // TODO: pass in context or call internal method
					count++;
				}
			}
			expired = findExprired(context);
		} 
		return count;
	}

	@Override
	protected void checkCanDelete(CharbotSearchContext authContext, TokenAuthModel model) {
		check(authContext, Sets.newHashSet(RoleModel.getAdminRole(), RoleModel.getServiceRole("SST")));
	}
}
