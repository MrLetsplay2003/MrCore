package me.mrletsplay.mrcore.http.server.impl;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.mrcore.http.server.ConnectionHandler;
import me.mrletsplay.mrcore.http.server.HttpConnection;
import me.mrletsplay.mrcore.http.server.HttpServer;

public class DefaultConnectionHandler implements ConnectionHandler {

	private HttpServer server;
	private List<HttpConnection> openConnections;
	
	public DefaultConnectionHandler(HttpServer server) {
		this.server = server;
		this.openConnections = new ArrayList<>();
	}
	
	@Override
	public HttpServer getServer() {
		return server;
	}
	
	@Override
	public HttpConnection getOrCreateConnection(InetAddress address, String connectionID) {
		HttpConnection con = getConnectionById(connectionID);
		if(con == null) {
			con = new DefaultHttpConnection(server, address, connectionID);
			openConnections.add(con);
		}
		return con;
	}
	
	@Override
	public void onConnectionSuccess(HttpConnection connection) {
		openConnections.add(connection);
	}

	@Override
	public void onConnectionFailed(Exception ex) {
		ex.printStackTrace();
	}

	@Override
	public HttpConnection getConnectionById(String connectionID) {
		return openConnections.stream().filter(c -> c.getID().equals(connectionID)).findFirst().orElse(null);
	}

	@Override
	public List<? extends HttpConnection> getActiveConnections() {
		return openConnections;
	}

}
