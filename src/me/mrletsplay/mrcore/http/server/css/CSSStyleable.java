package me.mrletsplay.mrcore.http.server.css;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import me.mrletsplay.mrcore.http.server.html.HTMLDocument.HttpSiteAccessedEvent;

public abstract class CSSStyleable {
	
	private Map<String, Function<HttpSiteAccessedEvent, String>> properties;
	
	public CSSStyleable() {
		this.properties = new LinkedHashMap<>();
	}
	
	public CSSStyleable(CSSStyleable from) {
		this.properties = new LinkedHashMap<>(from.properties);
	}
	
	public CSSStyleable addProperty(String key, String value) {
		return addProperty(key, event -> value);
	}
	
	public CSSStyleable addProperty(String key, Function<HttpSiteAccessedEvent, String> value) {
		properties.put(key, value);
		return this;
	}
	
	public CSSStyleable position(String type, String top, String left, String width, String height) {
		return 
				 addProperty("position", type)
				.addProperty("top", top)
				.addProperty("left", left)
				.addProperty("width", width)
				.addProperty("height", height);
	}
	
	public CSSStyleable dimensions(String width, String height) {
		return 
				addProperty("width", width)
				.addProperty("height", height);
	}
	
	public void setProperties(Map<String, Function<HttpSiteAccessedEvent, String>> properties) {
		this.properties = properties;
	}
	
	public Map<String, Function<HttpSiteAccessedEvent, String>> getProperties() {
		return properties;
	}
	
	public boolean isEmpty() {
		return properties.isEmpty();
	}
	
}
