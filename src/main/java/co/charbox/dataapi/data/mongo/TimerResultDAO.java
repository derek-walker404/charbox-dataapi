package co.charbox.dataapi.data.mongo;

import co.charbox.core.data.mongo.AbstractMongoDAO;
import co.charbox.domain.model.TimerResult;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class TimerResultDAO extends AbstractMongoDAO<TimerResult> {

	public TimerResultDAO(DBCollection collection) {
		super(collection, TimerResult.class);
	}

	@Override
	protected boolean hasSort() {
		return true;
	}

	@Override
	protected DBObject getSort() {
		return new BasicDBObject("startTime", -1);
	}
}
