package me.mrletsplay.mrcore.http.server.impl;

import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.mrcore.http.event.HttpPageBuildEvent;
import me.mrletsplay.mrcore.http.server.HttpClientHeader;
import me.mrletsplay.mrcore.http.server.HttpConnection;
import me.mrletsplay.mrcore.http.server.HttpOpenPage;
import me.mrletsplay.mrcore.http.server.HttpPage;
import me.mrletsplay.mrcore.http.server.HttpServer;

public class DefaultHttpConnection implements HttpConnection {

	private HttpServer server;
	private InetAddress address;
	private String id;
	private List<HttpOpenPage> openPages;
	
	public DefaultHttpConnection(HttpServer server, InetAddress address, String id) {
		this.server = server;
		this.address = address;
		this.id = id;
		this.openPages = new ArrayList<>();
	}
	
	@Override
	public HttpServer getServer() {
		return server;
	}

	@Override
	public InetAddress getConnectionAddress() {
		return address;
	}
	
	@Override
	public String getID() {
		return id;
	}

	@Override
	public HttpOpenPage open(HttpPage page, Socket socket, HttpClientHeader clientHeader) {
		HttpOpenPage p = page.build(new HttpPageBuildEvent(server, page, socket, clientHeader));
		openPages.add(p);
		return p;
	}

	@Override
	public List<? extends HttpOpenPage> getOpenPages() {
		return openPages;
	}
	
}
