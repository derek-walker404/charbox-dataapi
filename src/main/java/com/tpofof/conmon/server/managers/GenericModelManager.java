package com.tpofof.conmon.server.managers;

import java.util.List;

public interface GenericModelManager<ModelT> {

	public ModelT find(String id);
	public List<ModelT> find();
	public List<ModelT> find(int limit, int offset);
	public ModelT insert(ModelT model);
	public ModelT update(ModelT model);
	public boolean delete(String id);
}
