package co.charbox.dataapi.data.mongo;

import co.charbox.core.data.mongo.AbstractMongoDAO;
import co.charbox.domain.model.TestCase;

import com.mongodb.DBCollection;

public class TestCaseDAO extends AbstractMongoDAO<TestCase> {
	
	public TestCaseDAO(DBCollection testCaseCollection) {
		super(testCaseCollection, TestCase.class);
	}
}
