package me.mrletsplay.mrcore.http.server.impl;

import java.nio.charset.StandardCharsets;

import me.mrletsplay.mrcore.http.event.HttpPageBuildEvent;
import me.mrletsplay.mrcore.http.server.HttpOpenPage;
import me.mrletsplay.mrcore.http.server.HttpPage;
import me.mrletsplay.mrcore.json.JSONObject;

@FunctionalInterface
public interface HttpAPICallbackPage extends HttpPage {

	public JSONObject callback(HTTPAPICallbackEvent event);
	
	@Override
	public default HttpOpenPage build(HttpPageBuildEvent event) {
		event.getHeader().getFields().set("Content-Type", "application/json");
		byte[] body = callback(new HTTPAPICallbackEvent(event)).toString().getBytes(StandardCharsets.UTF_8);
		return new HttpOpenPage(event.getConnection(), event.getConnectionSocket(), event.getConnection().newPageID(), this, event.getHeader(), body);
	}
	
	public static class HTTPAPICallbackEvent extends HttpPageBuildEvent {

		public HTTPAPICallbackEvent(HttpPageBuildEvent event) {
			super(event);
		}
		
	}
	
}
