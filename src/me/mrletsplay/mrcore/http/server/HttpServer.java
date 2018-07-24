package me.mrletsplay.mrcore.http.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import me.mrletsplay.mrcore.http.server.html.HTMLDocument;
import me.mrletsplay.mrcore.http.server.html.HTMLDocument.HTMLBuiltDocument;
import me.mrletsplay.mrcore.http.server.html.HTMLElement;

public class HttpServer {

	private int port;
	
	private ServerSocket socket;
	private HTMLDocument page404;
	private Thread listeningThread;
	
	private Map<String, HTMLDocument> pages;
	
	private List<HttpConnection> connections;
	
	public HttpServer(int port) {
		this.port = port;
		pages = new LinkedHashMap<>();
		connections = new ArrayList<>();
		page404 = new HTMLDocument(HttpStatusCode.NOT_FOUND_404);
		page404.addElement(HTMLElement.h1("404 Not Found"));
		page404.addElement(HTMLElement.p("The URL " + HttpConstants.HTML_404_REQUESTED_URL + " could not be found on the server"));
	}
	
	public void addPage(String url, HTMLDocument page) {
		pages.put(url, page);
	}
	
	public void set404Page(HTMLDocument page404) {
		this.page404 = page404;
	}
	
	public HTMLDocument get404Page() {
		return page404;
	}
	
	public void addConnection(HttpConnection connection) {
		connections.add(connection);
	}
	
	public void disconnect(HttpConnection connection) {
		connections.remove(connection); // TODO
	}
	
	public List<HttpConnection> getActiveConnections() {
		return connections;
	}
	
	public HttpConnection getConnection(String sessionID) {
		return connections.stream().filter(c -> c.getSessionID().equals(sessionID)).findFirst().orElse(null);
	}
	
	public void start() {
		if(isRunning()) return;
		listeningThread = new Thread(() -> {
			try {
				socket = new ServerSocket(port);
				socket.setSoTimeout(1000);
				while(!Thread.interrupted() && !socket.isClosed()) {
					Socket s;
					try {
						s = socket.accept();
						s.setSoTimeout(1000);
					}catch(IOException e) {
						continue;
					}
					
					ByteArrayOutputStream clHeader = new ByteArrayOutputStream();
					byte[] buf = new byte[4096];
					int len;
					try {
						while((len = s.getInputStream().read(buf)) > 0) {
							clHeader.write(buf, 0, len);
							if(len < buf.length) break;
						}
					}catch(SocketTimeoutException e) {
						s.close();
						continue;
					}
					
					String clH = clHeader.toString();
					if(clH.trim().isEmpty()) {
						s.close();
						continue;
					}
					ClientHeader clientHeader = new ClientHeader(clH);
					
					HttpConnection hcon;
					HttpConnectionInstance con = null;
					
					if(clientHeader.getCookies().containsKey("mrcore_sessid")) {
						String sessID = clientHeader.getCookies().get("mrcore_sessid");
						hcon = getConnection(sessID);
						if(hcon==null) {
							hcon = new HttpConnection(this, s, sessID);
							addConnection(hcon);
							con = hcon.getConnections().get(0);
						}else {
							con = hcon.addConnection(s);
						}
					}else {
						hcon = new HttpConnection(this, s, UUID.randomUUID().toString());
						addConnection(hcon);
						con = hcon.getConnections().get(0);
					}
					try {
						hcon.handleRequest(clientHeader, con);
					}catch(Exception e) {
						e.printStackTrace();
						continue;
					}
				}
				socket.close();
			}catch(IOException e) {
				e.printStackTrace();
			}
		});
		listeningThread.start();
	}
	
	public void stop() {
		if(!isRunning()) return;
		try {
			if(!socket.isClosed()) socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		listeningThread.interrupt();
	}
	
	public boolean isRunning() {
		return listeningThread != null && listeningThread.isAlive();
	}
	
	protected HTMLBuiltDocument lookupURL(ParsedURL url, HttpConnectionInstance connectionInstance) {
		if(!pages.containsKey(url.getPath())) return page404.build(connectionInstance, HttpConstants.HTML_404_REQUESTED_URL, url.getPath());
		return pages.get(url.getPath()).build(connectionInstance);
	}
	
	public static class ClientHeader {
	
		private String raw;
		private String requestMethod, requestedURL;
		private Map<String, String> requestProperties, cookies;
		
		public ClientHeader(String raw) {
			this.raw = raw;
			String[] lines = raw.replace("\r", "").split("\n");
			String[] rq = lines[0].split(" ");
			requestMethod = rq[0];
			requestedURL = rq[1];
			requestProperties = new LinkedHashMap<>();
			for(int i = 1; i < lines.length; i++) {
				if(lines[i].trim().isEmpty()) continue;
				String[] line = lines[i].split(": ");
				requestProperties.put(line[0], line[1]);
			}
			cookies = new LinkedHashMap<>();
			String cookieStr = requestProperties.get("Cookie");
			if(cookieStr != null) {
				String[] cs = cookieStr.split(";");
				for(String c : cs) {
					String[] c2 = c.trim().split("=");
					cookies.put(c2[0], c2.length==2?c2[1]:null);
				}
			}
		}
		
		public String getRequestMethod() {
			return requestMethod;
		}
		
		public Map<String, String> getRequestProperties() {
			return requestProperties;
		}
		
		public Map<String, String> getCookies() {
			return cookies;
		}
		
		public String getRequestedURL() {
			return requestedURL;
		}
		
		public String getRaw() {
			return raw;
		}
		
	}
	
}
