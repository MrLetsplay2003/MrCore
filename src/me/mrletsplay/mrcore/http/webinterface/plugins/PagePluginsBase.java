package me.mrletsplay.mrcore.http.webinterface.plugins;

import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.mrcore.http.server.HttpClientPoll;
import me.mrletsplay.mrcore.http.server.css.CSSStylesheet;
import me.mrletsplay.mrcore.http.server.html.HTMLDocument;
import me.mrletsplay.mrcore.http.server.html.HTMLElement;
import me.mrletsplay.mrcore.http.server.js.JSFunction;
import me.mrletsplay.mrcore.http.server.js.JSScript;
import me.mrletsplay.mrcore.http.webinterface.Webinterface;
import me.mrletsplay.mrcore.http.webinterface.WebinterfaceUtils;
import me.mrletsplay.mrcore.http.webinterface.impl.PluginTab;
import me.mrletsplay.mrcore.http.webinterface.impl.PluginWebinterfaceImpl;

public class PagePluginsBase {

	private static HTMLDocument base;
	
	static {
		base = new HTMLDocument();
		CSSStylesheet style = base.getStyle();
		JSScript script = base.getBaseScript();
		
		script.addFunction(JSFunction.raw(
					"function toggleDropdown(dd){" + 
					"	var dropdownContent = dd.nextElementSibling;" + 
					"	if (dropdownContent.style.display === \"block\") {" + 
					"		dropdownContent.style.display = \"none\";" + 
					"	} else {" + 
					"		dropdownContent.style.display = \"block\";" + 
					"	}" + 
					"}"
				));
		
		script.addFunction(JSFunction.raw(
					"function redirect(url){\r\n" + 
					"	window.location.href = \"/plugins/\"+url+window.location.search;" + 
					"}"
				));
		
		style.get(".tab-button, .dropdown-button, .dropdown-container > a")
			.addProperty("font-family", "'Ek Mukta'")
			.addProperty("color", "white")
			.addProperty("border", "none")
			.addProperty("background", "none")
			.addProperty("outline", "none")
			.addProperty("width", "100%")
			.addProperty("font-size", "20px")
			.addProperty("padding", "6px 8px 6px 10px")
			.addProperty("text-align", "left");
		
		style.get(".tab-button:hover, .dropdown-button:hover, .dropdown-container > a:hover")
			.addProperty("color", "lightgray")
			.addProperty("cursor", "pointer")
			.addProperty("border-left", "2px solid white");
		
		style.get(".dropdown-button > img, .tab-button > img, .dropdown-container > a > img")
			.addProperty("position", "relative")
			.addProperty("top", "-2px")
			.addProperty("width", "16px")
			.addProperty("vertical-align", "middle")
			.addProperty("margin-right", "5px");
		
		style.get(".tab-title, .dropdown-container > a")
			.addProperty("font-family", "'Ek Mukta'")
			.addProperty("color", "lightgray")
			.addProperty("border", "none")
			.addProperty("background", "none")
			.addProperty("outline", "none")
			.addProperty("width", "100%")
			.addProperty("font-size", "14px")
			.addProperty("padding", "6px 8px 6px 10px")
			.addProperty("text-align", "left")
			.addProperty("display", "block")
			.addProperty("width", "100%");
		
		style.getClass("tab-title")
			.addProperty("margin-top", "10px");
		
		style.getClass("dropdown-container")
			.addProperty("display", "none")
			.addProperty("padding-left", "8px");
		
		style.getClass("fa-caret-down")
			.addProperty("float", "right")
			.addProperty("padding-right", "8px")
			.addProperty("padding-top", "5px");
		
		HTMLElement div = HTMLElement.div().setID("page-content");
		
		HTMLElement sidebarNav = HTMLElement.dynamic(HTMLElement.div(), event -> null, event -> {
					List<HTMLElement> ch = new ArrayList<>();
					for(PluginWebinterfaceImpl impl : Webinterface.getPluginPages()) {
						HTMLElement drd = HTMLElement.button(impl.getPlugin().getName())
								.addClass("dropdown-button")
								.addChild(HTMLElement.i().addClasses("fa", "fa-caret-down").setSortingIndex(2))
								.addChild(HTMLElement.img("https://graphite-official.com/webinterface/img/home.png"))
								.setContentSortingIndex(1);
						
						drd.onClicked().function(JSFunction.raw("toggleDropdown(this)"));
						
						ch.add(drd);
						
						HTMLElement sd = HTMLElement.div()
								.addClass("dropdown-container");
						
						for(PluginTab tab : impl.getTabs()) {
							HTMLElement a = HTMLElement.a(tab.getName())
								.addChild(HTMLElement.img("https://graphite-official.com/webinterface/img/home.png"))
								.setContentSortingIndex(1);
							
							a.onClicked()
								.function(JSFunction.raw("redirect('"+impl.getPlugin().getName()+"/"+tab.getName()+"')"));
							
							sd.addChild(a);
						}
						ch.add(sd);
					}
					return ch;
				})
				.setID("sidebar-nav");
		
		sidebarNav.css()
			.position("absolute", "0", "0", "15%", "100%")
			.addProperty("background-color", "rgb(90, 90, 90)");
		
		sidebarNav.addChild(HTMLElement.button("Category")
				.addClass("tab-title"));
		
		sidebarNav.addChild(HTMLElement.button("Something")
				.addClass("tab-button"));
		
		HTMLElement tabContent = HTMLElement.div().setID("tab-content");
		
		tabContent.css()
			.position("absolute", "0", "15%", "85%", "100%")
			.addProperty("background-color", "#ecf0f5");
		
		div.addChild(sidebarNav);
		div.addChild(tabContent);
		
		base.addElement(WebinterfaceUtils.addLoading(base));
		base.addElement(WebinterfaceUtils.addHeader(base));
		
		base.addElement(div);
		base.addBuildAction(event -> {
			if(!Webinterface.isLoggedIn(event.getConnection())) {
				event.setAllowAccess(false);
				event.getConnection().addPoll(HttpClientPoll.redirect("/login"));
			}
		});
	}
	
	public static HTMLDocument getBase() {
		return base.clone();
	}
	
}
