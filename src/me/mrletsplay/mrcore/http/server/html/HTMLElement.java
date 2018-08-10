package me.mrletsplay.mrcore.http.server.html;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.http.server.css.CSSStyleElement;
import me.mrletsplay.mrcore.http.server.html.HTMLDocument.HttpSiteAccessedEvent;
import me.mrletsplay.mrcore.http.server.html.HTMLElementTextInput.TextInputType;
import me.mrletsplay.mrcore.http.server.html.built.HTMLBuiltDocument;
import me.mrletsplay.mrcore.http.server.html.built.HTMLBuiltElement;
import me.mrletsplay.mrcore.http.server.js.JSFunction;
import me.mrletsplay.mrcore.misc.Condition;

public class HTMLElement {

	private String type, id;
	private List<String> classes;
	private List<HTMLElement> children;
	private HTMLElement parent;
	private CSSStyleElement css;
	private Condition<HttpSiteAccessedEvent> condition;
	protected Function<HTMLElementGetContentEvent, String> getContentMethod;
	private Function<HttpSiteAccessedEvent, List<HTMLElement>> getChildrenMethod;
	private int sortingIndex, contentSortingIndex;
	private Map<String, String> attributes;
	
	private OnHover onHover;
	private OnClicked onClicked;

	protected HTMLElement(String type, Function<HTMLElementGetContentEvent, String> getContentMethod) {
		this.type = type;
		this.getContentMethod = getContentMethod;
		this.id = null;
		this.classes = new ArrayList<>();
		this.children = new ArrayList<>();
		this.onClicked = new OnClicked();
		this.onHover = new OnHover();
		this.css = new CSSStyleElement();
		this.attributes = new LinkedHashMap<>();
	}
	
	protected HTMLElement(String type, String content) {
		this.type = type;
		this.getContentMethod = (event) -> content;
		this.id = null;
		this.classes = new ArrayList<>();
		this.children = new ArrayList<>();
		this.onClicked = new OnClicked();
		this.onHover = new OnHover();
		this.css = new CSSStyleElement();
		this.attributes = new LinkedHashMap<>();
	}
	
	protected HTMLElement(HTMLElement parent, HTMLElement element) {
		this.type = element.type;
		this.id = element.id;
		this.getContentMethod = element.getContentMethod;
		this.getChildrenMethod = element.getChildrenMethod;
		this.classes = new ArrayList<>(element.classes);
		this.children = new ArrayList<>(element.children).stream().map(e -> new HTMLElement(this, e)).collect(Collectors.toList());
		this.onClicked = element.onClicked.clone();
		this.onHover = element.onHover.clone();
		this.css = element.css.clone();
		this.condition = element.condition;
		this.parent = parent;
		this.sortingIndex = element.sortingIndex;
		this.contentSortingIndex = element.contentSortingIndex;
		this.attributes = new LinkedHashMap<>(element.attributes);
	}
	
	protected HTMLElement(HTMLElement element) {
		this.type = element.type;
		this.id = element.id;
		this.getContentMethod = element.getContentMethod;
		this.getChildrenMethod = element.getChildrenMethod;
		this.classes = new ArrayList<>(element.classes);
		this.children = new ArrayList<>(element.children).stream().map(e -> new HTMLElement(this, e)).collect(Collectors.toList());
		this.onClicked = element.onClicked.clone();
		this.onHover = element.onHover.clone();
		this.css = element.css.clone();
		this.condition = element.condition;
		this.parent = element.parent != null ? element.parent.clone() : null;
		this.sortingIndex = element.sortingIndex;
		this.contentSortingIndex = element.contentSortingIndex;
		this.attributes = new LinkedHashMap<>(element.attributes);
	}
	
	public HTMLElement(String type) {
		this(type, (String) null);
	}
	
	public void copyProperties(HTMLElement other) {
		this.type = other.type;
		this.id = other.id;
		this.getContentMethod = other.getContentMethod;
		this.classes = new ArrayList<>(other.classes);
		this.children = new ArrayList<>(other.children);
		this.onClicked = other.onClicked.clone();
		this.onHover = other.onHover.clone();
		this.css = other.css.clone();
		this.condition = other.condition;
		this.parent = other.parent;
		this.attributes = new LinkedHashMap<>(other.attributes);
	}
	
	public void copyInner(HTMLElement other) {
		this.getContentMethod = other.getContentMethod;
		this.children = new ArrayList<>(other.children);
	}
	
	public HTMLElement addClasses(String... classes) {
		this.classes.addAll(Arrays.asList(classes));
		return this;
	}
	
	public HTMLElement addClass(String htmlClass) {
		classes.add(htmlClass);
		return this;
	}
	
