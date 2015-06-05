package co.charbox.dataapi.data.elasticsearch;

import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.domain.model.DeviceVersionModel;

import com.tpofof.core.data.dao.es.AbstractElasticsearchDAO;
import com.tpofof.core.io.IO;
import com.tpofof.core.utils.Config;

@Component
public class DeviceVersionDAO extends AbstractElasticsearchDAO<DeviceVersionModel> {

	private IO io;
	private String index;
	private String type;

	@Autowired
	public DeviceVersionDAO(Config config, Client client, IO io) {
		super(config, client);
		this.io = io;
		init(config.getBoolean("es.deleteAll", false));
	}
	
	@Override
	protected String getIndex() {
		if (index == null) {
			index = getConfig().getString("es.device_version.index", "charbot_v0.1_version");
		}
		return index;
	}

	@Override
	protected String getType() {
		if (type == null) {
			type = getConfig().getString("es.device_version.type", "version");
		}
		return type;
	}

	@Override
	protected Class<DeviceVersionModel> getModelClass() {
		return DeviceVersionModel.class;
	}

	@Override
	protected boolean isRequiredIndex() {
		return true;
	}

	@Override
	protected String getMapping() {
		String filename = getConfig().getString("es.device_version.mapping.name", "mappings/es.device_version.mapping.json");
		return io.getContents(filename);
	}
}
