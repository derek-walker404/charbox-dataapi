package co.charbox.dataapi.data.elasticsearch;

import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.domain.model.Outage;

import com.tpofof.core.data.dao.ResultsSet;
import com.tpofof.core.data.dao.context.SearchWindow;
import com.tpofof.core.data.dao.context.SimpleSearchContext;
import com.tpofof.core.data.dao.es.AbstractElasticsearchDAO;
import com.tpofof.core.data.dao.es.EsQuery;
import com.tpofof.core.data.dao.es.EsQuery.EsQueryBuilder;
import com.tpofof.core.io.IO;
import com.tpofof.core.utils.Config;

@Component
public class OutageDAO extends AbstractElasticsearchDAO<Outage> {

	@Autowired private IO io;
	private String index;
	private String type;

	@Autowired
	public OutageDAO(Config config, Client client) {
		super(config, client);
	}

	@Override
	protected String getIndex() {
		if (index == null) {
			index = getConfig().getString("es.outage.index", "charbot_v0.1_outage");
		}
		return index;
	}

	@Override
	protected String getType() {
		if (type == null) {
			type = getConfig().getString("es.outage.type", "outage");
		}
		return type;
	}

	@Override
	protected Class<Outage> getModelClass() {
		return Outage.class;
	}
	
	@Override
	protected boolean isRequiredIndex() {
		return true;
	}
	
	@Override
	protected String getMapping() {
		String filename = getConfig().getString("es.outage.mapping.name", "mappings/es.outage.mapping.json");
		return io.getContents(filename);
	}
	
	public ResultsSet<Outage> getRecent(DateTime startTime, SearchWindow window) {
		return getRecent(null, startTime, window);
	}
	
	public ResultsSet<Outage> getRecent(String deviceId, DateTime startTime, SearchWindow window) {
		QueryBuilder constraints = QueryBuilders.termQuery("outageTime", startTime);
		if (deviceId != null) {
			constraints = QueryBuilders.boolQuery().must(constraints).must(QueryBuilders.termQuery("deviceId", deviceId));
		}
		EsQuery q = EsQuery.builder()
				.constraints(constraints)
				.limit(window.getLimit())
				.offset(window.getOffset())
				.build();
		return find(q);
	}

	public ResultsSet<Outage> getOutagesByDeviceId(SimpleSearchContext context, String deviceId) {
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
