package com.tpofof.conmon.server.data.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.pofof.conmon.model.TimerResult;

public class TimerResultDAO extends GenericMongoDAO<TimerResult> {

	public TimerResultDAO(DBCollection collection) {
		super(collection, TimerResult.class);
	}

	@Override
	protected boolean hasSort() {
		return true;
	}

	@Override
	protected DBObject getSort() {
		return new BasicDBObject("startTime", -1);
	}
}
