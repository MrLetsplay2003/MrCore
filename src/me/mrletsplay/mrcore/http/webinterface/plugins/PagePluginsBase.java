package me.mrletsplay.mrcore.http.webinterface.plugins;

import me.mrletsplay.mrcore.http.server.HttpClientPoll;
import me.mrletsplay.mrcore.http.server.css.CSSStylesheet;
import me.mrletsplay.mrcore.http.server.html.HTMLDocument;
import me.mrletsplay.mrcore.http.server.html.HTMLElement;
import me.mrletsplay.mrcore.http.server.js.JSFunction;
import me.mrletsplay.mrcore.http.server.js.JSScript;
import me.mrletsplay.mrcore.http.webinterface.Webinterface;
import me.mrletsplay.mrcore.http.webinterface.WebinterfaceUtils;

public class PagePluginsBase {

	private static HTMLDocument base;
	
	static {
		base = new HTMLDocument();
		CSSStylesheet style = base.getStyle();
		JSScript script = base.getBaseScript();
		
		script.appendFunction(JSFunction.raw(
					"function toggleDropdown(dd){" + 
					"	var dropdownContent = dd.nextElementSibling;" + 
					"	if (dropdownContent.style.display === \"block\") {" + 
					"		dropdownContent.style.display = \"none\";" + 
					"	} else {" + 
					"		dropdownContent.style.display = \"block\";" + 
					"	}" + 
					"}"
				));
		
		script.appendFunction(JSFunction.raw(
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
			.addProperty("margin-top", "10px");
		
		style.getClass("dropdown-container")
			.addProperty("display", "none")
			.addProperty("padding-left", "8px");
		
		style.getClass("fa-caret-down")
			.addProperty("float", "right")
			.addProperty("padding-right", "8px")
			.addProperty("padding-top", "5px");
		
		HTMLElement div = HTMLElement.div().setID("page-content");
		
		HTMLElement sidebarNav = HTMLElement.div()
				.setID("sidebar-nav");
		
		sidebarNav.css()
			.position("absolute", "0", "0", "15%", "100%")
			.addProperty("background-color", "rgb(90, 90, 90)");
		
		sidebarNav.addChild(HTMLElement.button("Category")
				.addClass("tab-title"));
		
		sidebarNav.addChild(HTMLElement.button("Something")
				.addClass("tab-button"));
		
		HTMLElement drd = HTMLElement.button("Something-Drop")
				.addClass("dropdown-button")
				.addChild(HTMLElement.i().addClasses("fa", "fa-caret-down").setSortingIndex(2))
				.addChild(HTMLElement.img("https://graphite-official.com/webinterface/img/home.png"))
				.setContentSortingIndex(1);
		
		drd.onClicked().function(JSFunction.raw("toggleDropdown(this)"));
		
		sidebarNav.addChild(drd);
		
		sidebarNav.addChild(HTMLElement.div()
				.addClass("dropdown-container")
				.addChild(HTMLElement.a("Something else")
					.addChild(HTMLElement.img("https://graphite-official.com/webinterface/img/home.png"))
					.setContentSortingIndex(1)));
		
		div.addChild(sidebarNav);
		
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
	
	public static HTMLDocument getPage() {
		return base;
	}
	
}
