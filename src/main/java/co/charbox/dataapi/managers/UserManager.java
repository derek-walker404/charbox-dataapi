package co.charbox.dataapi.managers;

import org.springframework.beans.factory.annotation.Autowired;

import com.tpofof.core.utils.Config;

import co.charbox.domain.data.mysql.UserDAO;
import co.charbox.domain.model.UserModel;

public class UserManager extends CharbotModelManager<UserModel, UserDAO>{


	@Autowired private Config config;
	
	public UserManager(UserDAO dao) {
		super(dao);
	}

	@Override
	protected int getDefualtLimit() {
		return config.getInt("users.limit", 10);
	}

	@Override
	protected boolean hasDefaultSort() {
		return false;
	}

}
