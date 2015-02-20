package com.tpofof.conmon.server.managers;

import java.util.List;

import com.google.common.collect.Lists;
import com.pofof.conmon.model.DeviceConfiguration;
import com.pofof.conmon.model.TestCase;
import com.tpofof.conmon.server.data.mongo.DeviceConfigDAO;

public class DeviceConfigurationManager implements GenericModelManager<DeviceConfiguration> {

	private final DeviceConfigDAO deviceConfigDao;
	private final TestCaseManager testCaseMan;
	
	public DeviceConfigurationManager(DeviceConfigDAO deviceConfigDao,
			TestCaseManager testCaseMan) {
		super();
		this.deviceConfigDao = deviceConfigDao;
		this.testCaseMan = testCaseMan;
	}
	
	public DeviceConfiguration getNewConfig() {
		List<TestCase> cases = testCaseMan.find(6, 0); // TODO: convert to manager and get random set
		List<String> tcIds = Lists.newArrayList();
		for (TestCase tc : cases) {
			tcIds.add(tc.get_id());
		}
		DeviceConfiguration newConfig = new DeviceConfiguration();
		newConfig.setTestCaseIds(tcIds);
		return deviceConfigDao.insert(newConfig);
	}
	
	public List<TestCase> getTestCases(List<String> ids) {
		List<TestCase> cases = Lists.newArrayList();
		for (String s : ids) {
			cases.add(testCaseMan.find(s));
		}
		return cases;
	}

	public DeviceConfiguration find(String id) {
		return deviceConfigDao.find(id);
	}

	public List<DeviceConfiguration> find() {
		return find(20, 0);
	}

	public List<DeviceConfiguration> find(int limit, int offset) {
		return deviceConfigDao.find(limit, offset);
	}
	
	public long count() {
		return deviceConfigDao.count();
	}

	public DeviceConfiguration insert(DeviceConfiguration model) {
		return deviceConfigDao.insert(model);
	}

	public DeviceConfiguration update(DeviceConfiguration model) {
		return deviceConfigDao.update(model);
	}

	public boolean delete(String id) {
		return deviceConfigDao.delete(id);
	}
}
