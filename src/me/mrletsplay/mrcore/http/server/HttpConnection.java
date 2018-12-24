package me.mrletsplay.mrcore.http.server;

import java.net.InetSocketAddress;
import java.net.Socket;

public interface HttpConnection {
	
	public Socket getConnectionSocket();

	public InetSocketAddress getConnectionAddress();
	
}
