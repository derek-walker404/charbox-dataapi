package co.charbox.dataapi.managers;

import lombok.extern.slf4j.Slf4j;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.domain.data.mysql.HeartbeatDAO;
import co.charbox.domain.model.DeviceModel;
import co.charbox.domain.model.HeartbeatModel;
import co.charbox.domain.model.OutageModel;

import com.tpofof.core.data.dao.context.PrincipalSearchContext;
import com.tpofof.core.utils.Config;

@Slf4j
@Component
public class HeartbeatManager extends CharbotModelManager<HeartbeatModel, HeartbeatDAO> {
	
	@Autowired private Config config;
	@Autowired private CharbotModelManagerProvider manPro;
	
	@Autowired
	public HeartbeatManager(HeartbeatDAO dao, Config config) {
		super(dao);
	}

	@Override
	public int getDefualtLimit() {
		return config.getInt("heartbeat.limit", 10);
	}
	
	public HeartbeatModel insert(PrincipalSearchContext context, Integer deviceId, DateTime time, String ipAddress) {
		return insert(context, getManProvider().getDeviceManager().find(context, deviceId), time, ipAddress);
	}
	
	public HeartbeatModel insert(PrincipalSearchContext context, DeviceModel device, DateTime time, String ipAddress) {
		ConnectionInfoManager ciMan = manPro.getConnectionInfoManager();
		return insert(context, HeartbeatModel.builder()
				.device(device)
				.time(time)
				.connection(ciMan.findByIp(context, ipAddress))
				.build());
	}

	@Override
	public HeartbeatModel insert(PrincipalSearchContext context, HeartbeatModel hb) {
		HeartbeatModel lastHb = findByDeviceId(hb.getDevice().getId());
		if (lastHb != null) {
			long currTime = hb.getTime().getMillis();
			long lastTime = lastHb.getTime().getMillis();
			long interval = (currTime - lastTime) / 1000 / 60; // to minutes
			if (interval > config.getInt("outage.threshold.minutes", 3)) {
				getManProvider().getOutageManager()
					.insert(context, OutageModel.builder()
						.device(hb.getDevice())
						.startTime(lastHb.getTime())
						.endTime(new DateTime())
						.duration(interval)
						.connectionInfo(hb.getConnection())
						.build());
			}
			lastHb.setTime(hb.getTime());
			return updateTime(context, lastHb);
		} else {
			log.info("Creating heartbeat for " + hb.getDevice());
			return super.insert(context, hb);
		}
	}
	
	public HeartbeatModel updateTime(PrincipalSearchContext context, HeartbeatModel model) {
		return getDao().updateTime(model);
	}
	
	public HeartbeatModel findByDeviceId(Integer deviceId) {
		return getDao().findByDeviceId(deviceId);
	}

	@Override
	protected boolean hasDefaultSort() {
		return false;
	}
}
