package me.mrletsplay.mrcore.http.server.impl;

import java.nio.charset.StandardCharsets;

import me.mrletsplay.mrcore.http.event.HttpPageBuildEvent;
import me.mrletsplay.mrcore.http.html.HTMLDocument;
import me.mrletsplay.mrcore.http.server.HttpDefaultStatusCode;
import me.mrletsplay.mrcore.http.server.HttpOpenPage;
import me.mrletsplay.mrcore.http.server.HttpPage;
import me.mrletsplay.mrcore.http.server.HttpServerHeader;

public class HTMLHttpPage implements HttpPage {

	private HTMLDocument document;
	
	public HTMLHttpPage(HTMLDocument document) {
		this.document = document;
	}
	
	@Override
	public HttpOpenPage build(HttpPageBuildEvent event) {
		HttpServerHeader header = new DefaultHttpServerHeader("HTTP/1.1", HttpDefaultStatusCode.OK, new DefaultHttpHeaderFields());
		byte[] body = document.build(event).asString().getBytes(StandardCharsets.UTF_8);
		return new DefaultOpenPage(event.getConnection(), event.getConnectionSocket(), this, header, body);
	}
	
}
