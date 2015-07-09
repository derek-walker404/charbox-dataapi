package co.charbox.dataapi.managers;

import lombok.extern.slf4j.Slf4j;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.domain.data.mysql.HeartbeatDAO;
import co.charbox.domain.model.DeviceModel;
import co.charbox.domain.model.HeartbeatModel;
import co.charbox.domain.model.OutageModel;

import com.tpofof.core.data.dao.context.SimpleSearchContext;
import com.tpofof.core.utils.Config;

@Slf4j
@Component
public class HeartbeatManager extends CharbotModelManager<HeartbeatModel, HeartbeatDAO> {
	
	@Autowired private Config config;
	
	@Autowired
	public HeartbeatManager(HeartbeatDAO dao, Config config) {
		super(dao);
	}

	@Override
	public int getDefualtLimit() {
		return config.getInt("heartbeat.limit", 10);
	}
	
	public HeartbeatModel insert(SimpleSearchContext context, Integer deviceId, DateTime time) {
		return insert(context, getManProvider().getDeviceManager().find(context, deviceId), time);
	}
	
	public HeartbeatModel insert(SimpleSearchContext context, DeviceModel device, DateTime time) {
		return insert(context, HeartbeatModel.builder()
				.device(device)
				.time(time)
				.build());
	}

	@Override
	public HeartbeatModel insert(SimpleSearchContext context, HeartbeatModel hb) {
		HeartbeatModel lastHb = findByDeviceId(hb.getDevice().getId());
		if (lastHb != null) {
			long currTime = hb.getTime().getMillis();
			long lastTime = lastHb.getTime().getMillis();
			long interval = (currTime - lastTime) / 1000 / 60; // to minutes
			if (interval > config.getInt("outage.threshold.minutes", 3)) {
				getManProvider().getOutageManager()
					.insert(context, OutageModel.builder()
						.device(hb.getDevice())
						.startTime(lastHb.getTime()) // TODO: wft is happening?!
						.endTime(new DateTime())
						.duration(interval)
						.build());
			}
			lastHb.setTime(hb.getTime());
			return updateTime(context, lastHb);
		} else {
			log.info("Creating heartbeat for " + hb.getDevice());
			return super.insert(context, hb);
		}
	}
	
	public HeartbeatModel updateTime(SimpleSearchContext context, HeartbeatModel model) {
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
