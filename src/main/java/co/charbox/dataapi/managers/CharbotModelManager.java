package co.charbox.dataapi.managers;

import com.tpofof.core.data.IPersistentModel;
import com.tpofof.core.data.dao.context.SimpleSearchContext;
import com.tpofof.core.data.dao.es.AbstractElasticsearchDAO;
import com.tpofof.core.managers.AbstractEsModelManager;

public abstract class CharbotModelManager<ModelT extends IPersistentModel<ModelT, String>, ModelDaoT extends AbstractElasticsearchDAO<ModelT>> 
		extends AbstractEsModelManager<ModelT, ModelDaoT> {

	public CharbotModelManager(ModelDaoT dao) {
		super(dao);
	}

	@Override
	protected void checkCanView(SimpleSearchContext authContext, ModelT model) {
	}

	@Override
	protected void checkCanDelete(SimpleSearchContext authContext, ModelT model) {
	}
}
