package co.charbox.dataapi.managers;

import org.springframework.beans.factory.annotation.Autowired;

import com.tpofof.core.utils.Config;

import co.charbox.domain.data.mysql.UserDeviceDAO;
import co.charbox.domain.model.UserDeviceModel;

public class UserDeviceManager extends CharbotModelManager<UserDeviceModel, UserDeviceDAO> {

	@Autowired private Config config;
	
	public UserDeviceManager(UserDeviceDAO dao) {
		super(dao);
	}
	
	@Override
	protected int getDefualtLimit() {
		return config.getInt("userdevice.limit", 10);
	}

	@Override
	protected boolean hasDefaultSort() {
		return false;
	}

}
