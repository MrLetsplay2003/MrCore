package me.mrletsplay.mrcore.http.server.html.built;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.http.server.html.HTMLDocument.HttpSiteAccessedEvent;
import me.mrletsplay.mrcore.http.server.HttpClientPoll;
import me.mrletsplay.mrcore.http.server.html.HTMLElement;
import me.mrletsplay.mrcore.http.server.html.HTMLElement.OnClicked;
import me.mrletsplay.mrcore.http.server.html.HTMLElement.OnHover;
import me.mrletsplay.mrcore.http.server.js.built.JSBuiltFunction;

public class HTMLBuiltElement {

	private HTMLElement base;
	private String id;
	private String content;
	private List<HTMLBuiltElement> children;
	private HTMLBuiltElement parent;
	private HTMLBuiltDocument document;
	private BuiltOnClicked onClicked;
	private BuiltOnHover onHover;
	private Map<String, String> attributes;
	
	public HTMLBuiltElement(HTMLBuiltElement parent, HTMLBuiltDocument doc, HTMLElement base, String id, HttpSiteAccessedEvent event, String... params) {
		this.parent = parent;
		this.base = base;
		this.id = id;
		this.content = base.getContent(event);
		for(int i = 0; i < params.length; i+=2) {
			content = content.replace(params[i], params[i+1]);
		}
		this.children = new ArrayList<>();
		this.attributes = new LinkedHashMap<>(base.getAttributes());
		this.document = doc;
		for(HTMLElement c : base.getAllChildren(event)) {
			if(c.getCondition() == null || c.getCondition().apply(event)) children.add(c.build(this, doc, c.getID() == null ? doc.randomElementID() : c.getID(), event));
		}
		if(!base.css().isEmpty()) doc.getCSS().addElement("#" + id, base.css());
		this.onClicked = new BuiltOnClicked(base.onClicked(), this);
		this.onHover = new BuiltOnHover(base.onHover(), this);
	}
	
	public void callOnBuildFinished() {
		onBuildFinished();
		children.forEach(HTMLBuiltElement::callOnBuildFinished);
	}
	
	public void onBuildFinished() {}
	
	public HTMLElement getBase() {
		return base;
	}
	
	public String getID() {
		return id;
	}
	
	public HTMLBuiltElement getParent() {
		return parent;
	}
	
	public HTMLBuiltDocument getDocument() {
		return document;
	}
	
	public void addAttribute(String key, String value) {
		this.attributes.put(key, value);
	}
	
	public Map<String, String> getAttributes() {
		return attributes;
	}
	
	public HTMLBuiltElement findElement(HTMLElement el) {
		if(el.equals(this.base)) return this;
		return children.stream().map(c -> c.findElement(el)).filter(e -> e != null).findFirst().orElse(null);
	}
	
	public HTMLBuiltElement findElementByID(String id) {
		if(id.equals(this.id)) return this;
		return children.stream().map(c -> c.findElementByID(id)).filter(e -> e != null).findFirst().orElse(null);
	}
	
	public void pollContentChange(String newContent) {
		document.getConnection().addPoll(HttpClientPoll.setContent(id, newContent));
	}
	
	public CharSequence asString() {
		StringBuilder builder = new StringBuilder();
		if(base.getType() != null) {
			builder.append("<").append(base.getType()).append(" id=\"").append(id).append("\"");
			if(!base.getClasses().isEmpty()) builder.append(" class=\"").append(base.getClasses().stream().collect(Collectors.joining(" "))).append("\"");
			if(onClicked.getFunction() != null) builder.append(" onclick=\"").append(onClicked.getFunction().getName()).append("(this)\"");
			if(onHover.getFunction() != null) builder.append(" onmouseover=\"").append(onHover.getFunction().getName()).append("(this)\"");
			attributes.entrySet().forEach(attr -> builder.append(" ").append(attr.getKey()).append("=\"").append(attr.getValue()).append("\""));
			builder.append(">");
		}
		List<Map.Entry<Integer, CharSequence>> strs = new ArrayList<>();
		if(content != null) strs.add(new AbstractMap.SimpleEntry<>(base.getContentSortingIndex(), content));
		children.forEach(c -> strs.add(new AbstractMap.SimpleEntry<>(c.base.getSortingIndex(), c.asString())));
		strs.stream().sorted((o1, o2) -> o1.getKey() - o2.getKey()).map(en -> en.getValue()).collect(Collectors.toList()).forEach(builder::append);
		if(base.getType() != null) {
			builder.append("</").append(base.getType()).append(">");
		}
		return builder;
	}
	
	public static class BuiltOnHover {
		
		private JSBuiltFunction function;
		
		public BuiltOnHover(OnHover base, HTMLBuiltElement element) {
			if(!base.css().isEmpty()) element.getDocument().getCSS().addElement("#" + element.getID() + ":hover", base.css());
			if(base.getFunction() != null) function = element.getDocument().getScript().appendFunction(base.getFunction());
		}
		
		public JSBuiltFunction getFunction() {
			return function;
		}
		
	}
	
	public static class BuiltOnClicked {
		
		private JSBuiltFunction function;
		
		public BuiltOnClicked(OnClicked base, HTMLBuiltElement element) {
			if(!base.css().isEmpty()) element.getDocument().getCSS().addElement("#" + element.getID() + ":active", base.css());
			if(base.getFunction() != null) function = element.getDocument().getScript().appendFunction(base.getFunction());
		}
		
		public JSBuiltFunction getFunction() {
			return function;
		}
		
	}

}
