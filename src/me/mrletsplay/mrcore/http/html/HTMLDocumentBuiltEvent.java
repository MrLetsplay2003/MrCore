package me.mrletsplay.mrcore.http.html;

import me.mrletsplay.mrcore.http.event.AbstractHttpEvent;
import me.mrletsplay.mrcore.http.html.built.HTMLBuiltDocument;
import me.mrletsplay.mrcore.http.server.HttpServer;

public class HTMLDocumentBuiltEvent extends AbstractHttpEvent {

	private HTMLDocument document;
	private HTMLBuiltDocument builtDocument;
	
	public HTMLDocumentBuiltEvent(HttpServer server, HTMLDocument document, HTMLBuiltDocument builtDocument) {
		super(server);
		this.document = document;
		this.builtDocument = builtDocument;
	}
	
	public HTMLDocument getDocument() {
		return document;
	}
	
	public HTMLBuiltDocument getBuiltDocument() {
		return builtDocument;
	}
	
}
