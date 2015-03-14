package com.tpofof.conmon.server.managers;

import com.pofof.conmon.model.TimerResult;
import com.tpofof.conmon.server.data.SearchResults;
import com.tpofof.conmon.server.data.elasticsearch.TimerResultEsDAO;

public class TimerResultManager extends AbstractModelManager<TimerResult, TimerResultEsDAO> {

	public TimerResultManager(TimerResultEsDAO esDao) {
		super(esDao);
	}

	/**
	 * NOT SUPPORTED
	 */
	@Deprecated
	@Override
	public TimerResult update(TimerResult model) {
		return null;
	}

	/**
	 * NOT SUPPORTED
	 */
	@Deprecated
	@Override
	public boolean delete(String id) {
		return false;
	}

	@Override
	public int getDefualtLimit() {
		return 20;
	}
	
	public SearchResults<TimerResult> getByDevice(long deviceId) {
		return getByDevice(deviceId, getDefualtLimit(), 0);
	}
	
	public SearchResults<TimerResult> getByDevice(long deviceId, int limit, int offset) {
		return getDao().getByDevice(deviceId, limit, offset);
	}
}
