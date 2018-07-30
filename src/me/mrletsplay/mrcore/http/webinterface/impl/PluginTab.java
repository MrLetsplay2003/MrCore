package me.mrletsplay.mrcore.http.webinterface.impl;

import me.mrletsplay.mrcore.http.server.css.CSSStylesheet;
import me.mrletsplay.mrcore.http.server.html.HTMLElement;

public class PluginTab {

	private String name;
	private String title;
	private HTMLElement html;
	private CSSStylesheet css;
	
	public PluginTab(String name, HTMLElement html, CSSStylesheet css) {
		this.name = name;
		this.html = html;
		this.css = css;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getName() {
		return name;
	}
	
	public String getTitle() {
		return title;
	}
	
	public HTMLElement getHTML() {
		return html;
	}
	
	public CSSStylesheet getCSS() {
		return css;
	}
	
}
