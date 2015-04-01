package co.charbox.dataapi.data.mongo;

import co.charbox.core.data.mongo.AbstractMongoDAO;
import co.charbox.domain.model.DeviceConfiguration;

import com.mongodb.DBCollection;

public class DeviceConfigDAO extends AbstractMongoDAO<DeviceConfiguration> {

	public DeviceConfigDAO(DBCollection collection) {
		super(collection, DeviceConfiguration.class);
	}
}
