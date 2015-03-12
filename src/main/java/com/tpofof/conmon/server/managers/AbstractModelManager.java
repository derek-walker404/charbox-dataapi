package com.tpofof.conmon.server.managers;

import java.util.List;

import com.pofof.conmon.model.PersistentModel;
import com.tpofof.conmon.server.data.GenericDAO;

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

	public List<ModelT> find() {
		return find(getDefualtLimit(), 0);
	}

	public List<ModelT> find(int limit, int offset) {
		return dao.find(limit, offset);
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
