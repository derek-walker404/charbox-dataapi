package co.charbox.dataapi.managers;

import co.charbox.core.data.GenericDAO;
import co.charbox.core.data.PersistentModel;
import co.charbox.core.data.SearchResults;

public abstract class AbstractModelManager<ModelT extends PersistentModel<ModelT>, ModelDaoT extends GenericDAO<ModelT>> implements GenericModelManager<ModelT> {

	private final ModelDaoT dao;
	
	public AbstractModelManager(ModelDaoT dao) {
		this.dao = dao;
	}
	
	protected ModelDaoT getDao() {
		return dao;
	}
	
	public ModelT find(String id) {
		return dao.find(id);
	}
	
	public abstract int getDefualtLimit();

	public SearchResults<ModelT> find() {
		return find(getDefualtLimit(), 0);
	}

	public SearchResults<ModelT> find(int limit, int offset) {
		return dao.find(limit <= 0 ? getDefualtLimit() : limit, offset);
	}

	public long count() {
		return dao.count();
	}

	public ModelT insert(ModelT model) {
		return dao.insert(model);
	}

	public ModelT update(ModelT model) {
		if (model.get_id() == null || model.get_id().equals("0")) {
			return this.insert(model);
		}
		return dao.update(model);
	}

	public boolean delete(String id) {
		return dao.delete(id);
	}

}
