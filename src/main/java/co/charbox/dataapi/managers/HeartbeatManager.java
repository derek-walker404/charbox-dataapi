package co.charbox.dataapi.managers;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.data.elasticsearch.HeartbeatDAO;
import co.charbox.domain.model.Heartbeat;
import co.charbox.domain.model.Outage;

import com.tpofof.core.managers.AbstractEsModelManager;
import com.tpofof.core.utils.Config;

@Component
public class HeartbeatManager extends AbstractEsModelManager<Heartbeat, HeartbeatDAO> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HeartbeatManager.class);
	
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
	
	public Heartbeat insert(String deviceId, DateTime time) {
		return insert(Heartbeat.builder()
				.deviceId(deviceId)
				.time(time)
				.build());
	}

	@Override
	public Heartbeat insert(Heartbeat hb) {
		Heartbeat lastHb = findByDeviceId(hb.getDeviceId());
		if (lastHb != null) {
			long currTime = hb.getTime().getMillis();
			long lastTime = lastHb.getTime().getMillis();
			long interval = (currTime - lastTime) / 1000 / 60; // to minutes
			if (interval > config.getInt("outage.threshold.minutes", 3)) {
				outageManager.insert(Outage.builder()
						.deviceId(hb.getDeviceId())
						.startTime(hb.getTime())
						.endTime(new DateTime())
						.duration(interval)
						.build());
			}
			lastHb.setTime(hb.getTime());
			return super.update(lastHb);
		} else {
			LOGGER.info("Creating heartbeat for " + hb.getDeviceId());
			return super.insert(hb);
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
