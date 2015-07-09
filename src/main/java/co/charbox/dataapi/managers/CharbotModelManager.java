package co.charbox.dataapi.managers;

import com.tpofof.core.data.IPersistentModel;
import com.tpofof.core.data.dao.context.SimpleSearchContext;
import com.tpofof.core.data.dao.jdbc.AbstractSimpleJooqDAO;
import com.tpofof.core.managers.AbstractJdbcModelManager;

public abstract class CharbotModelManager<ModelT extends IPersistentModel<ModelT, Integer>, ModelDaoT extends AbstractSimpleJooqDAO<ModelT, Integer, SimpleSearchContext>> 
		extends AbstractJdbcModelManager<ModelT, ModelDaoT, Integer, SimpleSearchContext, SimpleSearchContext, SimpleSearchContext> {

	private CharbotModelManagerProvider manProvider;
	
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
	protected void checkCanView(SimpleSearchContext authContext, ModelT model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void checkCanInsert(SimpleSearchContext authContext, ModelT model) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void checkCanUpdate(SimpleSearchContext authContext, ModelT model) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void checkCanDelete(SimpleSearchContext authContext, ModelT model) {
		// TODO Auto-generated method stub
	}

	@Override
	protected SimpleSearchContext convert(SimpleSearchContext context) {
		return context;
	}
}
