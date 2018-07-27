package me.mrletsplay.mrcore.http.webinterface;

import me.mrletsplay.mrcore.http.server.css.CSSStylesheet;
import me.mrletsplay.mrcore.http.server.html.HTMLDocument;
import me.mrletsplay.mrcore.http.server.html.HTMLElement;

public class PageHome {

	private static HTMLDocument home;
	
	static {
		home = new HTMLDocument();
		CSSStylesheet style = home.getStyle();
		
		style.get("a:link")
			.addProperty("text-decoration", "none")
			.addProperty("color", "white");
		style.get("a:active")
			.addProperty("text-decoration", "none")
			.addProperty("color", "white");
		style.get("a:visited")
			.addProperty("text-decoration", "none")
			.addProperty("color", "white");
		
		HTMLElement div = HTMLElement.div();
		
		HTMLElement pageContentDiv = HTMLElement.div().setID("page-content");
		
		pageContentDiv.css()
			.addProperty("position", "absolute")
			.addProperty("top", "65px")
			.addProperty("left", "0")
			.addProperty("width", "100%")
			.addProperty("height", "calc(100% - 65px)")
			.addProperty("background-color", "white")
			.addProperty("overflow", "hidden");
		
		HTMLElement about = HTMLElement.div().setID("about-container");
		
		about.css()
			.addProperty("position", "absolute")
			.addProperty("width", "100%")
			.addProperty("height", "30%")
			.addProperty("left", "0")
			.addProperty("top", "25%")
			.addProperty("color", "white")
			.addProperty("text-align", "center")
			.addProperty("font-size", "20px");
		
		HTMLElement aboutTextHeader = HTMLElement.a("About testname");
		
		aboutTextHeader.css()
			.addProperty("font-size", "35px")
			.addProperty("text-decoration", "underline")
			.addProperty("font-weight", "bold");
		
		HTMLElement aboutText = HTMLElement.a("</br></br>A very nice schlong dong text.</br>"
											+ "Made with <3 by the SchlongDong GmbH.</br>"
											+ "Da is ja da Günther und der Heinz Dieter</br>"
											+ "Dieser Text muss vier Zeilen haben.");
		
		about.addChild(aboutTextHeader);
		about.addChild(aboutText);
		
		HTMLElement buttonBox = HTMLElement.div().setID("button-box");
		
		buttonBox.css()
			.addProperty("position", "absolute")
			.addProperty("width", "100%")
			.addProperty("height", "5%")
			.addProperty("top", "55%")
			.addProperty("display", "inline-flex")
			.addProperty("justify-content", "center")
			.addProperty("align-items", "center");
		
		HTMLElement tryButton = HTMLElement.button("Try it out");
		
		tryButton.css()
			.addProperty("position", "absolute")
			.addProperty("width", "10%;")
			.addProperty("height", "100%")
			.addProperty("border", "2px solid white")
			.addProperty("border-radius", "3px")
			.addProperty("background-color", "transparent")
			.addProperty("color", "white")
			.addProperty("font-size", "18px")
			.addProperty("opacity", "0.6");
		
		tryButton.onHover().css()
			.addProperty("background-color", "hsl(0, 0%, 20%)")
			.addProperty("cursor", "pointer")
			.addProperty("opacity", "1");
		
		buttonBox.addChild(tryButton);
		
		pageContentDiv.addChild(about);
		pageContentDiv.addChild(buttonBox);
		div.addChild(Webinterface.addHeader(home));
		pageContentDiv.addChild(HTMLElement.img(Var.HEAD_IMG));
		div.addChild(pageContentDiv);
		
		home.addElement(Webinterface.addLoading(home));
		
		home.addElement(div);
	}
	
	public static HTMLDocument getPage() {
		return home;
	}
	
}
