package com.tpofof.conmon.server.managers;

import com.tpofof.conmon.server.data.SearchResults;

public interface GenericModelManager<ModelT> {

	public ModelT find(String id);
	public SearchResults<ModelT> find();
	public SearchResults<ModelT> find(int limit, int offset);
	public long count();
	public ModelT insert(ModelT model);
	public ModelT update(ModelT model);
	public boolean delete(String id);
}
