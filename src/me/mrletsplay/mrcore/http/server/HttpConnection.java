package me.mrletsplay.mrcore.http.server;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import me.mrletsplay.mrcore.http.server.HttpServer.ClientHeader;
import me.mrletsplay.mrcore.http.server.InternalPage.InternalResult;
import me.mrletsplay.mrcore.http.server.html.HTMLDocument;
import me.mrletsplay.mrcore.http.server.html.built.HTMLBuiltDocument;
import me.mrletsplay.mrcore.http.server.js.JSFunction;
import me.mrletsplay.mrcore.http.server.js.JSFunction.JSFunctionInvokedEvent;
import me.mrletsplay.mrcore.http.server.js.JSFunctionCallable;
import me.mrletsplay.mrcore.http.server.js.JSFunctionConsumingCallable;
import me.mrletsplay.mrcore.http.server.js.JSFunctionConsumingCallable.JSFunctionConsumingInvokedEvent;
import me.mrletsplay.mrcore.http.server.js.built.JSBuiltFunction;
import me.mrletsplay.mrcore.main.MrCore;
import me.mrletsplay.mrcore.misc.JSON.JSONArray;
import me.mrletsplay.mrcore.misc.JSON.JSONObject;

public class HttpConnection {

	private HttpServer server;
	private String sessID;
	private List<HttpConnectionInstance> connections;
	private List<HttpClientPoll> polls;
	private HTMLBuiltDocument lastServedPage;
	private String host;
	
	public HttpConnection(HttpServer server, Socket s, String sessID) throws IOException {
		this.server = server;
		this.sessID = sessID;
		this.connections = new ArrayList<>();
		this.connections.add(new HttpConnectionInstance(this, s));
		this.polls = new ArrayList<>();
		this.host = s.getInetAddress().getHostAddress();
	}
	
	public void handleRequest(ClientHeader clientHeader, HttpConnectionInstance connection) throws IOException {
		ParsedURL parsedUrl = ParsedURL.parse(clientHeader.getRequestedURL());
//		System.out.println(clientHeader.getRequestedURL() + " | " + parsedUrl.getPath());
		if(parsedUrl.getPath().startsWith("/_internals/")) {
			String iName = parsedUrl.getPath().substring("/_internals/".length());
			if(iName.equals("callback")) {
				if(lastServedPage == null) {
					writePage(HttpConstants.HTML_INTERNALS_ERROR_PAGE.build(connection, clientHeader, parsedUrl), connection);
					return;
				}
				JSBuiltFunction builtParsedF = lastServedPage.getScript().getFunction(parsedUrl.getGetParameters().get("function_name"));
				JSFunction parsedF = builtParsedF.getBase();
				if(parsedF != null && parsedF instanceof JSFunctionCallable) {
					((JSFunctionCallable) parsedF).invoke(new JSFunctionInvokedEvent(server, connection, lastServedPage));
				}
				writeEmpty(connection);
			}else if(iName.equals("callback_consuming")) {
				if(lastServedPage == null) {
					writePage(HttpConstants.HTML_INTERNALS_ERROR_PAGE.build(connection, clientHeader, parsedUrl), connection);
					return;
				}
				try {
					JSBuiltFunction builtParsedF = lastServedPage.getScript().getFunction(parsedUrl.getGetParameters().get("function_name"));
					JSFunction parsedF = builtParsedF.getBase();
					if(parsedF != null && parsedF instanceof JSFunctionConsumingCallable) {
						((JSFunctionConsumingCallable) parsedF)
							.invoke(new JSFunctionConsumingInvokedEvent(server, connection, lastServedPage, new JSONObject(parsedUrl.getGetParameters().get("function_data"))));
					}
					writeEmpty(connection);
				}catch(Exception e) {
					e.printStackTrace();
					writePage(HttpConstants.HTML_INTERNALS_ERROR_PAGE.build(connection, clientHeader, parsedUrl), connection);
				}
			}else if(iName.equals("poll")) {
				JSONArray arr = new JSONArray();
				polls.forEach(poll -> {
					JSONObject pl = new JSONObject();
					pl.put("type", poll.getType().jsName);
					pl.put("data", poll.getData());
					arr.add(pl);
				});
				polls.clear();
				writeRaw(arr.toString(), "text/html", connection);
			}else if(iName.startsWith("img/")) {
				String imgName = iName.substring("img/".length());
				try {
					URL u = MrCore.class.getResource("/img/"+imgName);
					if(u == null) {
						writePage(new HTMLDocument(HttpStatusCode.NOT_FOUND_404).build(connection, clientHeader, parsedUrl), connection);
						return;
					}
					URLConnection con = u.openConnection();
					con.setUseCaches(false);
					BufferedImage img = ImageIO.read(con.getInputStream());
					writeImage(img, connection);
				}catch(IOException e) {
					e.printStackTrace();
					writePage(new HTMLDocument(HttpStatusCode.INTERNAL_ERROR_500).build(connection, clientHeader, parsedUrl), connection);
				}
			}else {
				InternalResult r = server.lookupInternal("/" + iName, parsedUrl, clientHeader, connection);
				if(r != null) {
					writeInternal(r, connection);
					return;
				}
				writePage(HttpConstants.HTML_INTERNALS_404_PAGE.build(connection, clientHeader, parsedUrl), connection);
			}
			return;
		}
		HTMLBuiltDocument doc = server.lookupURL(parsedUrl, clientHeader, connection);
		if(doc != null) {
			lastServedPage = doc;
			writePage(doc, connection);
		}
	}
	
