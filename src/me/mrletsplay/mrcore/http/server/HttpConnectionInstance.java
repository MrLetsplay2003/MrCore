package me.mrletsplay.mrcore.http.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

import me.mrletsplay.mrcore.http.server.HttpServer.ClientHeader;

public class HttpConnectionInstance {

	Socket socket;
	InputStream in;
	OutputStream out;
	private Thread connectionThread;
	private HttpConnection connection;
//	HTMLBuiltDocument lastServedPage;
	
	public HttpConnectionInstance(HttpConnection connection, Socket s) throws IOException {
		this.connection = connection;
		this.socket = s;
		this.in = s.getInputStream();
		this.out = s.getOutputStream();
		this.connectionThread = new Thread(() -> {
			try {
				while(!Thread.interrupted() && !socket.isClosed()) {
					byte[] b = new byte[8192];
					int len;
					try {
						len = in.read(b);
					}catch(SocketTimeoutException e) {
						continue;
					}
					if(len < 0) break;
					ClientHeader header = new ClientHeader(new String(b));
					try {
						connection.handleRequest(header, this);
					}catch(Exception e) {
						e.printStackTrace();
						continue;
					}
				}
			}catch(Exception e) {
				e.printStackTrace();
				return;
			}
			close();
			connection.removeConnection(this);
		});
		connectionThread.start();
	}
	
	public HttpConnection getConnection() {
		return connection;
	}
	
	public void close() {
		try {
			if(!socket.isClosed()) socket.close();
			connectionThread.interrupt();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
