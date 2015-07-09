package co.charbox.dataapi.managers.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.managers.CharbotModelManager;
import co.charbox.domain.data.mysql.auth.ServerAuthDAO;
import co.charbox.domain.model.auth.ServerAuthModel;

@Component
public class ServerAuthManager extends CharbotModelManager<ServerAuthModel, ServerAuthDAO> {

	@Autowired
	public ServerAuthManager(ServerAuthDAO dao) {
		super(dao);
	}

	@Override
	public int getDefualtLimit() {
		return 10;
	}
	
	public ServerAuthModel isValid(String serverId, String key, String serviceName) {
		ServerAuthModel auth = ServerAuthModel.builder()
				.serverId(serverId)
				.key(key)
				.serviceName(serviceName)
				.build();
		ServerAuthModel devAuth = find(auth);
		boolean validAuth = devAuth != null && devAuth.isActivated();
		return validAuth ? devAuth : null;
	}
	
	public ServerAuthModel find(ServerAuthModel auth) {
		return auth != null ? getDao().find(auth) : null;
	}

	@Override
	protected boolean hasDefaultSort() {
		return false;
	}
}
