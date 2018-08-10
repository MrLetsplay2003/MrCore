package me.mrletsplay.mrcore.http.server.html;

import me.mrletsplay.mrcore.http.server.html.HTMLDocument.HttpSiteAccessedEvent;
import me.mrletsplay.mrcore.http.server.html.built.HTMLBuiltDocument;
import me.mrletsplay.mrcore.http.server.js.JSFunction;

public interface CustomPollHandler {

	public JSFunction getHandlingFunction();
	
	public PollType getHandlingType();
	
	public static CustomPollHandler create(PollType type, JSFunction handlingFunction) {
		return new CustomPollHandlerImpl(type, handlingFunction);
	}
	
	public default BuiltCustomPollHandler build(HTMLBuiltDocument doc, HttpSiteAccessedEvent event) {
		return new BuiltCustomPollHandler(doc, this, event);
	}
	
}
