package me.mrletsplay.mrcore.http.server.impl;

import java.util.List;

import me.mrletsplay.mrcore.http.server.ConnectionHandler;
import me.mrletsplay.mrcore.http.server.HttpConnection;

public class DefaultConnectionHandler implements ConnectionHandler {

	@Override
	public void onConnectionSuccess(HttpConnection connection) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectionFailed(Exception ex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HttpConnection getConnectionById(String connectionID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<? extends HttpConnection> getActiveConnections() {
		// TODO Auto-generated method stub
		return null;
	}

}
