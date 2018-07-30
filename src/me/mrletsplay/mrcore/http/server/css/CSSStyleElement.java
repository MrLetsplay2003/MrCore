package me.mrletsplay.mrcore.http.server.css;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.http.server.html.HTMLDocument.HttpSiteAccessedEvent;

public class CSSStyleElement implements Cloneable {

	private Map<String, Function<HttpSiteAccessedEvent, String>> properties;
	
	public CSSStyleElement() {
		this.properties = new LinkedHashMap<>();
	}
	
	public CSSStyleElement(Map<String, Function<HttpSiteAccessedEvent, String>> properties) {
		this.properties = properties;
	}
	
	public CSSStyleElement addProperty(String key, String value) {
		return addProperty(key, event -> value);
	}
	
	public CSSStyleElement addProperty(String key, Function<HttpSiteAccessedEvent, String> value) {
		properties.put(key, value);
		return this;
	}
	
	public CSSStyleElement position(String type, String top, String left, String width, String height) {
		return 
				 addProperty("position", type)
				.addProperty("top", top)
				.addProperty("left", left)
				.addProperty("width", width)
				.addProperty("height", height);
	}
	
	public CSSStyleElement dimensions(String width, String height) {
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
	
	public String asString(String target, HttpSiteAccessedEvent event) {
		return
				target + "{" +
				properties.entrySet().stream().map(en -> en.getKey() + ": " + en.getValue().apply(event) + ";").collect(Collectors.joining()) +
				"}";
	}
	
	@Override
	public CSSStyleElement clone() {
		return new CSSStyleElement(properties);
	}
	
}
