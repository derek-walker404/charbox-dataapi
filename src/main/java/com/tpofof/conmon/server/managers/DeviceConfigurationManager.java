package com.tpofof.conmon.server.managers;

import java.util.List;

import com.google.common.collect.Lists;
import com.pofof.conmon.model.DeviceConfiguration;
import com.pofof.conmon.model.TestCase;
import com.tpofof.conmon.server.data.mongo.DeviceConfigDAO;

public class DeviceConfigurationManager extends AbstractModelManager<DeviceConfiguration, DeviceConfigDAO> {

	private final TestCaseManager testCaseMan;
	
	public DeviceConfigurationManager(DeviceConfigDAO deviceConfigDao,
			TestCaseManager testCaseMan) {
		super(deviceConfigDao);
		this.testCaseMan = testCaseMan;
	}
	
	public DeviceConfiguration getNewConfig() {
		List<TestCase> cases = testCaseMan.find(6, 0).getResults(); // TODO: convert to manager and get random set
		List<String> tcIds = Lists.newArrayList();
		for (TestCase tc : cases) {
			tcIds.add(tc.get_id());
		}
		DeviceConfiguration newConfig = new DeviceConfiguration();
		return getDao().insert(newConfig);
	}
	
	public List<TestCase> getTestCases(List<String> ids) {
		List<TestCase> cases = Lists.newArrayList();
		for (String s : ids) {
			cases.add(testCaseMan.find(s));
		}
		return cases;
	}

	@Override
	public int getDefualtLimit() {
		return 10; // TODO: config or setting
	}
}
