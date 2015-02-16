package com.tpofof.conmon.server.mongo;

import org.bson.types.ObjectId;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.mongodb.util.JSON;
import com.pofof.conmon.model.DeviceConfiguration;
import com.tpofof.utils.JsonUtils;

public class DeviceConfigDAO {

	private DBCollection collection;
	
	public DeviceConfigDAO(DBCollection collection) {
		this.collection = collection;
	}
	
	public DeviceConfiguration insert(DeviceConfiguration config) {
		DBObject obj = (DBObject) JSON.parse(JsonUtils.toJson(config));
		ObjectId insertId = new ObjectId();
		obj.put("_id", insertId);
		WriteResult wr = collection.insert(obj);
		if (insertId.equals(wr.getField("_id"))) {
			return get(insertId.toString());
		}
		return null;
	}
	
	public DeviceConfiguration get(String id) {
		DBObject obj = collection.findOne(id);
		return obj == null
				? null
				: JsonUtils.fromJson(JSON.serialize(obj), DeviceConfiguration.class);
	}
}
