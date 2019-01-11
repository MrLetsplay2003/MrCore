package me.mrletsplay.mrcore.http.server;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public interface HttpServerHeader {

	public String getProtocol();
	
	public void setStatusCode(HttpStatusCode statusCode);

	public HttpStatusCode getStatusCode();

	public HttpHeaderFields getFields();

	public default String asString(int contentLength) {
		StringBuilder b = new StringBuilder();
		b.append(getProtocol()).append(" ").append(getStatusCode().getStatusCode()).append(" ").append(getStatusCode().getMessage()).append("\r\n");
		for(Map.Entry<String, List<String>> en : getFields().getRawFields().entrySet()) {
			for(String v : en.getValue()) {
				b.append(en.getKey()).append(": ").append(v).append("\r\n");
			}
		}
		b.append("Content-Type: text/html; charset=UTF-8").append("\r\n");
		b.append("Content-Length: ").append(contentLength).append("\r\n");
		b.append("\r\n");
		return b.toString();
	}
	
	public default byte[] getBytes(int contentLength) {
		return asString(contentLength).getBytes(StandardCharsets.UTF_8);
	}

}