	public HTMLElement addAttribute(String key, String value) {
		this.attributes.put(key, value);
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
	
	public HTMLElement setSortingIndex(int sortingIndex) {
		this.sortingIndex = sortingIndex;
		return this;
	}
	
	public HTMLElement setContentSortingIndex(int contentSortingIndex) {
		this.contentSortingIndex = contentSortingIndex;
		return this;
	}
	
	public String getID() {
		return id;
	}
	
	public HTMLElement getElementByID(String id) {
		if(id.equals(this.id)) return this;
		return children.stream().map(c -> c.getElementByID(id)).filter(e -> e != null).findFirst().orElse(null);
	}
	
	public String getType() {
		return type;
	}
	
	public String getContent(HTMLElementGetContentEvent event) {
		return getContentMethod.apply(event);
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
	
	public List<HTMLElement> getAllChildren(HttpSiteAccessedEvent event) {
		List<HTMLElement> ch = new ArrayList<>(children);
		if(getChildrenMethod != null) ch.addAll(getChildrenMethod.apply(event));
		return ch;
	}
	
	public List<String> getClasses() {
		return classes;
	}
	
	public Map<String, String> getAttributes() {
		return attributes;
	}
	
	public OnHover onHover() {
		return onHover;
	}
	
	public OnClicked onClicked() {
		return onClicked;
	}
	
	public int getSortingIndex() {
		return sortingIndex;
	}
	
	public int getContentSortingIndex() {
		return contentSortingIndex;
	}
	
	public HTMLElement onHover(OnHover onHover) {
		this.onHover = onHover;
		return this;
	}
	
	public HTMLElement onClicked(OnClicked onClicked) {
		this.onClicked = onClicked;
		return this;
	}
	
	public HTMLElement clone() {
		return new HTMLElement(this);
	}
	
	public HTMLBuiltElement build(HTMLBuiltElement parent, HTMLBuiltDocument doc, String id, HttpSiteAccessedEvent event, String... params) {
		return new HTMLBuiltElement(parent, doc, this, id, event, params);
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
		return new HTMLElement("a", a).addAttribute("href", href);
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
	
	public static HTMLElement div(String text) {
		return new HTMLElement("div", text);
	}
	
	public static HTMLElement img(String src) {
		return new HTMLElement("img").addAttribute("src", src);
	}
	
	public static HTMLElementTextInput inputText() {
		return new HTMLElementTextInput(TextInputType.PLAIN);
	}
	
	public static HTMLElementTextInput inputText(String placeholder) {
		return new HTMLElementTextInput(TextInputType.PLAIN, placeholder);
	}
	
	public static HTMLElementTextInput inputPassword() {
		return new HTMLElementTextInput(TextInputType.PASSWORD);
	}
	
	public static HTMLElementTextInput inputPassword(String placeholder) {
		return new HTMLElementTextInput(TextInputType.PASSWORD, placeholder);
	}
	
	public static HTMLElementCheckInput inputCheckBox() {
		return new HTMLElementCheckInput();
	}
	
	public static HTMLElement button(String text) {
		return new HTMLElement("button", text);
	}
	
	public static HTMLElement script(String js) {
		return new HTMLElement("script", js);
	}
	
	public static HTMLElement raw(String raw) {
		return new HTMLElement(null, raw);
	}
	
	public static HTMLElement table() {
		return new HTMLElement("table");
	}
	
	public static HTMLElement thead() {
		return new HTMLElement("thead");
	}
	
	public static HTMLElement tbody() {
		return new HTMLElement("tbody");
	}
	
	public static HTMLElement th(String title) {
		return new HTMLElement("th", title);
	}
	
	public static HTMLElement tr() {
		return new HTMLElement("tr");
	}
	
	public static HTMLElement td() {
		return new HTMLElement("td");
	}
	
	public static HTMLElement td(String text) {
		return new HTMLElement("td", text);
	}
	
	public static <T extends HTMLElement> T dynamic(T element, Function<HTMLElementGetContentEvent, String> getContentMethod) {
//		HTMLElement el =  element.clone(); TODO
		element.getContentMethod = getContentMethod;
		return element;
	}
	
	public static HTMLElement dynamic(HTMLElement element, Function<HTMLElementGetContentEvent, String> getContentMethod, Function<HttpSiteAccessedEvent, List<HTMLElement>> getChildrenMethod) {
		HTMLElement el =  new HTMLElement(element);
		el.getContentMethod = getContentMethod;
		el.getChildrenMethod = getChildrenMethod;
		return el;
	}
	
	public static HTMLElement i(String text) {
		return new HTMLElement("i", text);
	}
	
	public static HTMLElement i() {
		return new HTMLElement("i");
	}
	
	public static HTMLElementLabel label(HTMLElement forElement) {
		return new HTMLElementLabel(forElement);
	}
	
	public static HTMLElementLabel label(HTMLElement forElement, String text) {
		return new HTMLElementLabel(forElement, text);
	}
	
	public static HTMLElement textarea(int rows, int cols) {
		return new HTMLElement("textarea")
				.addAttribute("rows", "" + rows)
				.addAttribute("cols", "" + cols);
	}
	
	public static HTMLElement textareaReadOnly(int rows, int cols) {
		return new HTMLElement("textarea")
				.addAttribute("rows", "" + rows)
				.addAttribute("cols", "" + cols)
				.addAttribute("readonly", "readonly");
	}
	
	public static class OnHover {
		
		private CSSStyleElement style;
		private JSFunction function;
		
		public OnHover() {
			this.style = new CSSStyleElement();
		}
		
		public OnHover(OnHover from) {
			this.style = from.style;
			this.function = from.function;
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
		
		public OnHover clone() {
			return new OnHover(this);
		}
		
	}
	
	public static class OnClicked {
		
		private CSSStyleElement style;
		private JSFunction function;
		
		public OnClicked() {
			this.style = new CSSStyleElement();
		}
		
		public OnClicked(OnClicked from) {
			this.style = from.style;
			this.function = from.function;
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
		
		public OnClicked clone() {
			return new OnClicked(this);
		}
		
	}
	
	public static interface HTMLElementEvent {

		public String getElementID();
		
		public HTMLBuiltElement getElement();
		
	}
	
	public static class HTMLElementGetContentEvent implements HTMLElementEvent {
		
		private HTMLBuiltElement element;
		private HttpSiteAccessedEvent accessEvent;
		
		public HTMLElementGetContentEvent(HTMLBuiltElement element, HttpSiteAccessedEvent accessEvent) {
			this.element = element;
			this.accessEvent = accessEvent;
		}

		@Override
		public String getElementID() {
			return element.getID();
		}

		@Override
		public HTMLBuiltElement getElement() {
			return element;
		}
		
		public HttpSiteAccessedEvent getAccessEvent() {
			return accessEvent;
		}
		
	}
	
}
