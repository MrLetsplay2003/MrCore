package me.mrletsplay.mrcore.http.server.html.built;

import me.mrletsplay.mrcore.http.server.html.HTMLDocument.HttpSiteAccessedEvent;
import me.mrletsplay.mrcore.http.server.html.HTMLElement;
import me.mrletsplay.mrcore.http.server.html.HTMLElement.HTMLElementGetContentEvent;
import me.mrletsplay.mrcore.http.server.html.HTMLElementLabel;

public class HTMLBuiltElementLabel extends HTMLBuiltElement {

	private HTMLElement target;
	
	public HTMLBuiltElementLabel(HTMLBuiltElement parent, HTMLBuiltDocument doc, HTMLElementLabel base, String id, HttpSiteAccessedEvent event, String[] params) {
		super(parent, doc, base, id, event, params);
		this.target = base.getTarget();
	}
	
	public HTMLElement getTarget() {
		return target;
	}
	
	@Override
	public void onBuildFinished(HTMLElementGetContentEvent event) {
		super.onBuildFinished(event);
		addAttribute("for", getDocument().getElement(((HTMLElementLabel) getBase()).getTarget()).getID());
	}

}
