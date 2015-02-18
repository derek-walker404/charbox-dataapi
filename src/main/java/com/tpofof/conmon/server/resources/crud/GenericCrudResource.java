package com.tpofof.conmon.server.resources.crud;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Optional;
import com.tpofof.conmon.server.managers.GenericModelManager;
import com.tpofof.conmon.server.resources.ResponseUtils;

public class GenericCrudResource<ModelT, ManagerT extends GenericModelManager<ModelT>> {

	private final ManagerT man;
	private final Class<ModelT> modelClass;
	
	public GenericCrudResource(ManagerT man, Class<ModelT> modelClass) {
		this.man = man;
		this.modelClass = modelClass;
	}
	
	protected final ManagerT getManager() {
		return man;
	}
	
	@GET
	@Timed
	public JsonNode findModels(@QueryParam("limit") Optional<Integer> limit,
			@QueryParam("offset") Optional<Integer> offset) {
		int limitVal = limit.isPresent() && limit.get() > 0 ? limit.get() : 20;
		int offsetVal = offset.isPresent() && offset.get() >= 0 ? offset.get() : 0;
		List<ModelT> models = man.find(limitVal, offsetVal);
		return ResponseUtils.success(ResponseUtils.listData(models, limitVal, offsetVal));
	}
	
	@Path("/{_id}")
	@GET
	@Timed
	public JsonNode findModel(@PathParam("_id") String id) {
		ModelT model = man.find(id);
		return model == null ?
				ResponseUtils.failure("Could not find " + modelClass.getSimpleName() + " with id " + id, 404)
				: ResponseUtils.success(ResponseUtils.modelData(model));
	}
	
	@POST
	@Timed
	public JsonNode postTestCase(ModelT model) {
		ModelT insertedModel = man.insert(model);
		return insertedModel == null ?
				ResponseUtils.failure("Could not create " + modelClass.getSimpleName())
				: ResponseUtils.success(ResponseUtils.modelData(insertedModel)); 
	}
}
