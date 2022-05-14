package me.mrletsplay.mrcore.bukkitimpl.ui;

import java.util.HashMap;

public class UILayoutElement {
	
	private UILayoutElementType type;
	private HashMap<String, Object> properties;
	
	public UILayoutElement(UILayoutElementType type) {
		this.type = type;
		this.properties = new HashMap<>();
	}
	
	public UILayoutElementType getType() {
		return type;
	}
	
	public UILayoutElement setProperty(String key, Object value) {
		properties.put(key, value);
		return this;
	}
	
	public Object getProperty(String key) {
		return properties.get(key);
	}
	
}
