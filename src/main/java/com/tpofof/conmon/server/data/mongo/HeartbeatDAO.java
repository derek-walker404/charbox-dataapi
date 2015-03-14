package com.tpofof.conmon.server.data.mongo;

import java.util.List;

import com.google.api.client.util.Lists;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.pofof.conmon.model.Heartbeat;

public class HeartbeatDAO extends GenericMongoDAO<Heartbeat> {

	private static final long HOUR_IN_MS = 1000 * 60 * 60;
	
	public HeartbeatDAO(DBCollection collection) {
		super(collection, Heartbeat.class);
	}
	
	public List<Heartbeat> findByDeviceId(int deviceId) {
		BasicDBObject query = new BasicDBObject("deviceId", deviceId);
		query.append("time", new BasicDBObject("$gt", System.currentTimeMillis() - HOUR_IN_MS));
		DBCursor result = getCollection().find(query);
		List<Heartbeat> hbs = Lists.newArrayList();
		for (int i=0;i<result.count();i++) {
			hbs.add(convert(result.next()));
		}
		return hbs;
	}
}
