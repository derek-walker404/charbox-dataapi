package co.charbox.dataapi.managers;

import co.charbox.dataapi.data.SearchResults;
import co.charbox.dataapi.data.elasticsearch.TimerResultEsDAO;

import com.pofof.conmon.model.TimerResult;

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
