package co.charbox.dataapi.data.elasticsearch;

import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.domain.model.TestCase;

import com.tpofof.core.data.dao.es.AbstractElasticsearchDAO;
import com.tpofof.core.io.IO;
import com.tpofof.core.utils.Config;

@Component
public class TestCaseDAO extends AbstractElasticsearchDAO<TestCase> {
	
	private IO io;
	private String index;
	private String type;
	
	@Autowired
	public TestCaseDAO(Config config, Client client, IO io) {
		super(config, client);
		this.io = io;
		init();
	}

	@Override
	protected String getIndex() {
		if (index == null) {
			index = getConfig().getString("es.testcase.index", "charbot_v0.1_tc");
		}
		return index;
	}

	@Override
	protected String getType() {
		if (type == null) {
			type = getConfig().getString("es.testcase.type", "tc");
		}
		return type;
	}

	@Override
	protected Class<TestCase> getModelClass() {
		return TestCase.class;
	}

	@Override
	protected boolean isRequiredIndex() {
		return true;
	}
	
	@Override
	protected String getMapping() {
		String filename = getConfig().getString("es.testcase.mapping.name", "mappings/es.testcase.mapping.json");
		return io.getContents(filename);
	}
}
