package co.charbox.dataapi.data.elasticsearch;

import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.domain.model.Device;

import com.tpofof.core.data.dao.ResultsSet;
import com.tpofof.core.data.dao.es.AbstractElasticsearchDAO;
import com.tpofof.core.data.dao.es.EsQuery;
import com.tpofof.core.io.IO;
import com.tpofof.core.utils.Config;

@Component
public class DeviceDAO extends AbstractElasticsearchDAO<Device> {

	private IO io;
	private String index;
	private String type;
	
	@Autowired
	public DeviceDAO(Config config, Client client, IO io) {
		super(config, client);
		this.io = io;
		init();
	}

	@Override
	protected String getIndex() {
		if (index == null) {
			index = getConfig().getString("es.device.index", "charbot_v0.1_device");
		}
		return index;
	}

	@Override
	protected String getType() {
		if (type == null) {
			type = getConfig().getString("es.device.type", "device");
		}
		return type;
	}

	@Override
	protected Class<Device> getModelClass() {
		return Device.class;
	}
	
	@Override
	protected boolean isRequiredIndex() {
		return true;
	}
	
	@Override
	protected String getMapping() {
		String filename = getConfig().getString("es.device.mapping.name", "mappings/es.device.mapping.json");
		return io.getContents(filename);
	}
	
	public ResultsSet<Device> findByDeviceId(String deviceId) {
		EsQuery q = EsQuery.builder()
				.constraints(QueryBuilders.termQuery("deviceId", deviceId))
				.limit(10)
				.offset(0)
				.build();
		return super.find(q);
	}
}
