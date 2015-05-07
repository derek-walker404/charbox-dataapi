package co.charbox.dataapi.managers;

import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.data.elasticsearch.DeviceAuthDAO;
import co.charbox.domain.model.DeviceAuthModel;

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
	
	public boolean checkAuth(DeviceAuthModel auth) {
		if (auth != null) {
			DeviceAuthModel dbAuth = getDao().find(auth);
			return dbAuth != null && dbAuth.isActivated();
		}
		return false;
	}
}
