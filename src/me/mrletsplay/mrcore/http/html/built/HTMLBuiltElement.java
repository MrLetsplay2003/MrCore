package me.mrletsplay.mrcore.http.html.built;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import me.mrletsplay.mrcore.http.html.HTMLElement;

public class HTMLBuiltElement {

	private HTMLElement element;
	private Map<String, String> attributes;
	private List<HTMLBuiltElement> children;
	
	public HTMLBuiltElement(HTMLElement element) {
		this.element = element;
		this.attributes = new LinkedHashMap<>();
		this.children = new ArrayList<>();
	}
	
	public HTMLElement getElement() {
		return element;
	}
	
	public void addAttribute(String key, String value) {
		this.attributes.put(key, value);
	}
	
	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void addChild(HTMLBuiltElement child) {
		this.children.add(child);
	}
	
	public List<HTMLBuiltElement> getChildren() {
		return children;
	}
	
}
