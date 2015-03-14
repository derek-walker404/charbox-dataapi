package com.tpofof.conmon.server.managers;

import com.pofof.conmon.model.Heartbeat;
import com.tpofof.conmon.server.data.SearchResults;
import com.tpofof.conmon.server.data.mongo.HeartbeatDAO;

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
