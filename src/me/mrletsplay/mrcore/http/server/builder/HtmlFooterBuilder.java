package me.mrletsplay.mrcore.http.server.builder;

import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.mrcore.http.server.css.CSSStylesheet;
import me.mrletsplay.mrcore.http.server.html.HTMLDocument;
import me.mrletsplay.mrcore.http.server.html.HTMLElement;

public class HtmlFooterBuilder {
	
	List<FooterElement> links;
	
	public HtmlFooterBuilder() {
		this.links = new ArrayList<>();
	}
	
	public HtmlFooterBuilder addFooterLink(String name, String link) {
		links.add(new LinkFooterElement(name, link));
		return this;
	}
	
	public HTMLDocument generateHtml() {
		HTMLDocument doc = new HTMLDocument();
		CSSStylesheet style = doc.getStyle();

		style.get("a:link")
			.addProperty("text-decoration", "none")
			.addProperty("color", "rgba(140, 140, 140, 0.6)");
		style.get("a:active")
			.addProperty("text-decoration", "none")
			.addProperty("color", "rgba(140, 140, 140, 0.6)");
		style.get("a:visited")
			.addProperty("text-decoration", "none")
			.addProperty("color", "rgba(140, 140, 140, 0.6)");
		style.get(".footer-wrap")
			.addProperty("position", "absolute")
			.addProperty("bottom", "3px")
			.addProperty("left", "0")
			.addProperty("width", "100%")
			.addProperty("background-color", "transparent")
			.addProperty("font-family", "'Ek Mukta'")
			.addProperty("padding-left", "10px")
			.addProperty("font-size", "20px")
			.addProperty("color", "white")
			.addProperty("text-align", "left")
			.addProperty("color", "rgba(140, 140, 140, 0.7)");
		
		HTMLElement div = HTMLElement.div().addClass("footer-wrap");
		
		for(FooterElement el : links) {
			if(el instanceof LinkFooterElement) {
				LinkFooterElement ln = (LinkFooterElement) el;
				div.addChild(HTMLElement.a(ln.getName(), ln.link));
			}else throw new RuntimeException();
		}
		
		doc.addElement(div);
		
		return doc;
	}
	
	public abstract class FooterElement{
		
		String name;
		
		public FooterElement(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
		
	}
	
	public class LinkFooterElement extends FooterElement{
		
		String link;
		
		public LinkFooterElement(String name, String link) {
			super(name);
			this.link = link;
		}
		
	}

}
