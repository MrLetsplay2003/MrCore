package me.mrletsplay.mrcore.http.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import me.mrletsplay.mrcore.http.server.impl.DefaultConnectionHandler;
import me.mrletsplay.mrcore.http.server.impl.DefaultHttpHeaderFields;
import me.mrletsplay.mrcore.http.server.impl.DefaultHttpServerHeader;
import me.mrletsplay.mrcore.io.IOUtils;

public class HttpServer {

	private ScheduledExecutorService executorService;
	private int port;
	private ServerSocket socket;
	
	private int socketTimeout;
	
	private ConnectionHandler connectionHandler;
	
	public HttpServer(int port) {
		this.executorService = Executors.newScheduledThreadPool(0);
		this.port = port;
		this.connectionHandler = new DefaultConnectionHandler();
	}
	
	public void start() throws IOException {
		this.socket = new ServerSocket(port);
		this.socket.setSoTimeout(socketTimeout);
		this.executorService.execute(() -> {
			while(true) {
				try {
					Socket clientConnection = socket.accept();
					acceptConnection(clientConnection);
				} catch (Exception e) {
					e.printStackTrace();
					connectionHandler.onConnectionFailed(e);
					continue;
				}
			}
		});
	}
	
	protected void acceptConnection(Socket socket) throws IOException {
//		HttpConnection con = connectionHandler.getConnectionById(connectionID)
//		HttpOpenPage con = new DefaultOpenPage(null, clientConnection, null);
		
		byte[] b = IOUtils.readAllBytes(socket.getInputStream());
		if(b.length == 0) return;
		HttpClientHeader header = HttpClientHeader.parse(new String(b, StandardCharsets.UTF_8));
		System.out.println(header.getCookies());
		String sID = header.getCookie("mrcore-id");
		if(sID == null) {
			sID = UUID.randomUUID().toString();
		}
		
		HttpHeaderFields hf = new DefaultHttpHeaderFields();
		
		if(header.getRawRequestedPath().equals("/")) {
			hf.set("Set-Cookie", "mrcore_id=lol");
			HttpServerHeader sH = new DefaultHttpServerHeader("HTTP/1.1", "200 OK", hf, "<h1>Hey there!</h1>".getBytes(StandardCharsets.UTF_8));
			socket.getOutputStream().write(sH.getBytes());
		}else {
			HttpServerHeader sH = new DefaultHttpServerHeader("HTTP/1.1", "404 NOT FOUND", hf, "<h1>Not found!</h1>".getBytes(StandardCharsets.UTF_8));
			socket.getOutputStream().write(sH.getBytes());
		}
		socket.getOutputStream().flush();
		socket.close();
	}
	
}
