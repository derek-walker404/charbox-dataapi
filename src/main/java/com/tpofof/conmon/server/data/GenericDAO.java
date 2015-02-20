package com.tpofof.conmon.server.data;

import java.util.List;

public interface GenericDAO<ModelT> {

	public List<ModelT> find(int limit, int offset);
	
	public long count();
	
	public ModelT find(String id);
	
	public ModelT insert(ModelT model);
	
	public ModelT update(ModelT model);
	
	public boolean delete(String id);
}