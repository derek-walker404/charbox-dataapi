package co.charbox.dataapi.data.elasticsearch;

import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.domain.model.Heartbeat;

import com.tpofof.core.data.dao.ResultsSet;
import com.tpofof.core.data.dao.es.AbstractElasticsearchDAO;
import com.tpofof.core.utils.Config;

@Component
public class HeartbeatDAO extends AbstractElasticsearchDAO<Heartbeat> {

	private String index;
	private String type;
	
	@Autowired
	public HeartbeatDAO(Config config, Client client) {
		super(config, client);
		init();
	}

	@Override
	protected String getIndex() {
		if (index == null) {
			index = getConfig().getString("es.heartbeat.index", "charbot_v0.1_hb");
		}
		return index;
	}

	@Override
	protected String getType() {
		if (type == null) {
			type = getConfig().getString("es.heartbeat.type", "hb");
		}
		return type;
	}

	@Override
	protected Class<Heartbeat> getModelClass() {
		return Heartbeat.class;
	}

	@Override
	protected boolean isRequiredIndex() {
		return true;
	}

	@Override
	protected boolean hasSort() {
		return false;
	}

	@Override
	protected boolean hasMapping() {
		return false;
	}
	
	public Heartbeat findByDeviceId(String deviceId) {
		ResultsSet<Heartbeat> heartbeats = find(QueryBuilders.termQuery("deviceId", deviceId), 10, 0);
		return heartbeats.getResults().size() == 1 ? heartbeats.getResults().get(0) : null;
	}
}
