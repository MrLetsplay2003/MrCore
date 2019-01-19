package me.mrletsplay.mrcore.http.server;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import me.mrletsplay.mrcore.http.event.HttpSiteAccessedEvent;
import me.mrletsplay.mrcore.http.server.impl.DefaultConnectionHandler;
import me.mrletsplay.mrcore.http.server.impl.DefaultHttpHeaderFields;
import me.mrletsplay.mrcore.http.server.impl.DefaultHttpServerHeader;
import me.mrletsplay.mrcore.http.server.impl.HttpAPICallbackPage;
import me.mrletsplay.mrcore.http.server.impl.HttpFilePage;
import me.mrletsplay.mrcore.io.IOUtils;
import me.mrletsplay.mrcore.json.JSONObject;

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
		
		addPage("/internal/include.js", new HttpFilePage(new File("D:\\Programme_2\\Programmieren\\Eclipse\\Test_Alles-3\\MrCore\\src\\me\\mrletsplay\\mrcore\\http\\server\\jslib.js")));
		addPage("/internal/callback", (HttpAPICallbackPage) event -> {
			String mrCoreID = event.getClientHeader().getCookie(MrCoreHTTPConstants.MRCORE_COOKIE_NAME);
			HttpConnection connection = connectionHandler.getConnectionById(mrCoreID);
			if(connection == null) {
				JSONObject resp = new JSONObject();
				resp.set("success", false);
				resp.set("message", "Invalid/expired mrcore session id");
				return resp;
			}
			connection.getOpenPages();
			return null;
		});
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
			try {
				HttpSiteAccessedEvent e = new HttpSiteAccessedEvent(this, pg, con, socket, header, p, new DefaultHttpServerHeader("HTTP/1.1", HttpDefaultStatusCode.OK, new DefaultHttpHeaderFields()));
				HttpOpenPage page = con.open(e);
				HttpServerHeader sH = page.getHeader();
				sH.getFields().add("Set-Cookie", MrCoreHTTPConstants.MRCORE_COOKIE_NAME + "=" + sID);
//				sH.getFields().add("Set-Cookie", MrCoreHTTPConstants.MRCORE_PAGE_COOKIE_NAME + "=" + page.getPageID()); // TODO page id??????
				sH.getFields().set("Connection", "close");
				socket.getOutputStream().write(sH.getBytes(page.getBody().length));
				socket.getOutputStream().write(page.getBody());
			} catch (Exception e) {
				// TODO: Dynamic 404
				HttpServerHeader sH = new DefaultHttpServerHeader("HTTP/1.1", HttpDefaultStatusCode.INTERNAL_SERVER_ERROR, new DefaultHttpHeaderFields());
				byte[] body = "<h1>500 Internal error!</h1>".getBytes(StandardCharsets.UTF_8);
				socket.getOutputStream().write(sH.getBytes(body.length));
				socket.getOutputStream().write(body);
			}
		}else {
			// TODO: Dynamic 404
			HttpServerHeader sH = new DefaultHttpServerHeader("HTTP/1.1", HttpDefaultStatusCode.NOT_FOUND, new DefaultHttpHeaderFields());
			byte[] body = "<h1>404 Not found!</h1>".getBytes(StandardCharsets.UTF_8);
			socket.getOutputStream().write(sH.getBytes(body.length));
			socket.getOutputStream().write(body);
		}
		
		socket.getOutputStream().flush();
		socket.close();
	}
	
}
