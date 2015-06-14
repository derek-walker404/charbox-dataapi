package co.charbox.dataapi.data.elasticsearch.auth;

import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.domain.model.auth.ServerAuthModel;

import com.tpofof.core.data.dao.ResultsSet;
import com.tpofof.core.data.dao.es.AbstractElasticsearchDAO;
import com.tpofof.core.data.dao.es.EsQuery;
import com.tpofof.core.io.IO;
import com.tpofof.core.utils.Config;

@Component
public class ServerAuthDAO extends AbstractElasticsearchDAO<ServerAuthModel> {

	@Autowired private IO io;
	private String index;
	private String type;
	
	@Autowired
	public ServerAuthDAO(Config config, Client client) {
		super(config, client);
	}
	
	@Override
	protected String getIndex() {
		if (index == null) {
			index = getConfig().getString("es.server_auth.index", "charbot_v0.1_serverauth");
		}
		return index;
	}

	@Override
	protected String getType() {
		if (type == null) {
			type = getConfig().getString("es.server_auth.type", "serauth");
		}
		return type;
	}

	@Override
	protected Class<ServerAuthModel> getModelClass() {
		return ServerAuthModel.class;
	}
	
	@Override
	protected String getMapping() {
		String filename = getConfig().getString("es.server_auth.mapping.name", "mappings/es.server_auth.mapping.json");
		return io.getContents(filename);
	}

	@Override
	protected boolean isRequiredIndex() {
		return true;
	}

	// TODO: shouldCreate() boolean call
	
	public ServerAuthModel find(ServerAuthModel auth) {
		QueryBuilder q = QueryBuilders.boolQuery()
				.must(QueryBuilders.termQuery("serverId", auth.getServerId()))
				.must(QueryBuilders.termQuery("serverKey", auth.getServerKey()))
				.must(QueryBuilders.termQuery("service", auth.getService()));
		EsQuery esQuery = EsQuery.builder()
				.constraints(q)
				.limit(1)
				.offset(0)
				.build();
		ResultsSet<ServerAuthModel> authResults = find(esQuery);
		return authResults.getTotal() == 1 ? authResults.getResults().get(0) : null;
	}
}
