package co.charbox.dataapi.managers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.data.elasticsearch.TimerResultEsDAO;
import co.charbox.domain.model.TimerResult;

import com.tpofof.core.data.dao.ResultsSet;
import com.tpofof.core.data.dao.context.SearchWindow;
import com.tpofof.core.data.dao.context.SimpleSort;
import com.tpofof.core.utils.Config;

@Component
public class TimerResultManager extends CharbotModelManager<TimerResult, TimerResultEsDAO> {

	private int defaultLimit;
	
	@Autowired
	public TimerResultManager(TimerResultEsDAO esDao, Config config) {
		super(esDao);
		this.defaultLimit = config.getInt("timer_result.limit", 20);
	}
	
	@Override
	public int getDefualtLimit() {
		return defaultLimit;
	}
	
	@Override
	public String getDefaultId() {
		return "";
	}

	public ResultsSet<TimerResult> getByDevice(String deviceId) {
		return getByDevice(deviceId, getDefualtWindow(), getDefaultSort());
	}
	
	public ResultsSet<TimerResult> getByDevice(String deviceId, SearchWindow window, SimpleSort sort) {
		return getDao().getByDevice(deviceId, window, sort);
	}

	@Override
	protected boolean hasDefaultSort() {
		return true;
	}
	
	@Override
	public SimpleSort getDefaultSort() {
		return SimpleSort.builder()
				.field("startTime")
				.direction(-1)
				.build();
	}
}
