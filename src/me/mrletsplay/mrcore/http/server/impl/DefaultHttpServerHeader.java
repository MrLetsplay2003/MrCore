package me.mrletsplay.mrcore.http.server.impl;

import me.mrletsplay.mrcore.http.server.HttpHeaderFields;
import me.mrletsplay.mrcore.http.server.HttpServerHeader;
import me.mrletsplay.mrcore.http.server.HttpStatusCode;

public class DefaultHttpServerHeader implements HttpServerHeader {

	private String protocol;
	private HttpStatusCode statusCode;
	private HttpHeaderFields headerFields;
	
	public DefaultHttpServerHeader(String protocol, HttpStatusCode statusCode, HttpHeaderFields headerFields) {
		this.protocol = protocol;
		this.statusCode = statusCode;
		this.headerFields = headerFields;
	}
	
	@Override
	public String getProtocol() {
		return protocol;
	}
	
	@Override
	public void setStatusCode(HttpStatusCode statusCode) {
		this.statusCode = statusCode;
	}

	@Override
	public HttpStatusCode getStatusCode() {
		return statusCode;
	}
	
	@Override
	public HttpHeaderFields getFields() {
		return headerFields;
	}
	
}
