package me.mrletsplay.mrcore.http.webinterface.impl;

import me.mrletsplay.mrcore.http.server.css.CSSStyleSheet;
import me.mrletsplay.mrcore.http.server.html.HTMLElement;

public interface PluginTab {

	public String getName();
	
	public String getTitle();
	
	public HTMLElement getTabContent();
	
	public CSSStyleSheet getCSS();
	
}
