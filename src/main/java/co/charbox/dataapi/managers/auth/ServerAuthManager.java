package co.charbox.dataapi.managers.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.data.elasticsearch.auth.ServerAuthDAO;
import co.charbox.domain.model.auth.ServerAuthModel;

import com.tpofof.core.managers.AbstractEsModelManager;

@Component
public class ServerAuthManager extends AbstractEsModelManager<ServerAuthModel, ServerAuthDAO> {

	@Autowired
	public ServerAuthManager(ServerAuthDAO dao) {
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
	
	public ServerAuthModel isValid(String serverId, String serverKey) {
		ServerAuthModel auth = ServerAuthModel.builder()
				.serverId(serverId)
				.serverKey(serverKey)
				.build();
		ServerAuthModel devAuth = find(auth);
		boolean validAuth = devAuth != null && devAuth.isActivated();
		return validAuth ? devAuth : null;
	}
	
	public ServerAuthModel find(ServerAuthModel auth) {
		return auth != null ? getDao().find(auth) : null;
	}

	@Override
	protected boolean hasDefualtSort() {
		return false;
	}
}
