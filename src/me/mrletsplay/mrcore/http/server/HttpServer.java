package me.mrletsplay.mrcore.http.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import me.mrletsplay.mrcore.http.server.HTML.HTMLDocument;
import me.mrletsplay.mrcore.http.server.HTML.HTMLElement;
import me.mrletsplay.mrcore.http.server.js.JSFunctionCallable;
import me.mrletsplay.mrcore.http.server.js.JSFunctionCallable.JSFunctionCalledEvent;
import me.mrletsplay.mrcore.http.server.js.JSParsedFunction;
import me.mrletsplay.mrcore.http.server.js.JSScript;

public class HttpServer {

	private ServerSocket socket;
	private HTMLDocument page404;
	private Thread listeningThread;
	
	private Map<String, HTMLDocument> pages;
	
	private List<HttpConnection> connections;
	
	public HttpServer() {
		pages = new HashMap<>();
		connections = new ArrayList<>();
		page404 = new HTMLDocument();
		page404.addElement(new HTMLElement("h1", "404 Not Found"));
		page404.addElement(new HTMLElement("p", "The URL {requested_url} could not be found on the server"));
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
		
	}
	
	public List<HttpConnection> getActiveConnections() {
		return connections;
	}
	
	public void start() {
		if(isRunning()) return;
		listeningThread = new Thread(() -> {
			try {
				socket = new ServerSocket(9090);
				socket.setSoTimeout(1000);
				while(!Thread.interrupted()) {
					Socket s;
					try {
						s = socket.accept();
					}catch(IOException e) {
						continue;
					}
					
					ByteArrayOutputStream clHeader = new ByteArrayOutputStream();
					byte[] buf = new byte[4096];
					int len;
					while((len = s.getInputStream().read(buf)) > 0) {
						clHeader.write(buf, 0, len);
						if(len < buf.length) break;
					}
					
					String clH = clHeader.toString();
					if(clH.trim().isEmpty()) {
						// TODO: Disconnect
						return;
					}
					ClientHeader clientHeader = new ClientHeader(clH);
					
					HttpConnection con = null;
					
					if(clientHeader.getCookies().containsKey("mrcore_sessid")) {
						String sessID = clientHeader.getCookies().get("mrcore_sessid");
						con = connections.stream().filter(c -> c.getSessionID().equals(sessID)).findFirst().orElse(null);
						if(con==null) {
							con = new HttpConnection(this, s, sessID);
							addConnection(con);
						}
					}else {
						con = new HttpConnection(this, s, UUID.randomUUID().toString());
						addConnection(con);
					}
					System.out.println("sdkljf");
					con.handleRequest(clientHeader);
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
		listeningThread.interrupt();
	}
	
	public boolean isRunning() {
		return listeningThread != null && listeningThread.isAlive();
	}
	
	protected HTMLDocument lookupURL(ParsedURL url, JSScript script) {
		if(url.getPath().startsWith("/_internals/")) {
			String iName = url.getPath().substring("/_internals/".length());
			if(script!=null && iName.equals("callback")) {
				JSParsedFunction parsedF = script.getFunction(url.getGetParameters().get("function_name"));
				if(parsedF != null && parsedF.getRaw() instanceof JSFunctionCallable) {
					((JSFunctionCallable) parsedF.getRaw()).invoke(new JSFunctionCalledEvent(this));
				}
				return new HTMLDocument();
			}
		}
		if(!pages.containsKey(url.getPath())) return null;
		return pages.get(url.getPath());
	}
	
	public static class ClientHeader {
	
		private String raw;
		private String requestMethod, requestedURL;
		private Map<String, String> requestProperties, cookies;
		
		public ClientHeader(String raw) {
			this.raw = raw;
			String[] lines = raw.split("\r\n");
			String[] rq = lines[0].split(" ");
			requestMethod = rq[0];
			requestedURL = rq[1];
			requestProperties = new HashMap<>();
			for(int i = 1; i < lines.length; i++) {
				String[] line = lines[i].split(": ");
				requestProperties.put(line[0], line[1]);
			}
			cookies = new HashMap<>();
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
