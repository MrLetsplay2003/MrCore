package me.mrletsplay.mrcore.http.server;

import me.mrletsplay.mrcore.misc.JSON.JSONObject;

public class HttpClientPoll {

	private HttpClientPollType type;
	private JSONObject data;
	
	private HttpClientPoll(HttpClientPollType type, JSONObject data) {
		this.type = type;
		this.data = data;
	}
	
	public HttpClientPollType getType() {
		return type;
	}
	
	public JSONObject getData() {
		return data;
	}
	
	public static HttpClientPoll redirect(String url) {
		JSONObject d = new JSONObject();
		d.put("url", url);
		return new HttpClientPoll(HttpClientPollType.REDIRECT, d);
	}
	
	public static HttpClientPoll alert(String message) {
		JSONObject d = new JSONObject();
		d.put("message", message);
		return new HttpClientPoll(HttpClientPollType.ALERT, d);
	}
	
	public static HttpClientPoll custom(String customType, JSONObject customData) {
		JSONObject d = new JSONObject();
		d.put("type", customType);
		d.put("data", customData);
		return new HttpClientPoll(HttpClientPollType.CUSTOM, d);
	}
	
}
