package com.tpofof.conmon.server.managers;

import java.util.List;

import com.google.common.collect.Lists;
import com.pofof.conmon.model.Device;
import com.pofof.conmon.model.DeviceConfiguration;
import com.pofof.conmon.model.Heartbeat;
import com.pofof.conmon.model.TestCase;
import com.pofof.conmon.model.TimerResult;
import com.tpofof.conmon.server.data.mongo.DeviceDAO;

public class DeviceManager extends AbstractModelManager<Device, DeviceDAO> {

	private final DeviceConfigurationManager deviceConfigMan;
	private final TestCaseManager testCaseManager;
	private final TimerResultManager timerResultsManager;
	private final HeartbeatManager hbManager;
	
	public DeviceManager(DeviceDAO deviceDao, DeviceConfigurationManager deviceConfigMan, 
			TestCaseManager testCaseManager, TimerResultManager timerResultsManager,
			HeartbeatManager heartBeatManager) {
		super(deviceDao);
		this.deviceConfigMan = deviceConfigMan;
		this.testCaseManager = testCaseManager;
		this.timerResultsManager = timerResultsManager;
		this.hbManager = heartBeatManager;
	}
	
	public Device findByDeviceId(int deviceId) {
		Device device = getDao().findByDeviceId(deviceId);
		return device != null 
				? device
				: new Device()
						.setDeviceId(deviceId)
						.setRegistered(false);
	}
	
	public Device register(int deviceId) {
		Device device = findByDeviceId(deviceId);
		if (!device.isRegistered()) {
			DeviceConfiguration config = deviceConfigMan.getNewConfig();
			if (config != null) {
				device.setConfigId(config.get_id());
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
			cases = testCaseManager.find();
		}
		return cases;
	}

	@Override
	public int getDefualtLimit() {
		return 10; // TODO: config or setting
	}
	
	public List<TimerResult> getResults(long deviceId) {
		return timerResultsManager.getByDevice(deviceId);
	}
	
	public List<TimerResult> getResults(long deviceId, int limit, int offset) {
		return timerResultsManager.getByDevice(deviceId, limit, offset);
	}
	
	public Heartbeat heartbeat(int deviceId, long time) {
		return hbManager.insert(deviceId, time);
	}
	
	public List<Heartbeat> getHeartbeats(int deviceId) {
		return hbManager.findByDeviceId(deviceId);
	}
}
