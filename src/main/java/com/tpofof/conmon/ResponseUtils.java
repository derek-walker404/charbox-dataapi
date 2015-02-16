package com.tpofof.conmon;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ResponseUtils {

	private static ObjectMapper mapper = new ObjectMapper();
	
	private ResponseUtils() { }
	
	public static JsonNode success(JsonNode content) {
		ObjectNode node = mapper.createObjectNode();
		node.put("success", true);
		node.put("status", 200);
		node.put("data", content);
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
		// TODO: implement developer/FAQ messages
		return node;
	}
}
