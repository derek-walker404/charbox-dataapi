package co.charbox.dataapi.managers.auth;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.data.elasticsearch.auth.DeviceAuthDAO;
import co.charbox.dataapi.managers.CharbotModelManager;
import co.charbox.domain.model.auth.DeviceAuthModel;

@Component
public class DeviceAuthManager extends CharbotModelManager<DeviceAuthModel, DeviceAuthDAO> {

	@Autowired
	public DeviceAuthManager(DeviceAuthDAO dao) {
		super(dao);
	}

	@Override
	public int getDefualtLimit() {
		return 10;
	}

	@Override
	public String getDefaultId() {
		return "";
	}
	
	public DeviceAuthModel isValid(String deviceId, String apiKey) {
		DeviceAuthModel auth = DeviceAuthModel.builder()
				.deviceId(deviceId)
				.apiKey(apiKey)
				.build();
		DeviceAuthModel devAuth = find(auth);
		boolean validAuth = devAuth != null && devAuth.isActivated();
		return validAuth ? devAuth : null;
	}
	
	public DeviceAuthModel find(DeviceAuthModel auth) {
		return auth != null ? getDao().find(auth) : null;
	}

	@Override
	protected boolean hasDefaultSort() {
		return false;
	}

	public DeviceAuthModel newInstall() {
		String deviceId = null;
		while (deviceId == null || deviceIdExists(deviceId)) {
			deviceId = UUID.randomUUID().toString().substring(4, 13);
		}
		DeviceAuthModel model = getDao().insert(DeviceAuthModel.builder()
				.deviceId(deviceId)
				.apiKey(UUID.randomUUID().toString().substring(0, 13))
				.activated(true)
				.build());
		return model;
	}
	
	protected boolean deviceIdExists(String deviceId) {
		return getDao().findByDeviceId(deviceId) != null;
	}
}
