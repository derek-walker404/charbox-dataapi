package com.tpofof.conmon.server.managers;

import java.util.List;

import com.pofof.conmon.model.TimerResult;
import com.tpofof.conmon.server.mongo.TimerResultDAO;

public class TimerResultManager implements GenericModelManager<TimerResult> {

	private final TimerResultDAO dao;
	
	public TimerResultManager(TimerResultDAO dao) {
		this.dao = dao;
	}
	
	public TimerResult find(String id) {
		return dao.find(id);
	}

	public List<TimerResult> find() {
		return find(20, 0);
	}

	public List<TimerResult> find(int limit, int offset) {
		return dao.find(limit, offset);
	}

	public TimerResult insert(TimerResult model) {
		return dao.insert(model);
	}

	public TimerResult update(TimerResult model) {
		return dao.update(model);
	}

	public boolean delete(String id) {
		return dao.delete(id);
	}

}
