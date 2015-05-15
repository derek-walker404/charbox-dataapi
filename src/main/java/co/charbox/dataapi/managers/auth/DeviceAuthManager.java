package co.charbox.dataapi.managers.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.data.elasticsearch.auth.DeviceAuthDAO;
import co.charbox.domain.model.auth.DeviceAuthModel;

import com.tpofof.core.managers.AbstractEsModelManager;

@Component
public class DeviceAuthManager extends AbstractEsModelManager<DeviceAuthModel, DeviceAuthDAO> {

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
}
