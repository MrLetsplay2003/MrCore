package me.mrletsplay.mrcore.http.server;

import me.mrletsplay.mrcore.http.server.html.HTMLDocument;
import me.mrletsplay.mrcore.http.server.html.HTMLElement;

public class HttpConstants {

	public static final String
			HTML_404_REQUESTED_URL = "{requested_url}",
			HTML_403_REQUESTED_URL = "{requested_url}";
	
	public static final HTMLDocument
			HTML_INTERNALS_404_PAGE,
			HTML_INTERNALS_ERROR_PAGE;
	
	static {
		HTML_INTERNALS_404_PAGE = new HTMLDocument(HttpStatusCode.NOT_FOUND_404);
		HTML_INTERNALS_404_PAGE.addElement(HTMLElement.h1("Something isn't right here..."));
		HTML_INTERNALS_404_PAGE.addElement(HTMLElement.p("Whatever URL you tried to access, it either doesn't exist anymore or didn't ever exist in the first place"));
		HTML_INTERNALS_ERROR_PAGE = new HTMLDocument(HttpStatusCode.INTERNAL_ERROR_500);
		HTML_INTERNALS_ERROR_PAGE.addElement(HTMLElement.h1("Something isn't right here..."));
		HTML_INTERNALS_ERROR_PAGE.addElement(HTMLElement.p("An internal error occured while processing your request"));
	}
	
}
