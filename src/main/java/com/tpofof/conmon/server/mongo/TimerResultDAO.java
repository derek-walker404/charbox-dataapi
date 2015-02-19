package com.tpofof.conmon.server.mongo;

import java.util.List;

import com.google.common.collect.Lists;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.pofof.conmon.model.TimerResult;

public class TimerResultDAO extends GenericMongoDAO<TimerResult> {

	public TimerResultDAO(DBCollection collection) {
		super(collection, TimerResult.class);
	}

	@Override
	public List<TimerResult> find(int limit, int offset) {
		DBCursor result = getCollection().find().sort(new BasicDBObject("startTime", -1)).limit(limit).skip(offset);
		List<TimerResult> cases = Lists.newArrayList();
		while (result.hasNext()) {
			TimerResult temp = convert(result.next());
			if (temp != null) {
				cases.add(temp);
			}
		}
		return cases;
	}
}
