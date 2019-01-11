package me.mrletsplay.mrcore.http.server;

import java.net.InetAddress;
import java.util.List;

public interface ConnectionHandler {
	
	public HttpServer getServer();
	
	public HttpConnection getOrCreateConnection(InetAddress address, String connectionID);
	
	public HttpConnection getConnectionById(String connectionID);
	
	public List<? extends HttpConnection> getActiveConnections();
	
	public void onConnectionSuccess(HttpConnection connection);
	
	public void onConnectionFailed(Exception ex);
	
}
