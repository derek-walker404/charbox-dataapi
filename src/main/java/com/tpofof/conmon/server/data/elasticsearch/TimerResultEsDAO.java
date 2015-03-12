package com.tpofof.conmon.server.data.elasticsearch;

import java.util.List;

import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;

import com.pofof.conmon.model.TimerResult;

public class TimerResultEsDAO extends GenericElasticsearchDAO<TimerResult> {

	public TimerResultEsDAO(Client client) {
		super(client, TimerResult.class);
	}

	@Override
	protected String getType() {
		return "timerResult";
	}
	
	public List<TimerResult> getByDevice(long deviceId, int limit, int offset) {
		return find(QueryBuilders.termQuery("deviceId", deviceId), limit, offset);
	}
}