	public String getHostAddress() {
		return host;
	}
	
	public HTMLBuiltDocument getLastServedPage() {
		return lastServedPage;
	}
	
	public List<HttpClientPoll> getQueuedPolls() {
		return polls;
	}
	
	private void writePage(HTMLBuiltDocument doc, HttpConnectionInstance instance) throws IOException {
		String resp = doc.asString();
		String start, header;
		if(doc.getRedirect() == null) {
			Date date = new Date();
			start = "HTTP/1.1 " + doc.getStatusCode().toString() + "\r\n";
			header = "Date: " + date.toString() + "\r\n";
			header += "Content-Type: text/html\r\n";
			header += "Content-length: " + resp.length() + "\r\n";
			header += "Set-Cookie: mrcore_sessid=" + sessID + "\r\n";
			for(Map.Entry<String, String> en : doc.getBase().getHeaderProperties().entrySet()) {
				header += en.getKey() + ": " + en.getValue() + "\r\n";
			}
			header += "\r\n";
		}else {
			Date date = new Date();
			start = "HTTP/1.1 " + HttpStatusCode.SEE_OTHER_303.toString() + "\r\n";
			header = "Date: " + date.toString() + "\r\n";
			header += "Set-Cookie: mrcore_sessid=" + sessID + "\r\n";
			header += "Location: " + doc.getRedirect() + "\r\n";
			for(Map.Entry<String, String> en : doc.getBase().getHeaderProperties().entrySet()) {
				header += en.getKey() + ": " + en.getValue() + "\r\n";
			}
			header += "\r\n";
		}
		instance.out.write((start + header + resp).getBytes());
	}
	
	private void writeImage(BufferedImage img, HttpConnectionInstance instance) throws IOException {
		Date date = new Date();
		ByteArrayOutputStream bOut = new ByteArrayOutputStream();
		ImageIO.write(img, "png", bOut);
		byte[] b = bOut.toByteArray();
		String start = "HTTP/1.1 200 OK\r\n";
		String header = "Date: " + date.toString() + "\r\n";
		header += "Content-Type: image/png\r\n";
		header += "Content-length: " + b.length + "\r\n";
		header += "Set-Cookie: mrcore_sessid=" + sessID + "\r\n";
		header += "\r\n";
		instance.out.write((start + header).getBytes());
		instance.out.write(b);
	}
	
	private void writeRaw(String raw, String dataType, HttpConnectionInstance instance) throws IOException {
		Date date = new Date();
		String start = "HTTP/1.1 200 OK\r\n";
		String header = "Date: " + date.toString() + "\r\n";
		header += "Content-Type: " + dataType + "\r\n";
		header += "Content-length: " + raw.length() + "\r\n";
		header += "Set-Cookie: mrcore_sessid=" + sessID + "\r\n";
		header += "\r\n";
		instance.out.write((start + header + raw).getBytes());
	}
	
	private void writeInternal(InternalResult res, HttpConnectionInstance instance) throws IOException {
		Date date = new Date();
		String start = "HTTP/1.1 " + res.getStatusCode().toString() + "\r\n";
		String header = "Date: " + date.toString() + "\r\n";
		header += "Content-Type: " + res.getContentType() + "\r\n";
		header += "Content-length: " + res.getContent().length + "\r\n";
		header += "Set-Cookie: mrcore_sessid=" + sessID + "\r\n";
		header += "\r\n";
		instance.out.write((start + header).getBytes());
		instance.out.write(res.getContent());
	}
	
	private void writeEmpty(HttpConnectionInstance instance) throws IOException {
		Date date = new Date();
		String start = "HTTP/1.1 200 OK\r\n";
		String header = "Date: " + date.toString() + "\r\n";
		header += "Content-Type: test/html\r\n";
		header += "Content-length: 0\r\n";
		header += "Set-Cookie: mrcore_sessid=" + sessID + "\r\n";
		header += "\r\n";
		instance.out.write((start + header).getBytes());
	}
	
	public void addPoll(HttpClientPoll poll) {
		polls.add(poll);
	}
	
	public String getSessionID() {
		return sessID;
	}
	
	public HttpServer getServer() {
		return server;
	}
	
	public List<HttpConnectionInstance> getConnections() {
		return connections;
	}
	
	public HttpConnectionInstance addConnection(Socket s) throws IOException {
		HttpConnectionInstance c = new HttpConnectionInstance(this, s);
		connections.add(c);
		return c;
	}
	
	public void disconnect() {
		disconnectRaw();
		server.removeConnection(this);
	}
	
	public void disconnectRaw() {
		connections.forEach(HttpConnectionInstance::closeRaw);
	}

	public void removeConnection(HttpConnectionInstance httpConnectionInstance) {
		connections.remove(httpConnectionInstance);
		if(connections.isEmpty()) disconnect();
	}
	
}
