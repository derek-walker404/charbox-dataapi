package com.tpofof.conmon.server.mongo;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.tpofof.utils.JsonUtils;

public class GenericMongoDAO<ModelT> {

	private final DBCollection collection;
	private final Class<ModelT> modelClass;
	private final Set<String> requiredFields;
	
	public GenericMongoDAO(DBCollection collection, Class<ModelT> modelClass, String...requiredFields) {
		this.collection = collection;
		this.modelClass = modelClass;
		Set<String> temp = Sets.newConcurrentHashSet();
		for (String s : requiredFields) {
			temp.add(s);
		}
		this.requiredFields = Collections.unmodifiableSet(temp);
	}
	
	protected final DBCollection getCollection() {
		return collection;
	}
	
	protected final Set<String> getRequiredFields() {
		return requiredFields;
	}
	
	public List<ModelT> find(int limit, int offset) {
		DBCursor result = getCollection().find().limit(limit).skip(offset);
		List<ModelT> cases = Lists.newArrayList();
		while (result.hasNext()) {
			ModelT temp = convert(result.next());
			if (temp != null) {
				cases.add(temp);
			}
		}
		return cases;
	}
	
	public ModelT find(String id) {
		try {
			return convert(collection.findOne(new ObjectId(id)));
		} catch (IllegalArgumentException e) {
			// do nothing when invalid id's are provided
			// TODO: need to log? probably not
		}
		return null;
	}
	
	public ModelT insert(ModelT model) {
		ObjectId expectedId = new ObjectId();
		DBObject inserObject = (DBObject)JSON.parse(JsonUtils.toJson(model));
		inserObject.put("_id", expectedId);
		if (validateRequiredFields(inserObject)) {
			getCollection().insert(inserObject);
		}
		return find(expectedId.toString());
	}
	
	protected ModelT convert(DBObject obj) {
		if (obj == null) {
			return null;
		}
		ObjectId id = (ObjectId) obj.get("_id");
		obj.put("_id", id.toString());
		return JsonUtils.fromJson(JSON.serialize(obj), modelClass);
	}
	
	protected boolean validateRequiredFields(DBObject obj) {
		Set<String> keys = obj.keySet();
		for (String fieldName : requiredFields) {
			if (!keys.contains(fieldName)) {
				return false;
			}
		}
		return true;
	}
}
