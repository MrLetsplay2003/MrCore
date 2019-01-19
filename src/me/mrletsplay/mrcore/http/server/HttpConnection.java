package me.mrletsplay.mrcore.http.server;

import java.net.InetAddress;
import java.util.List;
import java.util.UUID;

import me.mrletsplay.mrcore.http.event.HttpSiteAccessedEvent;

public interface HttpConnection {
	
	public HttpServer getServer();
	
	public InetAddress getConnectionAddress();
	
	public String getID();
	
	public HttpOpenPage open(HttpSiteAccessedEvent event);
	
	public List<HttpOpenPage> getOpenPages();
	
	public default HttpOpenPage getOpenPageByID(String pageID) {
		return getOpenPages().stream().filter(o -> o.getPageID().equals(pageID)).findFirst().orElse(null);
	}
	
	public default String newPageID() {
		return UUID.randomUUID().toString();
	}
	
}
