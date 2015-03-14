package com.tpofof.conmon.server.data.elasticsearch;

import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;

import com.pofof.conmon.model.TimerResult;
import com.tpofof.conmon.server.data.SearchResults;

public class TimerResultEsDAO extends GenericElasticsearchDAO<TimerResult> {

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
