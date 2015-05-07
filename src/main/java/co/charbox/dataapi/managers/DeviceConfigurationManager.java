package co.charbox.dataapi.managers;

import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.data.elasticsearch.DeviceConfigDAO;
import co.charbox.domain.model.DeviceConfiguration;

import com.tpofof.core.managers.AbstractModelManager;
import com.tpofof.core.utils.Config;

@Component
public class DeviceConfigurationManager extends AbstractModelManager<DeviceConfiguration, String, DeviceConfigDAO, QueryBuilder> {

	private final Config config;

	@Autowired
	public DeviceConfigurationManager(DeviceConfigDAO deviceConfigDao, Config config) {
		super(deviceConfigDao);
		this.config = config;
	}

	@Override
	public String getDefaultId() {
		return "";
	}
	
	public DeviceConfiguration getNewConfig() {
		DeviceConfiguration newConfig = DeviceConfiguration.builder()
				.testInterval(config.getInt("device_config.test_interval_mins.default", 30))
				.trialsCount(config.getInt("device_config.trials_count.default", 3))
				.build();
		return getDao().insert(newConfig);
	}

	@Override
	public int getDefualtLimit() {
		return 10; // TODO: config or setting
	}
}
