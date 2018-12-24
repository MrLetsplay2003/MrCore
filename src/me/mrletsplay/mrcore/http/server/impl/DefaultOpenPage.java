package me.mrletsplay.mrcore.http.server.impl;

import java.net.Socket;

import me.mrletsplay.mrcore.http.html.HTMLDocument;
import me.mrletsplay.mrcore.http.server.HttpConnection;
import me.mrletsplay.mrcore.http.server.HttpOpenPage;

public class DefaultOpenPage implements HttpOpenPage {

	private HttpConnection connection;
	private Socket connectionSocket;
	private HTMLDocument document;
	
	public DefaultOpenPage(HttpConnection connection, Socket connectionSocket, HTMLDocument document) {
		this.connection = connection;
		this.connectionSocket = connectionSocket;
		this.document = document;
	}
	
	@Override
	public HttpConnection getConnection() {
		return connection;
	}
	
	@Override
	public Socket getConnectionSocket() {
		return connectionSocket;
	}
	
	@Override
	public HTMLDocument getDocument() {
		return document;
	}

}
