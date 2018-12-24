package me.mrletsplay.mrcore.http.server.impl;

import me.mrletsplay.mrcore.http.server.HttpClientHeader;
import me.mrletsplay.mrcore.http.server.HttpHeaderFields;

public class DefaultHttpClientHeader implements HttpClientHeader {

	private String method, path, protocol, protocolVersion;
	private HttpHeaderFields headerFields;
	private byte[] body;
	
	public DefaultHttpClientHeader(String method, String path, String protocol, String protocolVersion, HttpHeaderFields headerFields, byte[] body) {
		this.method = method;
		this.path = path;
		this.protocol = protocol;
		this.protocolVersion = protocolVersion;
		this.headerFields = headerFields;
		this.body = body;
	}
	
	@Override
	public String getRequestMethod() {
		return method;
	}

	@Override
	public String getRawRequestedPath() {
		return path;
	}

	@Override
	public String getProtocol() {
		return protocol;
	}
	
	@Override
	public String getProtocolVersion() {
		return protocolVersion;
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
