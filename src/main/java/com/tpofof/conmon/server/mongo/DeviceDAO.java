package com.tpofof.conmon.server.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.pofof.conmon.model.Device;

public class DeviceDAO extends GenericMongoDAO<Device> {

	public DeviceDAO(DBCollection collection) {
		super(collection, Device.class);
	}
	
	public Device findByDeviceId(int deviceId) {
		DBCursor result = getCollection().find(new BasicDBObject("deviceId", deviceId));
		return result.count() == 1
				? convert(result.next()) 
				: null;
	}
}
