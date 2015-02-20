package com.tpofof.conmon.server.managers;

import java.util.List;

import com.google.common.collect.Lists;
import com.pofof.conmon.model.Device;
import com.pofof.conmon.model.DeviceConfiguration;
import com.pofof.conmon.model.TestCase;
import com.tpofof.conmon.server.data.mongo.DeviceDAO;

public class DeviceManager implements GenericModelManager<Device> {

	private final DeviceConfigurationManager deviceConfigMan;
	private final DeviceDAO deviceDao;
	
	public DeviceManager(DeviceDAO deviceDao, DeviceConfigurationManager deviceConfigMan) {
		this.deviceDao = deviceDao;
		this.deviceConfigMan = deviceConfigMan;
	}
	
	public List<Device> find() {
		return find(10, 0);
	}
	
	public List<Device> find(int limit, int offset) {
		return deviceDao.find(limit, offset);
	}
	
	public long count() {
		return deviceDao.count();
	}
	
	public Device find(String id) {
		return deviceDao.find(id);
	}
	
	public Device findByDeviceId(int deviceId) {
		Device device = deviceDao.findByDeviceId(deviceId);
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
				Device temp = deviceDao.insert(device);
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
			DeviceConfiguration config = deviceConfigMan.find(model.getConfigId());
			if (config != null) {
				cases = deviceConfigMan.getTestCases(config.getTestCaseIds());
			}
		}
		return cases;
	}

	public Device update(Device model) {
		return deviceDao.update(model);
	}

	public boolean delete(String id) {
		return deviceDao.delete(id);
	}
}
