package co.charbox.dataapi.managers;

import org.springframework.beans.factory.annotation.Autowired;

import com.tpofof.core.data.IPersistentModel;
import com.tpofof.core.data.dao.context.PrincipalSearchContext;
import com.tpofof.core.data.dao.jdbc.AbstractSimpleJooqDAO;
import com.tpofof.core.managers.AbstractJdbcModelManager;

public abstract class CharbotModelManager<ModelT extends IPersistentModel<ModelT, Integer>, ModelDaoT extends AbstractSimpleJooqDAO<ModelT, Integer, PrincipalSearchContext>> 
		extends AbstractJdbcModelManager<ModelT, ModelDaoT, Integer, PrincipalSearchContext, PrincipalSearchContext, PrincipalSearchContext> {

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
	protected void checkCanView(PrincipalSearchContext authContext, ModelT model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void checkCanInsert(PrincipalSearchContext authContext, ModelT model) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void checkCanUpdate(PrincipalSearchContext authContext, ModelT model) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void checkCanDelete(PrincipalSearchContext authContext, ModelT model) {
		// TODO Auto-generated method stub
	}

	@Override
	protected PrincipalSearchContext convert(PrincipalSearchContext context) {
		return context;
	}
}
