package co.charbox.dataapi.managers;

import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tpofof.core.managers.AbstractModelManager;
import com.tpofof.core.utils.Config;

import co.charbox.dataapi.data.elasticsearch.TestCaseDAO;
import co.charbox.domain.model.TestCase;

@Component
public class TestCaseManager extends AbstractModelManager<TestCase, String, TestCaseDAO, QueryBuilder> {

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
}
