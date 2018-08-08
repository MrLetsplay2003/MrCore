package me.mrletsplay.mrcore.http.server.html;

import me.mrletsplay.mrcore.http.server.html.HTMLDocument.HttpSiteAccessedEvent;
import me.mrletsplay.mrcore.http.server.html.built.HTMLBuiltDocument;
import me.mrletsplay.mrcore.http.server.html.built.HTMLBuiltElement;
import me.mrletsplay.mrcore.http.server.html.built.HTMLBuiltElementLabel;

public class HTMLElementLabel extends HTMLElement{

	private HTMLElement forElement;
	
	public HTMLElementLabel(HTMLElement forElement) {
		super("label");
		this.forElement = forElement;
	}
	
	public HTMLElementLabel(HTMLElement forElement, String text) {
		super("label", text);
		this.forElement = forElement;
	}
	
	public HTMLElement getTarget() {
		return forElement;
	}
	
	@Override
	public HTMLBuiltElement build(HTMLBuiltElement parent, HTMLBuiltDocument doc, String id, HttpSiteAccessedEvent event, String... params) {
		return new HTMLBuiltElementLabel(parent, doc, this, id, event, params);
	}
	
}
