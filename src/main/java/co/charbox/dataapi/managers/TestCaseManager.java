package co.charbox.dataapi.managers;

import co.charbox.dataapi.data.mongo.TestCaseDAO;
import co.charbox.domain.model.TestCase;

public class TestCaseManager extends AbstractModelManager<TestCase, TestCaseDAO> {

	public TestCaseManager(TestCaseDAO testCaseDao) {
		super(testCaseDao);
	}

	@Override
	public int getDefualtLimit() {
		return 20; // TODO: config or setting
	}
}
