package co.charbox.dataapi.managers;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.data.elasticsearch.DeviceDAO;
import co.charbox.domain.model.Device;
import co.charbox.domain.model.DeviceConfiguration;
import co.charbox.domain.model.Heartbeat;
import co.charbox.domain.model.TestCase;
import co.charbox.domain.model.TimerResult;

import com.google.common.collect.Lists;
import com.tpofof.core.data.dao.ResultsSet;
import com.tpofof.core.managers.AbstractEsModelManager;
import com.tpofof.core.utils.Config;

@Component
public class DeviceManager extends AbstractEsModelManager<Device, DeviceDAO> {

	private int defualtLimit;
	
	@Autowired private DeviceConfigurationManager deviceConfigMan;
	@Autowired private TestCaseManager testCaseManager;
	@Autowired private TimerResultManager timerResultsManager;
	@Autowired private HeartbeatManager hbManager;
	
	@Autowired
	public DeviceManager(DeviceDAO deviceDao, Config config) {
		super(deviceDao);
		this.defualtLimit = config.getInt("device.limit", 10);
	}
	
	/**
	 * 
	 * @param deviceId
	 * @return Never {@code null}. Only deviceId field is populated if device is not activated.
	 */
	public Device findByDeviceId(String deviceId) {
		ResultsSet<Device> devices = getDao().findByDeviceId(deviceId);
		Device device = devices.getResults().size() == 1 ? devices.getResults().get(0) : null;
		return device != null 
				? device
				: Device.builder()
					.deviceId(deviceId)
					.registered(false)
					.build();
	}
	
	public Device register(String deviceId) {
		Device device = findByDeviceId(deviceId);
		if (!device.isRegistered()) {
			DeviceConfiguration config = deviceConfigMan.getNewConfig();
			if (config != null) {
				device.setConfigId(config.getId());
				device.setRegistered(true);
				Device temp = getDao().insert(device);
				if (temp == null) {
					device.setRegistered(false);
				} else {
					device = temp;
				}
			}
		}
		return device;
	}

	/**
	 * Use {@link DeviceManager#register(int)}
	 */
	@Deprecated
	public Device insert(Device model) {
		throw new UnsupportedOperationException("Use {@link DeviceManager#register(int)}");
	}
	
	public List<TestCase> getTestCases(Device model) {
		List<TestCase> cases = Lists.newArrayList();
		if (model != null) {
			cases = testCaseManager.find().getResults();
		}
		return cases;
	}

	@Override
	public int getDefualtLimit() {
		return defualtLimit; // TODO: config or setting
	}
	
	public ResultsSet<TimerResult> getResults(String deviceId) {
		return timerResultsManager.getByDevice(deviceId);
	}
	
	public ResultsSet<TimerResult> getResults(String deviceId, int limit, int offset) {
		return timerResultsManager.getByDevice(deviceId, limit, offset);
	}
	
	public Heartbeat heartbeat(String deviceId, DateTime time) {
		return hbManager.insert(deviceId, time);
	}
	
	public Heartbeat getHeartbeats(String deviceId) {
		return hbManager.findByDeviceId(deviceId);
	}

	@Override
	public String getDefaultId() {
		return "";
	}

	@Override
	protected boolean hasDefualtSort() {
		return false;
	}
}
