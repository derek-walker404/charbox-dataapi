package co.charbox.dataapi.data.mongo;

import com.mongodb.DBCollection;
import com.pofof.conmon.model.DeviceConfiguration;

public class DeviceConfigDAO extends GenericMongoDAO<DeviceConfiguration> {

	public DeviceConfigDAO(DBCollection collection) {
		super(collection, DeviceConfiguration.class);
	}
}
