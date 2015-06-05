package co.charbox.dataapi.data.elasticsearch;

import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.domain.model.Heartbeat;

import com.tpofof.core.data.dao.ResultsSet;
import com.tpofof.core.data.dao.es.AbstractElasticsearchDAO;
import com.tpofof.core.data.dao.es.EsQuery;
import com.tpofof.core.io.IO;
import com.tpofof.core.utils.Config;

@Component
public class HeartbeatDAO extends AbstractElasticsearchDAO<Heartbeat> {

	private IO io;
	private String index;
	private String type;
	
	@Autowired
	public HeartbeatDAO(Config config, Client client, IO io) {
		super(config, client);
		this.io = io;
		init(config.getBoolean("es.deleteAll", false));
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
	protected String getMapping() {
		String filename = getConfig().getString("es.heartbeat.mapping.name", "mappings/es.heartbeat.mapping.json");
		return io.getContents(filename);
	}
	
	public Heartbeat findByDeviceId(String deviceId) {
		EsQuery q = EsQuery.builder()
				.constraints(QueryBuilders.termQuery("deviceId", deviceId))
				.limit(10)
				.offset(0)
				.build();
		ResultsSet<Heartbeat> heartbeats = find(q);
		return heartbeats.getTotal() == 1 ? heartbeats.getResults().get(0) : null;
	}
}
