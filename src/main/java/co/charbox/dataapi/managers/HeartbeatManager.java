package co.charbox.dataapi.managers;

import co.charbox.dataapi.data.SearchResults;
import co.charbox.dataapi.data.mongo.HeartbeatDAO;

import com.pofof.conmon.model.Heartbeat;

public class HeartbeatManager extends AbstractModelManager<Heartbeat, HeartbeatDAO> {

	public HeartbeatManager(HeartbeatDAO dao) {
		super(dao);
	}

	@Override
	public int getDefualtLimit() {
		return 100;
	}

	public Heartbeat insert(int deviceId, long time) {
		return insert(new Heartbeat().setDeviceId(deviceId).setTime(time));
		
	}

	public SearchResults<Heartbeat> findByDeviceId(int deviceId) {
		return getDao().findByDeviceId(deviceId);
	}
}
