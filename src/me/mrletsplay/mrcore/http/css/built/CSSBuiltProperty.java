package me.mrletsplay.mrcore.http.css.built;

import me.mrletsplay.mrcore.http.css.CSSProperty;

public class CSSBuiltProperty {

	private CSSProperty property;
	private String name, value;
	
	public CSSBuiltProperty(CSSProperty property, String name, String value) {
		this.property = property;
		this.name = name;
		this.value = value;
	}
	
	public CSSProperty getProperty() {
		return property;
	}
	
	public String getName() {
		return name;
	}
	
	public String getValue() {
		return value;
	}
	
	public String asString() {
		StringBuilder b = new StringBuilder();
		b.append(name).append(':').append(value).append(';');
		return b.toString();
	}
	
}
