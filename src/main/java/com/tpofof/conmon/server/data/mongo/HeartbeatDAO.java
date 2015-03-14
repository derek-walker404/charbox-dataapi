package com.tpofof.conmon.server.data.mongo;

import java.util.List;

import com.google.api.client.util.Lists;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.pofof.conmon.model.Heartbeat;
import com.tpofof.conmon.server.data.SearchResults;

public class HeartbeatDAO extends GenericMongoDAO<Heartbeat> {

	private static final long HOUR_IN_MS = 1000 * 60 * 60;
	
	public HeartbeatDAO(DBCollection collection) {
		super(collection, Heartbeat.class);
	}
	
	public SearchResults<Heartbeat> findByDeviceId(int deviceId) {
		BasicDBObject query = new BasicDBObject("deviceId", deviceId);
		query.append("time", new BasicDBObject("$gt", System.currentTimeMillis() - HOUR_IN_MS));
		DBCursor result = getCollection().find(query);
		List<Heartbeat> hbs = Lists.newArrayList();
		for (int i=0;i<result.count();i++) {
			hbs.add(convert(result.next()));
		}
		return new SearchResults<Heartbeat>()
				.setLimit(1000)
				.setOffset(0)
				.setResults(hbs);
	}
}
