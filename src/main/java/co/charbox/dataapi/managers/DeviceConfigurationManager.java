package co.charbox.dataapi.managers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.data.elasticsearch.DeviceConfigDAO;
import co.charbox.domain.model.DeviceConfiguration;

import com.tpofof.core.managers.AbstractEsModelManager;
import com.tpofof.core.utils.Config;

@Component
public class DeviceConfigurationManager extends AbstractEsModelManager<DeviceConfiguration, DeviceConfigDAO> {

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

	@Override
	protected boolean hasDefaultSort() {
		return false;
	}
}
