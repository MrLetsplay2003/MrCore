package me.mrletsplay.mrcore.http.server;

import java.net.Socket;

public interface HttpOpenPage {

	public HttpConnection getConnection();

	public Socket getConnectionSocket();
	
	public HttpPage getPage();
	
	public HttpServerHeader getHeader();
	
	public byte[] getBody();
	
}
