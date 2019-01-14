package me.mrletsplay.mrcore.http.html.built;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.common.html.HtmlEscapers;

import me.mrletsplay.mrcore.http.html.HTMLElement;

public class HTMLBuiltElement {

	private HTMLElement element;
	private String type, content;
	private Map<String, String> attributes;
	private List<HTMLBuiltElement> children;
	
	public HTMLBuiltElement(HTMLElement element) {
		this.element = element;
		this.content = "";
		this.attributes = new LinkedHashMap<>();
		this.children = new ArrayList<>();
	}
	
	public HTMLElement getElement() {
		return element;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	
	public void addAttribute(String key, String value) {
		this.attributes.put(key, value);
	}
	
	public String getAttribute(String name) {
		return attributes.get(name);
	}
	
	public Map<String, String> getAttributes() {
		return attributes;
	}
	
	public String getID() {
		return getAttribute("id");
	}
	
	public List<String> getClasses() {
		return Arrays.asList(getAttribute("class").split(" "));
	}

	public void addChild(HTMLBuiltElement child) {
		this.children.add(child);
	}
	
	public List<HTMLBuiltElement> getChildren() {
		return children;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public boolean isEmpty() {
		return (content == null || content.isEmpty()) && attributes.isEmpty() && children.isEmpty();
	}
	
	public String asString() {
		StringBuilder b = new StringBuilder();
		b.append("<").append(type).append(" ");
		for(Map.Entry<String, String> attr : attributes.entrySet()) {
			b.append(attr.getKey()).append("=").append("\"").append(HtmlEscapers.htmlEscaper().escape(attr.getValue())).append("\"").append(" ");
		}
		b.deleteCharAt(b.length() - 1);
		b.append(">");
		if(content != null) b.append(content);
		for(HTMLBuiltElement el : children) {
			b.append(el.asString());
		}
		b.append("</").append(type).append(">");
		return b.toString();
	}
	
}
