package me.mrletsplay.mrcore.http.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
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
	
	private Map<String, HttpPage> pages;
	private ConnectionHandler connectionHandler;
	
	public HttpServer(int port) {
		this.pages = new HashMap<>();
		this.executorService = Executors.newScheduledThreadPool(1);
		this.port = port;
		this.connectionHandler = new DefaultConnectionHandler(this);
	}
	
	public void setExecutorService(ScheduledExecutorService executorService) {
		this.executorService = executorService;
	}
	
	public void setConnectionHandler(ConnectionHandler connectionHandler) {
		this.connectionHandler = connectionHandler;
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
	
	public void addPage(String path, HttpPage page) {
		pages.put(path, page);
	}
	
	protected void acceptConnection(Socket socket) throws IOException {
//		HttpConnection con = connectionHandler.getConnectionById(connectionID)
//		HttpOpenPage con = new DefaultOpenPage(null, clientConnection, null);
		byte[] b = IOUtils.readBytesUntilUnavailable(socket.getInputStream());
		if(b.length == 0) return;
		HttpClientHeader header = HttpClientHeader.parse(new String(b, StandardCharsets.UTF_8));
		String sID = header.getCookie("mrcore-id");
		if(sID == null) sID = UUID.randomUUID().toString();
		
		HttpConnection con = connectionHandler.getOrCreateConnection(socket.getInetAddress(), sID);
		HttpRequestPath p = HttpRequestPath.parse(header.getRawRequestedPath());
		System.out.println("REQUEST: " + p.getPath());
		HttpPage pg = pages.get(p.getPath());
		
		if(pg != null) {
			HttpOpenPage page = con.open(pg, socket, header);
			HttpServerHeader sH = page.getHeader();
			sH.getFields().add("Set-Cookie", "mrcore-id=" + sID);
			sH.getFields().set("Connection", "close");
			socket.getOutputStream().write(sH.getBytes(page.getBody().length));
			socket.getOutputStream().write(page.getBody());
		}else {
			HttpServerHeader sH = new DefaultHttpServerHeader("HTTP/1.1", HttpDefaultStatusCode.NOT_FOUND, new DefaultHttpHeaderFields());
			byte[] body = "<h1>404 Not found!</h1>".getBytes(StandardCharsets.UTF_8);
			socket.getOutputStream().write(sH.getBytes(body.length));
			socket.getOutputStream().write(body);
		}
		
		socket.getOutputStream().flush();
		socket.close();
	}
	
}
