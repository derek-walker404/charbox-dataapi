package co.charbox.dataapi.data.elasticsearch;

import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.domain.model.SstResults;

import com.tpofof.core.data.dao.ResultsSet;
import com.tpofof.core.data.dao.context.SimpleSearchContext;
import com.tpofof.core.data.dao.es.AbstractElasticsearchDAO;
import com.tpofof.core.data.dao.es.EsQuery;
import com.tpofof.core.data.dao.es.EsQuery.EsQueryBuilder;
import com.tpofof.core.io.IO;
import com.tpofof.core.utils.Config;

@Component
public class SstResultEsDAO extends AbstractElasticsearchDAO<SstResults> {

	@Autowired private IO io;
	private String index;
	private String type;

	@Autowired
	public SstResultEsDAO(Config config, Client client) {
		super(config, client);
	}

	@Override
	protected String getIndex() {
		if (index == null) {
			index = getConfig().getString("es.sst_result.index", "charbot_v0.1_sst");
		}
		return index;
	}

	@Override
	protected String getType() {
		if (type == null) {
			type = getConfig().getString("es.sst_result.type", "sst");
		}
		return type;
	}
	
	@Override
	protected Class<SstResults> getModelClass() {
		return SstResults.class;
	}
	
	@Override
	protected boolean isRequiredIndex() {
		return true;
	}
	
	@Override
	protected String getMapping() {
		String filename = getConfig().getString("es.sst_result.mapping.name", "mappings/es.sst_result.mapping.json");
		return io.getContents(filename);
	}
	
	public ResultsSet<SstResults> getByDevice(String deviceId, int limit, int offset) {
		EsQuery q = EsQuery.builder()
				.constraints(QueryBuilders.termQuery("deviceId", deviceId))
				.limit(limit)
				.offset(offset)
				.build();
		return find(q);
	}

	public ResultsSet<SstResults> getByDeviceId(SimpleSearchContext context, String deviceId) {
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
