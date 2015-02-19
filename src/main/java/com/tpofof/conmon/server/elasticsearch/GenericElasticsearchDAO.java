package com.tpofof.conmon.server.elasticsearch;

import java.util.List;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.SearchHit;

import com.google.common.collect.Lists;
import com.pofof.conmon.model.PersistentModel;
import com.tpofof.utils.Config;
import com.tpofof.utils.JsonUtils;

public abstract class GenericElasticsearchDAO<ModelT extends PersistentModel> {

	private final Client client;
	private final Class<ModelT> modelClass;
	
	public GenericElasticsearchDAO(Client client, Class<ModelT> modelClass) {
//		this.client = new TransportClient()
//	        .addTransportAddress(new InetSocketTransportAddress("localhost", 9300));
		this.client = client;
		this.modelClass = modelClass;
	}

	protected String getIndex() {
		return Config.get().getString("es.index.name");
	}
	
	protected abstract String getType();
	
	public ModelT  insert(ModelT model) {
		IndexResponse response = client.prepareIndex(getIndex(), getType(), model.get_id())
			.setSource(JsonUtils.toJson(model))
			.execute()
			.actionGet();
		return model.get_id().equals(response.getId()) ? model : null;
	}
	
	public ModelT find(String id) {
		GetResponse response = client.prepareGet(getIndex(), getType(), id)
				.execute()
				.actionGet();
		return JsonUtils.fromJson(response.toString(), modelClass);
	}
	
	public List<ModelT> find(int limit, int offset) {
		SearchResponse response = client.prepareSearch(getIndex())
				.execute()
				.actionGet();
		List<ModelT> models = Lists.newArrayList();
		for (SearchHit h : response.getHits()) {
			models.add(JsonUtils.fromJson(h.sourceAsString(), modelClass));
		}
		return models;
	}
}
