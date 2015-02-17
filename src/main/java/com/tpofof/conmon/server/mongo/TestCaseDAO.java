package com.tpofof.conmon.server.mongo;

import java.util.List;

import org.bson.types.ObjectId;

import com.google.common.collect.Lists;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.pofof.conmon.model.TestCase;
import com.tpofof.utils.JsonUtils;

public class TestCaseDAO {

	private DBCollection testCaseCollection;
	
	public TestCaseDAO(DBCollection testCaseCollection) {
		this.testCaseCollection = testCaseCollection;
	}
	
	public List<TestCase> getTestCases(int limit, int offset) {
		DBCursor result = testCaseCollection.find().limit(limit).skip(offset);
		List<TestCase> cases = Lists.newArrayList();
		while (result.hasNext()) {
			TestCase temp = convert(result.next());
			if (temp != null) {
				cases.add(temp);
			}
		}
		return cases;
	}
	
	public TestCase getTestCase(String id) {
		return convert(testCaseCollection.findOne(new ObjectId(id)));
	}
	
	public TestCase postTestCase(TestCase tc) {
		ObjectId expectedId = new ObjectId();
		DBObject inserObject = (DBObject)JSON.parse(JsonUtils.toJson(tc));
		inserObject.put("_id", expectedId);
		testCaseCollection.insert(inserObject);
		return getTestCase(expectedId.toString());
	}
	
	private TestCase convert(DBObject obj) {
		if (obj == null) {
			return null;
		}
		ObjectId id = (ObjectId) obj.get("_id");
		obj.put("_id", id.toString());
		return JsonUtils.fromJson(JSON.serialize(obj), TestCase.class);
	}
}
