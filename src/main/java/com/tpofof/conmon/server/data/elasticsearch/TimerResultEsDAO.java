package com.tpofof.conmon.server.data.elasticsearch;

import org.elasticsearch.client.Client;

import com.pofof.conmon.model.TimerResult;

public class TimerResultEsDAO extends GenericElasticsearchDAO<TimerResult> {

//	private final DateTimeFormatter DATE_FORMAT = DateTimeFormat.forPattern("yyyy/MM/dd HH:mm:ss");
	
	public TimerResultEsDAO(Client client) {
		super(client, TimerResult.class);
	}

	@Override
	protected String getType() {
		return "timerResult";
	}
	
//	protected String convert(TimerResult model) {
//		DateTime startTime = new DateTime(model.getStartTime());
//		ObjectNode node = JsonUtils.parseObject(JsonUtils.toJson(model));
//		node.put("startTime", startTime.toString(DATE_FORMAT));
//		return node.toString();
//	}
//	
//	protected TimerResult convert(String jsonContent) {
//		ObjectNode obj = JsonUtils.parseObject(jsonContent);
//		obj.put("startTime", new DateTime(obj.get("startTime").asText()).toDate().getTime());
//		return JsonUtils.fromJson(jsonContent, TimerResult.class);
//	}
}
