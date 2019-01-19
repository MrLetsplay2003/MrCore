package me.mrletsplay.mrcore.http.html;

import me.mrletsplay.mrcore.http.event.HttpPageBuildEvent;
import me.mrletsplay.mrcore.http.html.built.HTMLBuiltDocument;

public class HTMLDocumentBuildEvent extends HttpPageBuildEvent {

	private HTMLDocument document;
	private HTMLBuiltDocument builtDocument;
	
	public HTMLDocumentBuildEvent(HttpPageBuildEvent event, HTMLDocument document, HTMLBuiltDocument builtDocument) {
		super(event);
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
