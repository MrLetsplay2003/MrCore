package me.mrletsplay.mrcore.http.server;

import java.net.Socket;

import me.mrletsplay.mrcore.http.html.HTMLDocument;

public interface HttpOpenPage {

	public HttpConnection getConnection();

	public Socket getConnectionSocket();
	
	public HTMLDocument getDocument();
	
}
