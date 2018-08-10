package me.mrletsplay.mrcore.http.webinterface.plugins;

import me.mrletsplay.mrcore.http.server.HttpClientPoll;
import me.mrletsplay.mrcore.http.server.html.ConsolePollType;
import me.mrletsplay.mrcore.http.server.html.HTMLDocument;
import me.mrletsplay.mrcore.http.server.html.HTMLElement;
import me.mrletsplay.mrcore.misc.JSON.JSONObject;

public class PagePluginsHome {

	private static HTMLDocument home;
	
	static {
		home = PagePluginsBase.getBase();
		home.setName("Plugins | Home");
		
		HTMLElement div = home.getElementByID("tab-content");
		
		div.addChild(HTMLElement.h1("@Home"));
		
		home.addAccessAction(event -> {
			event.getConnection().addPoll(HttpClientPoll.custom(ConsolePollType.CONSOLE_LINE, new JSONObject()));
		});
	}
	
	public static HTMLDocument getPage() {
		return home;
	}
	
}
