package me.mrletsplay.mrcore.http.server.impl;

import java.net.Socket;

import me.mrletsplay.mrcore.http.server.HttpConnection;
import me.mrletsplay.mrcore.http.server.HttpOpenPage;
import me.mrletsplay.mrcore.http.server.HttpPage;
import me.mrletsplay.mrcore.http.server.HttpServerHeader;

public class DefaultOpenPage implements HttpOpenPage {

	private HttpConnection connection;
	private Socket connectionSocket;
	private HttpPage page;
	private HttpServerHeader header;
	private byte[] body;
	
	public DefaultOpenPage(HttpConnection connection, Socket connectionSocket, HttpPage page, HttpServerHeader header, byte[] body) {
		this.connection = connection;
		this.connectionSocket = connectionSocket;
		this.page = page;
		this.header = header;
		this.body = body;
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
	public HttpPage getPage() {
		return page;
	}
	
	@Override
	public HttpServerHeader getHeader() {
		return header;
	}
	
	@Override
	public byte[] getBody() {
		return body;
	}

}
