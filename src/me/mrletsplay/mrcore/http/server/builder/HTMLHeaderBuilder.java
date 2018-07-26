package me.mrletsplay.mrcore.http.server.builder;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import me.mrletsplay.mrcore.http.server.css.CSSStylesheet;
import me.mrletsplay.mrcore.http.server.html.HTMLDocument;
import me.mrletsplay.mrcore.http.server.html.HTMLElement;

public class HTMLHeaderBuilder {
	
	private HtmlHeaderImage headerImage;
	private List<NavbarElement> links;
	
	public HTMLHeaderBuilder() {
		this.links = new ArrayList<>();
	}
	
	public HTMLHeaderBuilder addNavbarLink(String name, String link) {
		links.add(new LinkNavbarElement(name, link));
		return this;
	}
	
	public NavbarDropdown addNavbarDropdown(String name) {
		NavbarDropdown dr = new NavbarDropdown(this, name);
		links.add(dr);
		return dr;
	}
	
	public HtmlHeaderImage setImage(BufferedImage img, Position pos) {
		headerImage = new HtmlHeaderImage(this, img, pos);
		return headerImage;
	}
	
	public HTMLDocument generateHtml() {
		HTMLDocument doc = new HTMLDocument();
		CSSStylesheet style = doc.getStyle();
		
		style.get("body")
				.addProperty("background-color", "rgba(24, 34, 40, 1)")
				.addProperty("overflow", "hidden");
		style.get("a:link")
				.addProperty("text-decoration", "none")
				.addProperty("color", "white");
		style.get("a:active")
				.addProperty("text-decoration", "none")
				.addProperty("color", "white");
		style.get("a:visited")
				.addProperty("text-decoration", "none")
				.addProperty("color", "white");
		style.get(".clearfix:after")
				.addProperty("display", "block")
				.addProperty("clear", "both");
		style.get(".menu-element")
				.addProperty("width", "20%")
				.addProperty("box-shadow", "0px 1px 3px rgba(0,0,0,0.2)")
				.addProperty("text-align", "left");
		style.get(".menu")
				.addProperty("position", "absolute")
				.addProperty("top", "0")
				.addProperty("left", "0")
				.addProperty("width", "100%");
		style.get(".menu li")
				.addProperty("margin", "0px")
				.addProperty("list-style", "none")
				.addProperty("font-family", "'Ek Mukta'");
		style.get(".menu .arrow")
				.addProperty("font-size", "11px")
				.addProperty("line-height", "0%");
		style.get(".menu a")
				.addProperty("transition", "all linear 0.15s");
		style.get(".menu li:hover > a, .menu")
				.addProperty("text-decoration", "none")
				.addProperty("color", "white");
		style.get(".menu > ul > li")
				.addProperty("display", "inline-block")
				.addProperty("position", "relative")
				.addProperty("font-size", "18px");
		style.get(".menu li:hover .sub-menu")
				.addProperty("z-index", "1")
				.addProperty("opacity", "1");
		style.get(".sub-menu")
				.addProperty("width", "100%")
				.addProperty("padding", "5px 0px")
				.addProperty("position", "absolute")
				.addProperty("top", "100%")
				.addProperty("left", "0px")
				.addProperty("z-index", "-1")
				.addProperty("opacity", "0")
				.addProperty("transition", "opacity linear 0.15s");
		style.get(".clearfix > li > a")
				.addProperty("color", "white");
		style.get(".menu > ul > li > a")
				.addProperty("padding", "5px 40px")
				.addProperty("display", "inline-block")
				.addProperty("text-shadow", "0px 1px 0px rgba(0, 0, 0, 0.4)");
		style.get(".menu > ul > li:hover > a")
				.addProperty("bottom", "auto")
				.addProperty("border-bottom-color", "white")
				.addProperty("border-bottom-style", "solid")
				.addProperty("border-bottom-width", "thin");
		style.get(".menu > ul > li:hover > a, .menu > ul > .current-item > a")
				.addProperty("background", "rgba(128, 128, 128, 0.1)");
		style.get(".sub-menu")
				.addProperty("width", "100%")
				.addProperty("padding", "3px 0px")
				.addProperty("position", "absolute")
				.addProperty("top", "100%")
				.addProperty("left", "0px")
				.addProperty("z-index", "-1")
				.addProperty("opacity", "0")
				.addProperty("transition", "opacity linear 0.15s")
				.addProperty("box-shadow", "0px 2px 3px rgba(0,0,0,0.2)")
				.addProperty("background", "rgba(128,128,128,0.1)")
				.addProperty("color", "white");
		style.get(".sub-menu li")
				.addProperty("display", "block")
				.addProperty("font-size", "16px");
		style.get(".sub-menu li a")
				.addProperty("padding", "1px 30px")
				.addProperty("display", "block");
		style.get(".sub-menu li a:hover, .sub.menu .current-item a")
				.addProperty("background", "gray");
		
		HTMLElement ul = HTMLElement.ul().addClass("clearfix");
		
		HTMLElement div = HTMLElement.div().addClass("menu-wrap").addChild(
				HTMLElement.nav().addClass("menu")
					.addChild(ul));
		
		for(NavbarElement el : links) {
			if(el instanceof NavbarDropdown) {
				NavbarDropdown dr = (NavbarDropdown) el;
				HTMLElement sl = HTMLElement.ul().addClass("sub-menu");
				for(Map.Entry<String, String> en : dr.dropdownElements.entrySet()) {
					sl.addChild(HTMLElement.li().addChild(HTMLElement.a(en.getKey(), en.getValue())));
				}
				ul.addChild(HTMLElement.li()
						.addChild(HTMLElement.a(dr.getName()))
						.addChild(sl));
			}else if(el instanceof LinkNavbarElement) {
				LinkNavbarElement ln = (LinkNavbarElement) el;
				ul.addChild(HTMLElement.li().addChild(HTMLElement.a(ln.getName(), ln.link)));
			}else throw new RuntimeException();
		}
		
		doc.addElement(div);
		
		return doc;
	}

