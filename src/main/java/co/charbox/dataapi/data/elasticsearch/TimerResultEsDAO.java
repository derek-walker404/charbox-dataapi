package co.charbox.dataapi.data.elasticsearch;

import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.domain.model.TimerResult;

import com.tpofof.core.data.dao.ResultsSet;
import com.tpofof.core.data.dao.es.AbstractElasticsearchDAO;
import com.tpofof.core.data.dao.es.EsQuery;
import com.tpofof.core.utils.Config;

@Component
public class TimerResultEsDAO extends AbstractElasticsearchDAO<TimerResult> {

	private String index;
	private String type;

	@Autowired
	public TimerResultEsDAO(Config config, Client client) {
		super(config, client);
		init();
	}

	@Override
	protected String getIndex() {
		if (index == null) {
			index = getConfig().getString("es.timer_result.index", "charbot_v0.1_tr");
		}
		return index;
	}

	@Override
	protected String getType() {
		if (type == null) {
			type = getConfig().getString("es.timer_result.type", "tr");
		}
		return type;
	}
	
	@Override
	protected Class<TimerResult> getModelClass() {
		return TimerResult.class;
	}
	
	@Override
	protected boolean isRequiredIndex() {
		return true;
	}
	
	@Override
	protected boolean hasMapping() {
		return false;
	}
	
	public ResultsSet<TimerResult> getByDevice(String deviceId, int limit, int offset) {
		EsQuery q = EsQuery.builder()
				.constraints(QueryBuilders.termQuery("deviceId", deviceId))
				.limit(limit)
				.offset(offset)
				.build();
		return find(q);
	}
}
