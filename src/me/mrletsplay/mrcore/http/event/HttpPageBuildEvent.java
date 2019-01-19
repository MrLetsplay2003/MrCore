package me.mrletsplay.mrcore.http.event;

import java.net.Socket;

import me.mrletsplay.mrcore.http.server.HttpClientHeader;
import me.mrletsplay.mrcore.http.server.HttpConnection;
import me.mrletsplay.mrcore.http.server.HttpPage;
import me.mrletsplay.mrcore.http.server.HttpRequestPath;
import me.mrletsplay.mrcore.http.server.HttpServer;
import me.mrletsplay.mrcore.http.server.HttpServerHeader;

public class HttpPageBuildEvent extends HttpSiteAccessedEvent {
	
	public HttpPageBuildEvent(HttpServer server, HttpPage pageAccessed, HttpConnection connection,
			Socket connectionSocket, HttpClientHeader clientHeader, HttpRequestPath requestedPath,
			HttpServerHeader header) {
		super(server, pageAccessed, connection, connectionSocket, clientHeader, requestedPath, header);
	}

	public HttpPageBuildEvent(HttpSiteAccessedEvent accessedEvent) {
		this(accessedEvent.getServer(), accessedEvent.getPageAccessed(), accessedEvent.getConnection(), accessedEvent.getConnectionSocket(), accessedEvent.getClientHeader(), accessedEvent.getRequestedPath(), accessedEvent.getHeader());
	}

	public HttpPageBuildEvent(HttpPageBuildEvent accessedEvent) {
		this(accessedEvent.getServer(), accessedEvent.getPageAccessed(), accessedEvent.getConnection(), accessedEvent.getConnectionSocket(), accessedEvent.getClientHeader(), accessedEvent.getRequestedPath(), accessedEvent.getHeader());
	}
	
}
