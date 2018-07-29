package me.mrletsplay.mrcore.http.webinterface.plugins;

import me.mrletsplay.mrcore.http.server.html.HTMLDocument;
import me.mrletsplay.mrcore.http.server.html.HTMLElement;
import me.mrletsplay.mrcore.http.webinterface.WebinterfaceUtils;

public class PagePluginsHome {

	private static HTMLDocument home;
	
	static {
		home = new HTMLDocument();
//		CSSStylesheet style = home.getStyle();
		
		HTMLElement div = HTMLElement.div().setID("page-content");
		
		div.addChild(HTMLElement.h1("Base"));
		
		home.addElement(WebinterfaceUtils.addLoading(home));
		home.addElement(WebinterfaceUtils.addHeader(home));
		home.addElement(div);
	}
	
	public static HTMLDocument getPage() {
		return home;
	}
	
}
