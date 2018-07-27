package me.mrletsplay.mrcore.http.webinterface;

import me.mrletsplay.mrcore.http.server.html.HTMLDocument;

public class PageLogout {

	private static HTMLDocument logout;
	
	static {
		logout = new HTMLDocument();
		logout.addBuildAction(event -> {
			Webinterface.loggedInSessions.remove(event.getConnection().getSessionID());
		});
		logout.setRedirect("/");
	}
	
	public static HTMLDocument getPage() {
		return logout;
	}
	
}
