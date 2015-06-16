package co.charbox.dataapi.data.elasticsearch;

import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.domain.model.PingResults;

import com.tpofof.core.data.dao.ResultsSet;
import com.tpofof.core.data.dao.context.SimpleSearchContext;
import com.tpofof.core.data.dao.es.AbstractElasticsearchDAO;
import com.tpofof.core.data.dao.es.EsQuery;
import com.tpofof.core.data.dao.es.EsQuery.EsQueryBuilder;
import com.tpofof.core.io.IO;
import com.tpofof.core.utils.Config;

@Component
public class PingResultDAO extends AbstractElasticsearchDAO<PingResults> {

	@Autowired private IO io;
	private String index;
	private String type;

	@Autowired
	public PingResultDAO(Config config, Client client) {
		super(config, client);
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

	public ResultsSet<PingResults> getByDeviceId(SimpleSearchContext context, String deviceId) {
		EsQueryBuilder q = EsQuery.builder()
				.limit(context.getWindow().getLimit())
				.offset(context.getWindow().getOffset())
				.constraints(QueryBuilders.termQuery("deviceId", deviceId));
		if (context.getSort() != null) {
			q.sort(SortBuilders.fieldSort(context.getSort().getField()).order(context.getSort().getDirection() > 0 ? SortOrder.ASC : SortOrder.DESC));
		}
		return find(q.build());
	}
}
