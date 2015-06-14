package co.charbox.dataapi.managers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.data.elasticsearch.TestCaseDAO;
import co.charbox.domain.model.TestCase;

import com.tpofof.core.utils.Config;

@Component
public class TestCaseManager extends CharbotModelManager<TestCase, TestCaseDAO> {

	private int defaultLimit;
	
	@Autowired
	public TestCaseManager(TestCaseDAO testCaseDao, Config config) {
		super(testCaseDao);
		this.defaultLimit = config.getInt("test_case.limit", 20);
	}

	@Override
	public int getDefualtLimit() {
		return defaultLimit;
	}

	@Override
	public String getDefaultId() {
		return "";
	}

	@Override
	protected boolean hasDefaultSort() {
		return false;
	}
}
