package co.charbox.dataapi.managers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.core.mm.MaxMindService;
import co.charbox.domain.data.mysql.ConnectionInfoDAO;
import co.charbox.domain.model.mm.ConnectionInfoModel;

import com.tpofof.core.data.dao.context.PrincipalSearchContext;
import com.tpofof.core.utils.Config;

@Component
public class ConnectionInfoManager extends CharbotModelManager<ConnectionInfoModel, ConnectionInfoDAO> {

	@Autowired private MaxMindService mm;
	private final int defaultLimit;
	
	@Autowired
	public ConnectionInfoManager(ConnectionInfoDAO dao, Config config) {
		super(dao);
		this.defaultLimit = config.getInt("connInfo.limit", 10);
	}

	@Override
	public int getDefualtLimit() {
		return defaultLimit;
	}
	

	@Override
	protected boolean hasDefaultSort() {
		return false;
	}
	
	public ConnectionInfoModel find(PrincipalSearchContext context, ConnectionInfoModel model) {
		return model.getId() != null
				? model
				: getDao().find(context, model.getConnection().getIp(), 
						model.getLocation().getLat(), 
						model.getLocation().getLon());
	}

	public ConnectionInfoModel findByIp(PrincipalSearchContext context, String ipAddress) {
		ConnectionInfoModel conn = mm.get(ipAddress);
		if (conn == null) {
			return null;
		}
		ConnectionInfoModel existing = find(context, conn);
		return existing != null ? existing : conn;
	}
}
