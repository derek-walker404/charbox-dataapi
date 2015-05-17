package co.charbox.dataapi.data.elasticsearch;

import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.domain.model.PingResults;

import com.tpofof.core.data.dao.es.AbstractElasticsearchDAO;
import com.tpofof.core.io.IO;
import com.tpofof.core.utils.Config;

@Component
public class PingResultDAO extends AbstractElasticsearchDAO<PingResults> {

	private IO io;
	private String index;
	private String type;

	@Autowired
	public PingResultDAO(Config config, Client client, IO io) {
		super(config, client);
		this.io = io;
		init();
	}

	@Override
	protected String getIndex() {
		if (index == null) {
			index = getConfig().getString("es.ping.index", "charbot_v0.1_ping");
		}
		return index;
	}

	@Override
	protected String getType() {
		if (type == null) {
			type = getConfig().getString("es.ping.type", "ping");
		}
		return type;
	}

	@Override
	protected Class<PingResults> getModelClass() {
		return PingResults.class;
	}
	
	@Override
	protected boolean isRequiredIndex() {
		return true;
	}
	
	@Override
	protected String getMapping() {
		String filename = getConfig().getString("es.ping.mapping.name", "mappings/es.ping.mapping.json");
		return io.getContents(filename);
	}
}
