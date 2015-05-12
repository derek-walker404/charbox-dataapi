package co.charbox.dataapi.managers;

import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.data.elasticsearch.OutageDAO;
import co.charbox.domain.model.Outage;

import com.tpofof.core.data.dao.ResultsSet;
import com.tpofof.core.managers.AbstractEsModelManager;
import com.tpofof.core.utils.Config;

@Component
public class OutageManager extends AbstractEsModelManager<Outage, OutageDAO> {

	private int defaultLimit;
	
	@Autowired
	public OutageManager(OutageDAO dao, Config config) {
		super(dao);
		this.defaultLimit = config.getInt("outage.limit", 200);
	}

	@Override
	public int getDefualtLimit() {
		return defaultLimit;
	}
	
	@Override
	public String getDefaultId() {
		return "";
	}
	
	public ResultsSet<Outage> getRecentOutages() {
		return getRecentOutages(new DateTime().minusDays(1));
	}
	
	public ResultsSet<Outage> getRecentOutages(DateTime startTime) {
		return getDao().getRecent(startTime, getDefualtLimit());
	}
	
	public ResultsSet<Outage> getRecentOutages(String deviceId, DateTime startTime) {
		return getDao().getRecent(deviceId, startTime, getDefualtLimit());
	}
	
	public ResultsSet<Outage> getRecentOutages(DateTime startTime, int limit, int offset) {
		if (limit <= 0 || offset < 0) {
			limit = getDefualtLimit();
			offset = 0;
		}
		return getDao().getRecent(startTime, getDefualtLimit());
	}
	
	public ResultsSet<Outage> getRecentOutages(String deviceId, DateTime startTime, int limit, int offset) {
		if (limit <= 0 || offset < 0) {
			limit = getDefualtLimit();
			offset = 0;
		}
		return getDao().getRecent(deviceId, startTime, getDefualtLimit());
	}

	@Override
	protected boolean hasDefualtSort() {
		return true;
	}
	
	@Override
	protected SortBuilder getSort() {
		return SortBuilders.fieldSort("outageTime").order(SortOrder.DESC);
	}
}
