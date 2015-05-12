package co.charbox.dataapi.data.elasticsearch;

import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.domain.model.DeviceConfiguration;

import com.tpofof.core.data.dao.es.AbstractElasticsearchDAO;
import com.tpofof.core.utils.Config;

@Component
public class DeviceConfigDAO extends AbstractElasticsearchDAO<DeviceConfiguration> {

	private String index;
	private String type;

	@Autowired
	public DeviceConfigDAO(Config config, Client client) {
		super(config, client);
		init();
	}
	
	@Override
	protected String getIndex() {
		if (index == null) {
			index = getConfig().getString("es.device_config.index", "charbot_v0.1_config");
		}
		return index;
	}

	@Override
	protected String getType() {
		if (type == null) {
			type = getConfig().getString("es.device_config.type", "config");
		}
		return type;
	}

	@Override
	protected Class<DeviceConfiguration> getModelClass() {
		return DeviceConfiguration.class;
	}

	@Override
	protected boolean isRequiredIndex() {
		return true;
	}

	@Override
	protected boolean hasMapping() {
		return false;
	}
}
