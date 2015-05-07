package co.charbox.dataapi.data.elasticsearch;

import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.domain.model.DeviceAuthModel;

import com.tpofof.core.data.dao.ResultsSet;
import com.tpofof.core.data.dao.es.AbstractElasticsearchDAO;
import com.tpofof.core.io.IO;
import com.tpofof.core.utils.Config;

@Component
public class DeviceAuthDAO extends AbstractElasticsearchDAO<DeviceAuthModel> {

	private IO io;
	private String index;
	private String type;
	
	@Autowired
	public DeviceAuthDAO(Config config, Client client, IO io) {
		super(config, client);
		this.io = io;
		init();
	}
	
	@Override
	protected String getIndex() {
		if (index == null) {
			index = getConfig().getString("es.device_auth.index", "charbot_v0.1_devauth");
		}
		return index;
	}

	@Override
	protected String getType() {
		if (type == null) {
			type = getConfig().getString("es.device_auth.type", "auth");
		}
		return type;
	}

	@Override
	protected Class<DeviceAuthModel> getModelClass() {
		return DeviceAuthModel.class;
	}
	
	@Override
	protected boolean hasSort() {
		return false;
	}
	
	@Override
	protected boolean hasMapping() {
		return true;
	}
	
	@Override
	protected String getMapping() {
		String filename = getConfig().getString("es.device_config.mapping.name", "mappings/es.device_auth.mapping.json");
		return io.getContents(filename);
	}

	@Override
	protected boolean isRequiredIndex() {
		return true;
	}

	// TODO: shouldCreate() boolean call
	
	// TODO: populate on create
	
	public DeviceAuthModel find(DeviceAuthModel auth) {
		QueryBuilder q = QueryBuilders.boolQuery()
				.must(QueryBuilders.termQuery("deviceId", auth.getDeviceId()))
				.must(QueryBuilders.termQuery("apiKey", auth.getApiKey()));
		ResultsSet<DeviceAuthModel> authResults = find(q, 1, 0);
		return authResults.getTotal() == 1 ? authResults.getResults().get(0) : null;
	}
}
