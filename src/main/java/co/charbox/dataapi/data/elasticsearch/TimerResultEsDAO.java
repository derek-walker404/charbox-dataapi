package co.charbox.dataapi.data.elasticsearch;

import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.domain.model.TimerResult;

import com.tpofof.core.data.dao.ResultsSet;
import com.tpofof.core.data.dao.SearchWindow;
import com.tpofof.core.data.dao.SimpleSort;
import com.tpofof.core.data.dao.es.AbstractElasticsearchDAO;
import com.tpofof.core.data.dao.es.EsQuery;
import com.tpofof.core.io.IO;
import com.tpofof.core.utils.Config;

@Component
public class TimerResultEsDAO extends AbstractElasticsearchDAO<TimerResult> {

	private IO io;
	private String index;
	private String type;

	@Autowired
	public TimerResultEsDAO(Config config, Client client, IO io) {
		super(config, client);
		this.io = io;
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
	protected String getMapping() {
		String filename = getConfig().getString("es.timer_result.mapping.name", "mappings/es.timer_result.mapping.json");
		return io.getContents(filename);
	}
	
	public ResultsSet<TimerResult> getByDevice(String deviceId, SearchWindow window, SimpleSort sort) {
		EsQuery q = EsQuery.builder()
				.constraints(QueryBuilders.termQuery("deviceId", deviceId))
				.limit(window.getLimit())
				.offset(window.getOffset())
				.sort(convertSort(sort))
				.build();
		return find(q);
	}
}
