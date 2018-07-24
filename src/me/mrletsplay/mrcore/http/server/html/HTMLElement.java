package me.mrletsplay.mrcore.http.server.html;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.mrletsplay.mrcore.http.server.css.CSSStyleElement;
import me.mrletsplay.mrcore.http.server.html.HTMLDocument.HttpSiteAccessedEvent;
import me.mrletsplay.mrcore.http.server.js.JSFunction;
import me.mrletsplay.mrcore.misc.Condition;

public class HTMLElement {

	private String type, content, id;
	private List<String> classes;
	private List<HTMLElement> children;
	private HTMLElement parent;
	private CSSStyleElement css;
	private Condition<HttpSiteAccessedEvent> condition;
	
	private OnHover onHover;
	private OnClicked onClicked;
	
	protected HTMLElement(String type, String content) {
		this.type = type;
		this.content = content;
		this.id = null;
		this.classes = new ArrayList<>();
		this.children = new ArrayList<>();
		this.onClicked = new OnClicked();
		this.onHover = new OnHover();
		this.css = new CSSStyleElement();
	}
	
	public HTMLElement(String type) {
		this(type, "");
	}
	
	public HTMLElement addClass(String htmlClass) {
		classes.add(htmlClass);
		return this;
	}
	
	public HTMLElement setID(String id) {
		this.id = id;
		return this;
	}
	
	public CSSStyleElement css() {
		return css;
	}
	
	public HTMLElement css(CSSStyleElement css) {
		this.css = css;
		return this;
	}
	
	public HTMLElement condition(Condition<HttpSiteAccessedEvent> condition) {
		this.condition = condition;
		return this;
	}
	
	public String getID() {
		return id;
	}
	
	public String getType() {
		return type;
	}
	
	public String getContent() {
		return content;
	}
	
	public Condition<HttpSiteAccessedEvent> getCondition() {
		return condition;
	}
	
	public HTMLElement addChild(HTMLElement child) {
		child.parent = this;
		children.add(child);
		return this;
	}
	
	public HTMLElement removeChild(HTMLElement child) {
		if(!children.contains(child)) return this;
		child.parent = null;
		children.remove(child);
		return this;
	}
	
	public HTMLElement getParent() {
		return parent;
	}
	
	public List<HTMLElement> getChildren() {
		return children;
	}
	
	public void addClasses(String... classes) {
		this.classes.addAll(Arrays.asList(classes));
	}
	
	public List<String> getClasses() {
		return classes;
	}
	
	public OnHover onHover() {
		return onHover;
	}
	
	public OnClicked onClicked() {
		return onClicked;
	}
	
	public HTMLElement onHover(OnHover onHover) {
		this.onHover = onHover;
		return this;
	}
	
	public HTMLElement onClicked(OnClicked onClicked) {
		this.onClicked = onClicked;
		return this;
	}
	
	public static HTMLElement h1(String h1) {
		return new HTMLElement("h1", h1);
	}
	
	public static HTMLElement h2(String h2) {
		return new HTMLElement("h2", h2);
	}
	
	public static HTMLElement h3(String h3) {
		return new HTMLElement("h3", h3);
	}
	
	public static HTMLElement p(String p) {
		return new HTMLElement("p", p);
	}
	
	public static HTMLElement a(String a) {
		return new HTMLElement("a", a);
	}
	
	public static HTMLElement a(String a, String href) {
		return new HTMLElement("a href=\"" + href + "\"", a);
	}
	
	public static HTMLElement nav() {
		return new HTMLElement("nav");
	}
	
	public static HTMLElement ul() {
		return new HTMLElement("ul");
	}
	
	public static HTMLElement li() {
		return new HTMLElement("li");
	}
	
	public static HTMLElement div() {
		return new HTMLElement("div");
	}
	
	public static HTMLElement img(String src) {
		return new HTMLElement("img src=\"" + src + "\"");
	}
	
	public static HTMLElement inputText() {
		return new HTMLElement("input type=\"text\"");
	}
	
	public static HTMLElement inputText(String placeholder) {
		return new HTMLElement("input type=\"text\" placeholder=\""+placeholder+"\"");
	}
	
	public static HTMLElement inputPassword() {
		return new HTMLElement("input type=\"password\"");
	}
	
	public static HTMLElement inputPassword(String placeholder) {
		return new HTMLElement("input type=\"password\" placeholder=\""+placeholder+"\"");
	}
	
	public static HTMLElement button(String text) {
		return new HTMLElement("button", text);
	}
	
	public static HTMLElement raw(String raw) {
		return new HTMLElement(null, raw);
	}
	
	public static class OnHover {
		
		private CSSStyleElement style;
		private JSFunction function;
		
		public OnHover() {
			this.style = new CSSStyleElement();
		}
		
		public CSSStyleElement css() {
			return style;
		}
		
		public OnHover function(JSFunction function) {
			this.function = function;
			return this;
		}
		
		public JSFunction getFunction() {
			return function;
		}
		
	}
	
	public static class OnClicked {
		
		private CSSStyleElement style;
		private JSFunction function;
		
		public OnClicked() {
			this.style = new CSSStyleElement();
		}
		
		public CSSStyleElement css() {
			return style;
		}
		
		public OnClicked function(JSFunction function) {
			this.function = function;
			return this;
		}
		
		public JSFunction getFunction() {
			return function;
		}
		
	}
	
}
