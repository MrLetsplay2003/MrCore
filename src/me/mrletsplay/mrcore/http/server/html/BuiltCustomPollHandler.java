package me.mrletsplay.mrcore.http.server.html;

import me.mrletsplay.mrcore.http.server.html.HTMLDocument.HttpSiteAccessedEvent;
import me.mrletsplay.mrcore.http.server.html.built.HTMLBuiltDocument;
import me.mrletsplay.mrcore.http.server.js.built.JSBuiltFunction;

public class BuiltCustomPollHandler {
	
	private JSBuiltFunction handlingFunction;
	private CustomPollHandler base;
	
	public BuiltCustomPollHandler(HTMLBuiltDocument doc, CustomPollHandler base, HttpSiteAccessedEvent accessEvent) {
		this.base = base;
		base.getHandlingFunction().setParameters("poll");
		this.handlingFunction = doc.getScript().appendFunction(base.getHandlingFunction());
		System.out.println(this.handlingFunction.asString());
	}
	
	public JSBuiltFunction getHandlingFunction() {
		return handlingFunction;
	}
	
	public CustomPollHandler getBase() {
		return base;
	}

}
