package me.mrletsplay.mrcore.http.server.impl;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.mrcore.http.event.HttpPageBuildEvent;
import me.mrletsplay.mrcore.http.event.HttpSiteAccessedEvent;
import me.mrletsplay.mrcore.http.server.HttpConnection;
import me.mrletsplay.mrcore.http.server.HttpOpenPage;
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
	public HttpOpenPage open(HttpSiteAccessedEvent event) {
		HttpOpenPage p = event.getPageAccessed().build(new HttpPageBuildEvent(event));
		openPages.add(p);
		return p;
	}

	@Override
	public List<HttpOpenPage> getOpenPages() {
		return openPages;
	}
	
}
