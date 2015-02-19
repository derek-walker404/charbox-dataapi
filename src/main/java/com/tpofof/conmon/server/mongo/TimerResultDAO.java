package com.tpofof.conmon.server.mongo;

import com.mongodb.DBCollection;
import com.pofof.conmon.model.TimerResult;

public class TimerResultDAO extends GenericMongoDAO<TimerResult> {

	public TimerResultDAO(DBCollection collection) {
		super(collection, TimerResult.class);
	}

}
