package com.tpofof.conmon.server.data.elasticsearch;

import java.util.List;

import org.bson.types.ObjectId;
import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.SearchHit;

import com.google.common.collect.Lists;
import com.pofof.conmon.model.PersistentModel;
import com.tpofof.conmon.server.data.GenericDAO;
import com.tpofof.utils.Config;
import com.tpofof.utils.JsonUtils;

public abstract class GenericElasticsearchDAO<ModelT extends PersistentModel<ModelT>> implements GenericDAO<ModelT> {

	private final Client client;
	private final Class<ModelT> modelClass;
	
	public GenericElasticsearchDAO(Client client, Class<ModelT> modelClass) {
		this.client = client;
		this.modelClass = modelClass;
	}

	protected String getIndex() {
		return Config.get().getString("es.index.name");
	}
	
	protected abstract String getType();
	
	public ModelT  insert(ModelT model) {
		if (model.get_id() == null || model.get_id().isEmpty()) {
			model.set_id(new ObjectId().toString());
		}
		String jsonSource = convert(model);
		IndexResponse response = client.prepareIndex(getIndex(), getType(), model.get_id())
			.setSource(jsonSource)
			.execute()
			.actionGet();
		return model.get_id().equals(response.getId()) ? model : null;
	}
	
	public ModelT find(String id) {
		GetResponse response = client.prepareGet(getIndex(), getType(), id)
				.execute()
				.actionGet();
		return convert(response.toString());
	}
	
	public List<ModelT> find(int limit, int offset) {
		SearchResponse response = client.prepareSearch(getIndex())
				.setTypes(getType())
				.execute()
				.actionGet();
		List<ModelT> models = Lists.newArrayList();
		for (SearchHit h : response.getHits()) {
			models.add(convert(h.sourceAsString()));
		}
		return models;
	}
	
	public long count() {
		CountResponse response = client.prepareCount()
				.setIndices(getIndex())
				.setTypes(getType())
				.execute()
				.actionGet();
		return response.getCount();
	}

	/**
	 * NOT SUPPORTED
	 */
	@Deprecated
	public ModelT update(ModelT model) {
		return null;
	}

	/**
	 * NOT SUPPORTED
	 */
	@Deprecated
	public boolean delete(String id) {
		return false;
	}
	
	protected String convert(ModelT model) {
		return JsonUtils.toJson(model);
	}
	
	protected ModelT convert(String jsonContent) {
		return JsonUtils.fromJson(jsonContent, modelClass);
	}
}