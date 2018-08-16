package me.mrletsplay.mrcore.http.webinterface.impl;

import me.mrletsplay.mrcore.http.server.css.CSSStyleSheet;
import me.mrletsplay.mrcore.http.server.html.HTMLElement;

public class PluginTabImpl implements PluginTab {

	private String name;
	private String title;
	private HTMLElement html;
	private CSSStyleSheet css;
	
	public PluginTabImpl(String name, HTMLElement html, CSSStyleSheet css) {
		this.name = name;
		this.html = html;
		this.css = css;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public HTMLElement getTabContent() {
		return html;
	}

	@Override
	public CSSStyleSheet getCSS() {
		return css;
	}
	
}
