package me.mrletsplay.mrcore.http.event;

import java.net.Socket;

import me.mrletsplay.mrcore.http.server.HttpClientHeader;
import me.mrletsplay.mrcore.http.server.HttpPage;
import me.mrletsplay.mrcore.http.server.HttpServer;

public class HttpPageBuildEvent extends HttpSiteAccessedEvent {

	public HttpPageBuildEvent(HttpServer server, HttpPage pageAccessed, Socket connectionSocket, HttpClientHeader clientHeader) {
		super(server, pageAccessed, connectionSocket, clientHeader);
	}

}
