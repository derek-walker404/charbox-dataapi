package com.tpofof.conmon.server.managers;

import java.util.List;

import com.pofof.conmon.model.TimerResult;
import com.tpofof.conmon.server.data.elasticsearch.TimerResultEsDAO;
import com.tpofof.conmon.server.data.mongo.TimerResultDAO;

public class TimerResultManager implements GenericModelManager<TimerResult> {

	private final TimerResultDAO dao;
	private final TimerResultEsDAO esDao;
	
	public TimerResultManager(TimerResultDAO dao, TimerResultEsDAO esDao) {
		this.dao = dao;
		this.esDao = esDao;
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
	
	public long count() {
		return dao.count();
	}

	public TimerResult insert(TimerResult model) {
		esDao.insert(model);
		return dao.insert(model);
	}

	public TimerResult update(TimerResult model) {
		// TODO: probably shouldn't support this.
		return dao.update(model);
	}

	public boolean delete(String id) {
		// TODO: probably shouldn't support this.
		return dao.delete(id);
	}

}
