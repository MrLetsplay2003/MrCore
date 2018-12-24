package me.mrletsplay.mrcore.http.server.impl;

import me.mrletsplay.mrcore.http.server.HttpHeaderFields;
import me.mrletsplay.mrcore.http.server.HttpServerHeader;

public class DefaultHttpServerHeader implements HttpServerHeader {

	private String protocol, statusCode;
	private HttpHeaderFields headerFields;
	private byte[] body;
	
	public DefaultHttpServerHeader(String protocol, String statusCode, HttpHeaderFields headerFields, byte[] body) {
		this.protocol = protocol;
		this.statusCode = statusCode;
		this.headerFields = headerFields;
		this.body = body;
	}
	
	@Override
	public String getProtocol() {
		return protocol;
	}

	@Override
	public String getStatusCode() {
		return statusCode;
	}
	
	@Override
	public HttpHeaderFields getFields() {
		return headerFields;
	}
	
	@Override
	public byte[] getBody() {
		return body;
	}

}