	@SuppressWarnings("unused")
	public class HtmlHeaderImage {
		
		private HTMLHeaderBuilder builder;
		private Position pos;
		private BufferedImage image;
		private int xOffset, yOffset, width, height;
		
		public HtmlHeaderImage(HTMLHeaderBuilder builder, BufferedImage image, Position pos) {
			this.builder = builder;
			this.image = image;
			this.pos = pos;
		}
		
		public HtmlHeaderImage offset(int xOffset, int yOffset) {
			this.xOffset = xOffset;
			this.yOffset = yOffset;
			return this;
		}
		
		public HtmlHeaderImage xOffset(int offset) {
			this.xOffset = offset;
			return this;
		}
		
		public HtmlHeaderImage yOffset(int offset) {
			this.yOffset = offset;
			return this;
		}
		
		public HtmlHeaderImage scaleWidth(int width) {
			this.width = width;
			this.height = (int) ((image.getHeight() / (double) image.getWidth()) * width);
			return this;
		}
		
		public HtmlHeaderImage scaleHeight(int height) {
			this.height = height;
			this.width = (int) ((image.getWidth() / (double) image.getHeight()) * height);
			return this;
		}
		
		public HtmlHeaderImage rescale(int width, int height) {
			this.width = width;
			this.height = height;
			return this;
		}
		
		public HtmlHeaderImage rescale(double factor) {
			this.width = (int) (factor * image.getWidth());
			this.height = (int) (factor * image.getHeight());
			return this;
		}
		
		public HtmlHeaderImage width(int width) {
			this.width = width;
			return this;
		}
		
		public HtmlHeaderImage height(int height) {
			this.height = height;
			return this;
		}
		
		public HTMLHeaderBuilder done() {
			return builder;
		}
		
	}
	
	public abstract class NavbarElement {
		
		private String name;
		
		public NavbarElement(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
		
	}
	
	public class LinkNavbarElement extends NavbarElement {
		
		private String link;

		public LinkNavbarElement(String name, String link) {
			super(name);
			this.link = link;
		}
		
	}
	
	public class NavbarDropdown extends NavbarElement {

		private Map<String, String> dropdownElements;
		private HTMLHeaderBuilder builder;
		
		public NavbarDropdown(HTMLHeaderBuilder builder, String name) {
			super(name);
			this.builder = builder;
			this.dropdownElements = new LinkedHashMap<>();
		}
		
		public NavbarDropdown addElement(String name, String link) {
			dropdownElements.put(name, link);
			return this;
		}
		
		public HTMLHeaderBuilder done() {
			return builder;
		}
		
	}
	
	public enum Position{
		LEFT,
		CENTER,
		RIGHT;
	}
	
}
