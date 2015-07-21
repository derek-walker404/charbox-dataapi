package co.charbox.dataapi.managers;

import java.util.Set;

import jersey.repackaged.com.google.common.collect.Sets;

import org.springframework.beans.factory.annotation.Autowired;

import co.charbox.domain.data.CharbotSearchContext;
import co.charbox.domain.data.mysql.CharbotJooqDao;
import co.charbox.domain.model.RoleModel;

import com.tpofof.core.data.IPersistentModel;
import com.tpofof.core.managers.AbstractJdbcModelManager;
import com.tpofof.dwa.error.HttpUnauthorizedException;

public abstract class CharbotModelManager<ModelT extends IPersistentModel<ModelT, Integer>, ModelDaoT extends CharbotJooqDao<ModelT>> 
		extends AbstractJdbcModelManager<ModelT, ModelDaoT, Integer, CharbotSearchContext, CharbotSearchContext, CharbotSearchContext> {

	@Autowired private CharbotModelManagerProvider manProvider;
	
	public CharbotModelManager(ModelDaoT dao) {
		super(dao);
	}
	
	protected CharbotModelManagerProvider getManProvider() {
		return manProvider;
	}

	@Override
	public Integer getDefaultId() {
		return -1;
	}
	
	@Override
	protected void checkCanView(CharbotSearchContext authContext, ModelT model) {
		check(authContext, Sets.newHashSet(RoleModel.getAdminRole()), "Principal does not have permissions to view asset.");
	}
	
	@Override
	protected void checkCanViewCollection(CharbotSearchContext authContext) {
		check(authContext, Sets.newHashSet(RoleModel.getAdminRole()), "Principal does not have permissions to view asset.");
	}

	@Override
	protected void checkCanInsert(CharbotSearchContext authContext, ModelT model) {
		check(authContext, Sets.newHashSet(RoleModel.getAdminRole()), "Principal does not have permissions to insert asset.");
	}

	@Override
	protected void checkCanUpdate(CharbotSearchContext authContext, ModelT model) {
		check(authContext, Sets.newHashSet(RoleModel.getAdminRole()), "Principal does not have permissions to update asset.");
	}

	@Override
	protected void checkCanDelete(CharbotSearchContext authContext, ModelT model) {
		check(authContext, Sets.newHashSet(RoleModel.getAdminRole()), "Principal does not have permissions to delete asset.");
	}
	
	protected void check(CharbotSearchContext authContext, Set<RoleModel> expectedRoles) {
		check(authContext, expectedRoles, "Principal does not have permissions to perform action.");
	}
	
	protected void check(CharbotSearchContext authContext, Set<RoleModel> expectedRoles, String errorMessage) {
		if (authContext == null || authContext.getPrincipal() == null) {
			throw new HttpUnauthorizedException("Not authorized.");
		} else {
			Set<RoleModel> principalRoles = authContext.getPrincipal().getRoles();
			for (RoleModel expected : expectedRoles) {
				if (principalRoles.contains(expected)) {
					return;
				}
			}
			throw new HttpUnauthorizedException(errorMessage);
		}
	}

	@Override
	protected CharbotSearchContext convert(CharbotSearchContext context) {
		return context;
	}
}
