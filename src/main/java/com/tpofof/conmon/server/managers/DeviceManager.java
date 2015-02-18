package com.tpofof.conmon.server.managers;

import java.util.List;

import com.pofof.conmon.model.Device;
import com.pofof.conmon.model.DeviceConfiguration;
import com.tpofof.conmon.server.mongo.DeviceDAO;

public class DeviceManager {

	private final DeviceConfigurationManager deviceConfigMan;
	private final DeviceDAO deviceDao;
	
	public DeviceManager(DeviceDAO deviceDao, DeviceConfigurationManager deviceConfigMan) {
		this.deviceDao = deviceDao;
		this.deviceConfigMan = deviceConfigMan;
	}
	
	public List<Device> getDevices(int limit, int offset) {
		return deviceDao.find(limit, offset);
	}
	
	public Device getDevice(String id) {
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
}
