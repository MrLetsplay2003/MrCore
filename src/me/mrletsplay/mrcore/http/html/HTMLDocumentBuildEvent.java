package me.mrletsplay.mrcore.http.html;

import me.mrletsplay.mrcore.http.html.built.HTMLBuiltDocument;
import me.mrletsplay.mrcore.http.server.HttpEvent;

public class HTMLDocumentBuildEvent implements HttpEvent {

	private HTMLDocument document;
	private HTMLBuiltDocument builtDocument;
	
	public HTMLDocumentBuildEvent(HTMLDocument document, HTMLBuiltDocument builtDocument) {
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
