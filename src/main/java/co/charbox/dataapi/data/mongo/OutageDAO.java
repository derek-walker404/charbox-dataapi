package co.charbox.dataapi.data.mongo;

import java.util.List;

import org.joda.time.DateTime;

import co.charbox.core.data.SearchResults;
import co.charbox.core.data.mongo.AbstractMongoDAO;
import co.charbox.domain.model.Outage;

import com.google.api.client.util.Lists;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class OutageDAO extends AbstractMongoDAO<Outage> {

	public OutageDAO(DBCollection collection) {
		super(collection, Outage.class);
	}
	
	@Override
	protected boolean hasSort() {
		return true;
	}
	
	@Override
	protected DBObject getSort() {
		return new BasicDBObject("outageTime", -1);
	}
	
	public SearchResults<Outage> getRecent(DateTime startTime, int limit) {
		return getRecent(null, startTime, limit);
	}
	
	public SearchResults<Outage> getRecent(String deviceId, DateTime startTime, int limit) {
		BasicDBObject query = new BasicDBObject("outageTime", startTime);
		if (deviceId != null) {
			query.append("deviceId", deviceId);
		}
		DBCursor rawResults = getCollection()
				.find(query)
				.sort(getSort())
				.limit(limit);
		List<Outage> outages = Lists.newArrayList();
		while (rawResults.hasNext()) {
			outages.add(convert(rawResults.next()));
		}
		return new SearchResults<Outage>()
				.setLimit(limit)
				.setOffset(0)
				.setResults(outages);
	}
}
