package com.tpofof.conmon.server.managers;

import java.util.List;

import com.pofof.conmon.model.TestCase;
import com.tpofof.conmon.server.mongo.TestCaseDAO;

public class TestCaseManager implements GenericModelManager<TestCase> {

	private final TestCaseDAO testCaseDao;
	
	public TestCaseManager(TestCaseDAO testCaseDao) {
		this.testCaseDao = testCaseDao;
	}
	
	public TestCase find(String id) {
		return testCaseDao.find(id);
	}

	public List<TestCase> find() {
		return find(20, 0);
	}

	public List<TestCase> find(int limit, int offset) {
		return testCaseDao.find(limit, offset);
	}

	public TestCase insert(TestCase model) {
		return testCaseDao.insert(model);
	}

}
