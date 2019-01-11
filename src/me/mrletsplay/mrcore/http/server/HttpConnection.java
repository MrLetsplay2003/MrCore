package me.mrletsplay.mrcore.http.server;

import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

public interface HttpConnection {
	
	public HttpServer getServer();
	
	public InetAddress getConnectionAddress();
	
	public String getID();
	
	public HttpOpenPage open(HttpPage page, Socket socket, HttpClientHeader clientHeader);
	
	public List<? extends HttpOpenPage> getOpenPages();
	
}
