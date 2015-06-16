package co.charbox.dataapi.managers;

import lombok.extern.slf4j.Slf4j;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.data.elasticsearch.HeartbeatDAO;
import co.charbox.domain.model.Heartbeat;
import co.charbox.domain.model.Outage;

import com.tpofof.core.data.dao.context.SimpleSearchContext;
import com.tpofof.core.utils.Config;

@Slf4j
@Component
public class HeartbeatManager extends CharbotModelManager<Heartbeat, HeartbeatDAO> {
	
	@Autowired private Config config;
	@Autowired private OutageManager outageManager;
	
	@Autowired
	public HeartbeatManager(HeartbeatDAO dao, Config config) {
		super(dao);
	}

	@Override
	public int getDefualtLimit() {
		return config.getInt("heartbeat.limit", 10);
	}
	
	@Override
	public String getDefaultId() {
		return "";
	}
	
	public Heartbeat insert(SimpleSearchContext context, String deviceId, DateTime time) {
		return insert(context, Heartbeat.builder()
				.deviceId(deviceId)
				.time(time)
				.build());
	}

	@Override
	public Heartbeat insert(SimpleSearchContext context, Heartbeat hb) {
		Heartbeat lastHb = findByDeviceId(hb.getDeviceId());
		if (lastHb != null) {
			long currTime = hb.getTime().getMillis();
			long lastTime = lastHb.getTime().getMillis();
			long interval = (currTime - lastTime) / 1000 / 60; // to minutes
			if (interval > config.getInt("outage.threshold.minutes", 3)) {
				outageManager.insert(context, Outage.builder()
						.deviceId(hb.getDeviceId())
						.startTime(lastHb.getTime()) // TODO: wft is happening?!
						.endTime(new DateTime())
						.duration(interval)
						.build());
			}
			lastHb.setTime(hb.getTime());
			return super.update(context, lastHb);
		} else {
			log.info("Creating heartbeat for " + hb.getDeviceId());
			return super.insert(context, hb);
		}
	}
	
	public Heartbeat findByDeviceId(String deviceId) {
		return getDao().findByDeviceId(deviceId);
	}

	@Override
	protected boolean hasDefaultSort() {
		return false;
	}
}
