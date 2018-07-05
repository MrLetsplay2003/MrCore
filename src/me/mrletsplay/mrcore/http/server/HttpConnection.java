package me.mrletsplay.mrcore.http.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;

import me.mrletsplay.mrcore.http.server.HTML.HTMLBuiltDocument;
import me.mrletsplay.mrcore.http.server.HTML.HTMLDocument;
import me.mrletsplay.mrcore.http.server.HttpServer.ClientHeader;

public class HttpConnection {

	private HttpServer server;
	private Socket socket;
	private InputStream in;
	private OutputStream out;
	private HTMLBuiltDocument lastServedPage;
	private String sessID;
	private Thread connectionThread;
	
	public HttpConnection(HttpServer server, Socket s, String sessID) throws IOException {
		this.server = server;
		this.socket = s;
		this.in = s.getInputStream();
		this.out = s.getOutputStream();
		this.sessID = sessID;
		this.connectionThread = new Thread(() -> {
			
		});
	}
	
	public void handleRequest(ClientHeader clientHeader) throws IOException {
		ParsedURL parsedUrl = ParsedURL.parse(clientHeader.getRequestedURL());
		HTMLDocument doc = server.lookupURL(parsedUrl, lastServedPage!=null?lastServedPage.getScript():null);
		
		if(doc != null) {
			lastServedPage = doc.build();
			String resp = lastServedPage.getHTMLCode();
			Date date = new Date();
			String start = "HTTP/1.1 200 OK\r\n";
			String header = "Date: "+date.toString()+"\r\n";
			header+= "Content-Type: text/html\r\n";
			header+= "Content-length: "+resp.length()+"\r\n";
			header+= "Set-Cookie: mrcore_sessid=" + sessID + "\r\n";
			header+= "\r\n";
			out.write((start+header+resp).getBytes());
//			out.flush();
		}else {
			lastServedPage = server.get404Page().build();
			String content = lastServedPage.getHTMLCode().replace("{requested_url}", parsedUrl.getPath());
			Date date = new Date();
			String start = "HTTP/1.1 404 Not Found\r\n";
			String header = "Date: "+date.toString()+"\r\n";
			header+= "Content-Type: text/html\r\n";
			header+= "Content-length: " + content.length() + "\r\n";
			header+= "Set-Cookie: mrcore_sessid=" + sessID + "\r\n";
			header+= "\r\n";
			out.write((start+header+content).getBytes());
//			out.flush();
		}
	}
	
	public String getSessionID() {
		return sessID;
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public InputStream getInputStream() {
		return in;
	}
	
	public OutputStream getOutputStream() {
		return out;
	}
	
}
