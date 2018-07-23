package me.mrletsplay.mrcore.http.server.css;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CSSStyleElement implements Cloneable {

	private Map<String, String> properties;
	
	public CSSStyleElement() {
		this.properties = new LinkedHashMap<>();
	}
	
	public CSSStyleElement(Map<String, String> properties) {
		this.properties = properties;
	}
	
	public CSSStyleElement addProperty(String key, String value) {
		properties.put(key, value);
		return this;
	}
	
	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
	
	public Map<String, String> getProperties() {
		return properties;
	}
	
	public boolean isEmpty() {
		return properties.isEmpty();
	}
	
	public String asString(String target) {
		return
				target + "{" +
				properties.entrySet().stream().map(en -> en.getKey() + ": " + en.getValue() + ";").collect(Collectors.joining()) +
				"}";
	}
	
	@Override
	public CSSStyleElement clone() {
		return new CSSStyleElement(properties);
	}
	
}
