package me.mrletsplay.mrcore.http.server.css;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CSSStylesheet implements Cloneable {
	
	private Map<String, CSSStyleElement> elements;
	
	public CSSStylesheet() {
		this.elements = new LinkedHashMap<>();
	}
	
	public CSSStylesheet(Map<String, CSSStyleElement> elements) {
		this.elements = new LinkedHashMap<>(elements);
	}
	
	public CSSStylesheet addElement(String key, CSSStyleElement element) {
		elements.put(key, element);
		return this;
	}
	
	public CSSStyleElement get(String query) {
		if(elements.containsKey(query)) {
			return elements.get(query);
		}else {
			CSSStyleElement el = new CSSStyleElement();
			elements.put(query, el);
			return el;
		}
	}
	
	public CSSStyleElement getType(String type) {
		return get(type);
	}
	
	public CSSStyleElement getElement(String id) {
		return get("#"+id);
	}
	
	public CSSStyleElement getClass(String name) {
		return get("."+name);
	}
	
	public List<CSSStyleElement> getElements() {
		return new ArrayList<>(elements.values());
	}
	
	public String asString() {
		return elements.entrySet().stream().map(en -> en.getValue().asString(en.getKey())).collect(Collectors.joining("\n"));
	}
	
	public void appendStylesheet(CSSStylesheet style) {
		elements.putAll(style.elements);
	}
	
	@Override
	public CSSStylesheet clone() {
		return new CSSStylesheet(elements);
	}
	
}
