package com.tpofof.conmon.server.managers;

import org.joda.time.DateTime;

import com.pofof.conmon.model.Outage;
import com.tpofof.conmon.server.data.SearchResults;
import com.tpofof.conmon.server.data.mongo.OutageDAO;

public class OutageManager extends AbstractModelManager<Outage, OutageDAO> {

	public OutageManager(OutageDAO dao) {
		super(dao);
	}

	@Override
	public int getDefualtLimit() {
		return 200;
	}
	
	public SearchResults<Outage> getRecentOutages() {
		return getRecentOutages(new DateTime().minusDays(1));
	}
	
	public SearchResults<Outage> getRecentOutages(DateTime startTime) {
		return getDao().getRecent(startTime, getDefualtLimit());
	}
	
	public SearchResults<Outage> getRecentOutages(String deviceId, DateTime startTime) {
		return getDao().getRecent(deviceId, startTime, getDefualtLimit());
	}
	
	public SearchResults<Outage> getRecentOutages(DateTime startTime, int limit, int offset) {
		if (limit <= 0 || offset < 0) {
			limit = getDefualtLimit();
			offset = 0;
		}
		return getDao().getRecent(startTime, getDefualtLimit());
	}
	
	public SearchResults<Outage> getRecentOutages(String deviceId, DateTime startTime, int limit, int offset) {
		if (limit <= 0 || offset < 0) {
			limit = getDefualtLimit();
			offset = 0;
		}
		return getDao().getRecent(deviceId, startTime, getDefualtLimit());
	}
}
