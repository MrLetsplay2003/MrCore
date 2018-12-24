package me.mrletsplay.mrcore.http.server;

import java.util.List;

public interface ConnectionHandler {
	
	public HttpConnection getConnectionById(String connectionID);
	
	public List<? extends HttpConnection> getActiveConnections();
	
	public void onConnectionSuccess(HttpConnection connection);
	
	public void onConnectionFailed(Exception ex);
	
}
