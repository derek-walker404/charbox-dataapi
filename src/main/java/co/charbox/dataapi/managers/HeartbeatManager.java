package co.charbox.dataapi.managers;

import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.data.elasticsearch.HeartbeatDAO;
import co.charbox.domain.model.Heartbeat;

import com.tpofof.core.managers.AbstractModelManager;
import com.tpofof.core.utils.Config;

@Component
public class HeartbeatManager extends AbstractModelManager<Heartbeat, String, HeartbeatDAO, QueryBuilder> {
	
	private int defaultLimit;
	
	@Autowired
	public HeartbeatManager(HeartbeatDAO dao, Config config) {
		super(dao);
		this.defaultLimit = config.getInt("heartbeat.limit", 10);
		
	}

	@Override
	public int getDefualtLimit() {
		return defaultLimit;
	}
	
	@Override
	public String getDefaultId() {
		return "";
	}

	public Heartbeat insert(String deviceId, long time) {
		return insert(Heartbeat.builder()
				.deviceId(deviceId)
				.time(time)
				.build());
	}

	public Heartbeat findByDeviceId(String deviceId) {
		return getDao().findByDeviceId(deviceId);
	}
}
