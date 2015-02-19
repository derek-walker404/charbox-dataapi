package com.tpofof.conmon.server.resources;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tpofof.utils.JsonUtils;

public class ResponseUtils {

	private static ObjectMapper mapper = new ObjectMapper();
	
	private ResponseUtils() { }
	
	public static JsonNode success(JsonNode content) {
		ObjectNode node = mapper.createObjectNode();
		node.put("success", true);
		node.put("status", 200);
		if (content != null) {
			node.put("data", content);
		}
		return node;
	}
	
	public static JsonNode failure() {
		return failure("Something went wrong. Try again later.");
	}
	
	public static JsonNode failure(String message) {
		return failure(message, 500);
	}
	
	public static JsonNode failure(String message, int code) {
		ObjectNode node = mapper.createObjectNode();
		node.put("success", false);
		node.put("status", code);
		node.put("error", message);
		// TODO: implement developer/FAQ messages
		return node;
	}
	
	public static JsonNode listData(List<?> content, int limit, int offset) {
		ObjectNode node = mapper.createObjectNode();
		node.put("type", "collection");
		ArrayNode contentArray = mapper.createArrayNode();
		for (Object obj : content) {
			contentArray.add(JsonUtils.toJsonNode(obj));
		}
		node.put("collection", contentArray);
		node.put("count", content.size());
		ObjectNode pagingNode = mapper.createObjectNode();
		boolean hasMore = content.size() == limit;
		pagingNode.put("hasMore", hasMore);
		pagingNode.put("limit", hasMore ? limit : -1);
		pagingNode.put("offset", hasMore ? (offset + limit) : -1);
		node.put("next", pagingNode);
		return node;
	}
	
	public static JsonNode modelData(Object content) {
		ObjectNode node = mapper.createObjectNode();
		node.put("type", "model");
		node.put("model", JsonUtils.toJsonNode(content));
		return node;
	}
}
