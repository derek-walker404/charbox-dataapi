package co.charbox.dataapi.data.elasticsearch;

import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;

import co.charbox.core.data.SearchResults;
import co.charbox.core.data.es.AbstractElasticsearchDAO;
import co.charbox.domain.model.TimerResult;

public class TimerResultEsDAO extends AbstractElasticsearchDAO<TimerResult> {

	public TimerResultEsDAO(Client client) {
		super(client, TimerResult.class);
	}

	@Override
	protected String getType() {
		return "timerResult";
	}
	
	public SearchResults<TimerResult> getByDevice(long deviceId, int limit, int offset) {
		return find(QueryBuilders.termQuery("deviceId", deviceId), limit, offset);
	}
}
