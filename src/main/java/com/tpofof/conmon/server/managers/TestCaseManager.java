package com.tpofof.conmon.server.managers;

import com.pofof.conmon.model.TestCase;
import com.tpofof.conmon.server.data.mongo.TestCaseDAO;

public class TestCaseManager extends AbstractModelManager<TestCase, TestCaseDAO> {

	public TestCaseManager(TestCaseDAO testCaseDao) {
		super(testCaseDao);
	}

	@Override
	public int getDefualtLimit() {
		return 20; // TODO: config or setting
	}
}
