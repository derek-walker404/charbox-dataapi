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
import com.tpofof.conmon.server.mongo.GenericMongoDAO;
import com.tpofof.conmon.server.resources.ResponseUtils;

public class GenericCrudResource<ModelT, DaoT extends GenericMongoDAO<ModelT>> {

	private final DaoT dao;
	private final Class<ModelT> modelClass;
	
	public GenericCrudResource(DaoT dao, Class<ModelT> modelClass) {
		this.dao = dao;
		this.modelClass = modelClass;
	}
	
	protected final DaoT getDao() {
		return dao;
	}
	
	@GET
	@Timed
	public JsonNode findModels(@QueryParam("limit") Optional<Integer> limit,
			@QueryParam("offset") Optional<Integer> offset) {
		int limitVal = limit.isPresent() && limit.get() > 0 ? limit.get() : 20;
		int offsetVal = offset.isPresent() && offset.get() >= 0 ? offset.get() : 0;
		List<ModelT> models = dao.find(limitVal, offsetVal);
		return ResponseUtils.success(ResponseUtils.listData(models, limitVal, offsetVal));
	}
	
	@Path("/{_id}")
	@GET
	@Timed
	public JsonNode findModel(@PathParam("_id") String id) {
		ModelT model = dao.find(id);
		return model == null ?
				ResponseUtils.failure("Could not find " + modelClass.getSimpleName() + " with id " + id, 404)
				: ResponseUtils.success(ResponseUtils.modelData(model));
	}
	
	@POST
	@Timed
	public JsonNode postTestCase(ModelT model) {
		ModelT insertedModel = dao.insert(model);
		return insertedModel == null ?
				ResponseUtils.failure("Could not create " + modelClass.getSimpleName())
				: ResponseUtils.success(ResponseUtils.modelData(insertedModel)); 
	}
}
