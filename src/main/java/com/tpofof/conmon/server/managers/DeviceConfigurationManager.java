package com.tpofof.conmon.server.managers;

import java.util.List;

import com.google.common.collect.Lists;
import com.pofof.conmon.model.DeviceConfiguration;
import com.pofof.conmon.model.TestCase;
import com.tpofof.conmon.server.mongo.DeviceConfigDAO;
import com.tpofof.conmon.server.mongo.TestCaseDAO;

public class DeviceConfigurationManager {

	private final DeviceConfigDAO deviceConfigDao;
	private final TestCaseDAO testCaseDao;
	
	public DeviceConfigurationManager(DeviceConfigDAO deviceConfigDao,
			TestCaseDAO testCaseDao) {
		super();
		this.deviceConfigDao = deviceConfigDao;
		this.testCaseDao = testCaseDao;
	}
	
	public DeviceConfiguration getNewConfig() {
		List<TestCase> cases = testCaseDao.find(6, 0); // TODO: convert to manager and get random set
		List<String> tcIds = Lists.newArrayList();
		for (TestCase tc : cases) {
			tcIds.add(tc.get_id());
		}
		DeviceConfiguration newConfig = new DeviceConfiguration();
		newConfig.setTestCaseIds(tcIds);
		return deviceConfigDao.insert(newConfig);
	}
}
