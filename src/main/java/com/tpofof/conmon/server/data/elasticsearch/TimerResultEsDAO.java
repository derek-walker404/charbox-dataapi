package com.tpofof.conmon.server.data.elasticsearch;

import org.elasticsearch.client.Client;

import com.pofof.conmon.model.TimerResult;

public class TimerResultEsDAO extends GenericElasticsearchDAO<TimerResult> {

	public TimerResultEsDAO(Client client) {
		super(client, TimerResult.class);
	}

	@Override
	protected String getType() {
		return "timerResult";
	}
}
