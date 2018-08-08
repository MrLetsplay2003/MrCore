package me.mrletsplay.mrcore.http.server.html.built;

import me.mrletsplay.mrcore.http.server.html.HTMLDocument.HttpSiteAccessedEvent;
import me.mrletsplay.mrcore.http.server.html.HTMLElementLabel;

public class HTMLBuiltElementLabel extends HTMLBuiltElement {

	public HTMLBuiltElementLabel(HTMLBuiltElement parent, HTMLBuiltDocument doc, HTMLElementLabel base, String id, HttpSiteAccessedEvent event, String[] params) {
		super(parent, doc, base, id, event, params);
	}
	
	@Override
	public void onBuildFinished() {
		addAttribute("for", getDocument().getElement(((HTMLElementLabel) getBase()).getTarget()).getID());
	}

}
