package me.mrletsplay.mrcore.http.event;

import java.net.Socket;

import me.mrletsplay.mrcore.http.server.HttpClientHeader;
import me.mrletsplay.mrcore.http.server.HttpConnection;
import me.mrletsplay.mrcore.http.server.HttpDefaultStatusCode;
import me.mrletsplay.mrcore.http.server.HttpPage;
import me.mrletsplay.mrcore.http.server.HttpServer;
import me.mrletsplay.mrcore.http.server.HttpServerHeader;
import me.mrletsplay.mrcore.http.server.impl.DefaultHttpHeaderFields;
import me.mrletsplay.mrcore.http.server.impl.DefaultHttpServerHeader;

public class HttpSiteAccessedEvent extends AbstractHttpEvent {

	private HttpServerHeader header;
	private HttpPage pageAccessed;
	private HttpConnection connection;
	private Socket connectionSocket;
	private HttpClientHeader clientHeader;
	
	public HttpSiteAccessedEvent(HttpServer server, HttpPage pageAccessed, Socket connectionSocket, HttpClientHeader clientHeader) {
		super(server);
		this.header = new DefaultHttpServerHeader("HTTP/1.1", HttpDefaultStatusCode.OK, new DefaultHttpHeaderFields());
		this.pageAccessed = pageAccessed;
	}
	
	public HttpServerHeader getHeader() {
		return header;
	}

	public HttpPage getPageAccessed() {
		return pageAccessed;
	}
	
	public HttpConnection getConnection() {
		return connection;
	}
	
	public Socket getConnectionSocket() {
		return connectionSocket;
	}
	
	public HttpClientHeader getClientHeader() {
		return clientHeader;
	}
	
}
