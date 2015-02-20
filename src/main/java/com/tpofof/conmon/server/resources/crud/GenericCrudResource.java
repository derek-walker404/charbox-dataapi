package com.tpofof.conmon.server.resources.crud;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Optional;
import com.pofof.conmon.model.PersistentModel;
import com.tpofof.conmon.server.managers.GenericModelManager;
import com.tpofof.conmon.server.resources.ResponseUtils;
import com.tpofof.utils.JsonUtils;

public class GenericCrudResource<ModelT extends PersistentModel, ManagerT extends GenericModelManager<ModelT>> {

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
			@QueryParam("offset") Optional<Integer> offset,
			@Context HttpServletRequest request) {
		int limitVal = limit.isPresent() && limit.get() > 0 ? limit.get() : 20;
		int offsetVal = offset.isPresent() && offset.get() >= 0 ? offset.get() : 0;
		List<ModelT> models = man.find(limitVal, offsetVal);
		return ResponseUtils.success(ResponseUtils.listData(models, limitVal, offsetVal));
	}
	
	@Path("/count")
	@GET
	@Timed
	public JsonNode count(@Context HttpServletRequest request) {
		long count = man.count();
		return count >= 0
				? ResponseUtils.success(JsonUtils.getObjectNode().put("count", count))
				: ResponseUtils.failure("Could not retrieve count for " + modelClass.getSimpleName(), 500);
	}
	
	@Path("/{_id}")
	@GET
	@Timed
	public JsonNode findModel(@PathParam("_id") String id,
			@Context HttpServletRequest request) {
		ModelT model = man.find(id);
		return model == null ?
				ResponseUtils.failure("Could not find " + modelClass.getSimpleName() + " with id " + id, 404)
				: ResponseUtils.success(ResponseUtils.modelData(model));
	}
	
	@POST
	@Timed
	public JsonNode post(ModelT model,
			@Context HttpServletRequest request) {
		ModelT insertedModel = man.insert(model);
		return insertedModel == null ?
				ResponseUtils.failure("Could not create " + modelClass.getSimpleName())
				: ResponseUtils.success(ResponseUtils.modelData(insertedModel)); 
	}
	
	@Path("/{_id}")
	@PUT
	@Timed
	public JsonNode update(@PathParam("_id") String id, ModelT model,
			@Context HttpServletRequest request) {
		if (!id.equals(model.get_id())) {
			return ResponseUtils.failure("Invalid Request: ID's do not match", 400);
		}
		ModelT updatedModel = man.update(model);
		return updatedModel != null
				? ResponseUtils.success(ResponseUtils.modelData(updatedModel))
				: ResponseUtils.failure("Could not update the " + modelClass.getSimpleName(), 500);
	}
	
	@Path("/{_id}")
	@DELETE
	@Timed
	public JsonNode delete(@PathParam("_id") String id,
			@Context HttpServletRequest request) {
		boolean deleteSuccess = man.delete(id);
		return deleteSuccess
				? ResponseUtils.success(null)
				: ResponseUtils.failure("Failed to delete " + modelClass.getSimpleName() + " with id " + id, 500);
	}
}
