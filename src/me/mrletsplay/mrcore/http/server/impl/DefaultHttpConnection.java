package me.mrletsplay.mrcore.http.server.impl;

import java.net.InetSocketAddress;
import java.net.Socket;

import me.mrletsplay.mrcore.http.server.HttpConnection;

public class DefaultHttpConnection implements HttpConnection {

	private InetSocketAddress address;
	
	public DefaultHttpConnection(InetSocketAddress address) {
		this.address = address;
	}

	@Override
	public InetSocketAddress getConnectionAddress() {
		return address;
	}

	@Override
	public Socket getConnectionSocket() {
		return null;
	}
	
}
