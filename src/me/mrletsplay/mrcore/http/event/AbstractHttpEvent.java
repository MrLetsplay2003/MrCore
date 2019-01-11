package me.mrletsplay.mrcore.http.event;

import me.mrletsplay.mrcore.http.server.HttpServer;

public abstract class AbstractHttpEvent implements HttpEvent {

	private HttpServer server;
	
	public AbstractHttpEvent(HttpServer server) {
		this.server = server;
	}
	
	@Override
	public HttpServer getServer() {
		return server;
	}
	
}
