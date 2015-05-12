package co.charbox.dataapi.managers;

import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.data.elasticsearch.TimerResultEsDAO;
import co.charbox.domain.model.TimerResult;

import com.tpofof.core.data.dao.ResultsSet;
import com.tpofof.core.managers.AbstractEsModelManager;
import com.tpofof.core.utils.Config;

@Component
public class TimerResultManager extends AbstractEsModelManager<TimerResult, TimerResultEsDAO> {

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
		return getByDevice(deviceId, getDefualtLimit(), 0);
	}
	
	public ResultsSet<TimerResult> getByDevice(String deviceId, int limit, int offset) {
		return getDao().getByDevice(deviceId, limit, offset);
	}

	@Override
	protected boolean hasDefualtSort() {
		return true;
	}
	
	@Override
	public SortBuilder getSort() {
		return SortBuilders.fieldSort("startTime").order(SortOrder.DESC);
	}
}
