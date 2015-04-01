package co.charbox.dataapi.data.mongo;

import co.charbox.core.data.mongo.AbstractMongoDAO;
import co.charbox.domain.model.Device;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

public class DeviceDAO extends AbstractMongoDAO<Device> {

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
