package me.mrletsplay.mrcore.http.server.impl;

import java.nio.charset.StandardCharsets;

import me.mrletsplay.mrcore.http.event.HttpPageBuildEvent;
import me.mrletsplay.mrcore.http.html.HTMLDocument;
import me.mrletsplay.mrcore.http.server.HttpOpenPage;
import me.mrletsplay.mrcore.http.server.HttpPage;

public class HttpHTMLPage implements HttpPage {

	private HTMLDocument document;
	
	public HttpHTMLPage(HTMLDocument document) {
		this.document = document;
	}
	
	@Override
	public HttpOpenPage build(HttpPageBuildEvent event) {
		byte[] body = document.build(event).asString().getBytes(StandardCharsets.UTF_8);
		return new HttpOpenPage(event.getConnection(), event.getConnectionSocket(), event.getConnection().newPageID(), this, event.getHeader(), body);
	}
	
}
