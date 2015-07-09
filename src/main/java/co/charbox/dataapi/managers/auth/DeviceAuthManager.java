package co.charbox.dataapi.managers.auth;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.managers.CharbotModelManager;
import co.charbox.dataapi.managers.CharbotModelManagerProvider;
import co.charbox.domain.data.mysql.auth.DeviceAuthDAO;
import co.charbox.domain.model.DeviceModel;
import co.charbox.domain.model.auth.DeviceAuthModel;

@Component
public class DeviceAuthManager extends CharbotModelManager<DeviceAuthModel, DeviceAuthDAO> {

	@Autowired private CharbotModelManagerProvider manPro;
	
	@Autowired
	public DeviceAuthManager(DeviceAuthDAO dao) {
		super(dao);
	}

	@Override
	public int getDefualtLimit() {
		return 10;
	}

	@Override
	public Integer getDefaultId() {
		return -1;
	}
	
	public DeviceAuthModel isValid(Integer deviceId, String apiKey) {
		DeviceAuthModel auth = DeviceAuthModel.builder()
				.deviceId(deviceId)
				.key(apiKey)
				.build();
		DeviceAuthModel devAuth = find(auth);
		boolean validAuth = devAuth != null && devAuth.isActivated();
		return validAuth ? devAuth : null;
	}
	
	public DeviceAuthModel find(DeviceAuthModel model) {
		return getDao().find(model);
	}

	@Override
	protected boolean hasDefaultSort() {
		return false;
	}

	public DeviceAuthModel newInstall() {
		DeviceModel device = manPro.getDeviceManager().insert(null, DeviceModel.builder()
				.name("New Device")
				.build());
		DeviceAuthModel model = getDao().insert(DeviceAuthModel.builder()
				.deviceId(device.getId())
				.key(UUID.randomUUID().toString())
				.activated(true)
				.build());
		return model;
	}
}
