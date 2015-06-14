package co.charbox.dataapi.managers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.data.elasticsearch.PingResultDAO;
import co.charbox.domain.model.PingResults;

import com.tpofof.core.data.dao.context.SimpleSort;
import com.tpofof.core.utils.Config;

@Component
public class PingResultsManager extends CharbotModelManager<PingResults, PingResultDAO> {

	private int defaultLimit;
	
	@Autowired
	public PingResultsManager(PingResultDAO dao, Config config) {
		super(dao);
		this.defaultLimit = config.getInt("ping_result.limit", 10);
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
		return true;
	}
	
	@Override
	protected SimpleSort getDefaultSort() {
		return SimpleSort.builder()
				.field("testStartTime")
				.direction(-1)
				.build();
	}
}
