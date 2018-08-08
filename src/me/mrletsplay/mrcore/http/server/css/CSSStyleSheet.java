package me.mrletsplay.mrcore.http.server.css;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.http.server.html.HTMLDocument.HttpSiteAccessedEvent;
import me.mrletsplay.mrcore.http.server.html.built.HTMLBuiltDocument;

public class CSSStyleSheet implements Cloneable {
	
	private Map<String, CSSStyleElement> elements;
	
	public CSSStyleSheet() {
		this.elements = new LinkedHashMap<>();
	}
	
	public CSSStyleSheet(Map<String, CSSStyleElement> elements) {
		this.elements = new LinkedHashMap<>(elements);
	}
	
	public CSSStyleSheet addElement(String key, CSSStyleElement element) {
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
	
	public String asString(HttpSiteAccessedEvent event, HTMLBuiltDocument context) {
		return elements.entrySet().stream().map(en -> en.getValue().asString(en.getKey(), event, context)).collect(Collectors.joining());
	}
	
	public void appendStylesheet(CSSStyleSheet style) {
		elements.putAll(style.elements);
	}
	
	@Override
	public CSSStyleSheet clone() {
		return new CSSStyleSheet(elements);
	}
	
}
