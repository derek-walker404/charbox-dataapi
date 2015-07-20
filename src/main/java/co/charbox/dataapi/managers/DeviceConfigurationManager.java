package co.charbox.dataapi.managers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.domain.data.mysql.DeviceConfigurationDAO;
import co.charbox.domain.model.DeviceConfigurationModel;
import co.charbox.domain.model.DeviceModel;

import com.tpofof.core.data.dao.context.PrincipalSearchContext;

@Component
public class DeviceConfigurationManager extends CharbotModelManager<DeviceConfigurationModel, DeviceConfigurationDAO> {

	@Autowired
	public DeviceConfigurationManager(DeviceConfigurationDAO deviceConfigDao) {
		super(deviceConfigDao);
	}

	public DeviceConfigurationModel getNewConfig(PrincipalSearchContext context, DeviceModel device) {
		DeviceConfigurationModel newConfig = DeviceConfigurationModel.builder()
				.device(device)
				.registered(false)
				.schedules(null)
				.version(null)
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
	
	public DeviceConfigurationModel findByDeviceId(PrincipalSearchContext context, Integer deviceId) {
		return getDao().findByDeviceId(context, deviceId);
	}
	
	public DeviceConfigurationModel updateRegistered(PrincipalSearchContext context, DeviceConfigurationModel model) {
		return getDao().updateRegistered(model);
	}
}
