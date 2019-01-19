package me.mrletsplay.mrcore.http.server;

import java.net.Socket;

import me.mrletsplay.mrcore.http.server.HttpOpenPage;

public class HttpOpenPage {

	private HttpConnection connection;
	private Socket connectionSocket;
	private String pageID;
	private HttpPage page;
	private HttpServerHeader header;
	private byte[] body;
	
	public HttpOpenPage(HttpConnection connection, Socket connectionSocket, String pageID, HttpPage page, HttpServerHeader header, byte[] body) {
		this.connection = connection;
		this.connectionSocket = connectionSocket;
		this.pageID = pageID;
		this.page = page;
		this.header = header;
		this.body = body;
	}
	
	public HttpConnection getConnection() {
		return connection;
	}
	
	public Socket getConnectionSocket() {
		return connectionSocket;
	}
	
	public String getPageID() {
		return pageID;
	}
	
	public HttpPage getPage() {
		return page;
	}
	
	public HttpServerHeader getHeader() {
		return header;
	}
	
	public byte[] getBody() {
		return body;
	}

}
