package me.mrletsplay.mrcore.http.event;

import java.net.Socket;

import me.mrletsplay.mrcore.http.server.HttpClientHeader;
import me.mrletsplay.mrcore.http.server.HttpConnection;
import me.mrletsplay.mrcore.http.server.HttpPage;
import me.mrletsplay.mrcore.http.server.HttpRequestPath;
import me.mrletsplay.mrcore.http.server.HttpServer;
import me.mrletsplay.mrcore.http.server.HttpServerHeader;

public class HttpSiteAccessedEvent extends AbstractHttpEvent {

	private HttpConnection connection;
	private HttpPage pageAccessed;
	private Socket connectionSocket;
	private HttpClientHeader clientHeader;
	private HttpRequestPath requestedPath;
	private HttpServerHeader header;
	
	public HttpSiteAccessedEvent(HttpServer server, HttpPage pageAccessed, HttpConnection connection, Socket connectionSocket, HttpClientHeader clientHeader, HttpRequestPath requestedPath, HttpServerHeader header) {
		super(server);
		this.connection = connection;
		this.pageAccessed = pageAccessed;
		this.connectionSocket = connectionSocket;
		this.clientHeader = clientHeader;
		this.requestedPath = requestedPath;
		this.header = header;
	}
	
	public HttpConnection getConnection() {
		return connection;
	}
	
	public HttpPage getPageAccessed() {
		return pageAccessed;
	}
	
	public Socket getConnectionSocket() {
		return connectionSocket;
	}
	
	public HttpClientHeader getClientHeader() {
		return clientHeader;
	}
	
	public HttpRequestPath getRequestedPath() {
		return requestedPath;
	}
	
	public HttpServerHeader getHeader() {
		return header;
	}
	
}
