package me.mrletsplay.mrcore.http.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public interface HttpServerHeader {

	public String getProtocol();

	public String getStatusCode();

	public HttpHeaderFields getFields();

	public byte[] getBody();
	
	public default byte[] getBytes() {
		ByteArrayOutputStream bOut = new ByteArrayOutputStream();
		StringBuilder b = new StringBuilder();
		b.append(getProtocol()).append(" ").append(getStatusCode()).append("\r\n");
		for(Map.Entry<String, List<String>> en : getFields().getRawFields().entrySet()) {
			for(String v : en.getValue()) {
				b.append(en.getKey()).append(": ").append(v).append("\r\n");
			}
		}
		b.append("Content-Type: text/html; charset=UTF-8").append("\r\n");
		b.append("Content-Length: ").append(getBody().length).append("\r\n");
		b.append("\r\n");
		try {
			bOut.write(b.toString().getBytes(StandardCharsets.UTF_8));
			bOut.write(getBody());
		} catch (IOException e) {}
		return bOut.toByteArray();
	}

	public default String asString() {
		return new String(getBytes(), StandardCharsets.UTF_8);
	}

}
