package me.mrletsplay.mrcore.http.webinterface.plugins;

import me.mrletsplay.mrcore.http.server.html.HTMLDocument;
import me.mrletsplay.mrcore.http.server.html.HTMLElement;

public class PagePluginsHome {

	private static HTMLDocument home;
	
	static {
		home = PagePluginsBase.getBase();
		home.setName("Plugins | Home");
		
		HTMLElement div = home.getElementByID("tab-content");
		
		div.addChild(HTMLElement.h1("@Home"));
	}
	
	public static HTMLDocument getPage() {
		return home;
	}
	
}
