package co.charbox.dataapi.managers;

import org.elasticsearch.index.query.QueryBuilder;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tpofof.core.data.dao.ResultsSet;
import com.tpofof.core.managers.AbstractModelManager;
import com.tpofof.core.utils.Config;

import co.charbox.dataapi.data.elasticsearch.OutageDAO;
import co.charbox.domain.model.Outage;

@Component
public class OutageManager extends AbstractModelManager<Outage, String, OutageDAO, QueryBuilder> {

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
}
