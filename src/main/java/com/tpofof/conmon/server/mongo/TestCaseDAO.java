package com.tpofof.conmon.server.mongo;

import com.mongodb.DBCollection;
import com.pofof.conmon.model.TestCase;

public class TestCaseDAO extends GenericMongoDAO<TestCase> {
	
	public TestCaseDAO(DBCollection testCaseCollection) {
		super(testCaseCollection, TestCase.class);
	}
}
