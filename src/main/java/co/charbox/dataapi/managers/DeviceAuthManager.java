package co.charbox.dataapi.managers;

import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.data.elasticsearch.DeviceAuthDAO;
import co.charbox.domain.model.auth.DeviceAuthModel;

import com.tpofof.core.managers.AbstractModelManager;

@Component
public class DeviceAuthManager extends AbstractModelManager<DeviceAuthModel, String, DeviceAuthDAO, QueryBuilder> {

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
}
