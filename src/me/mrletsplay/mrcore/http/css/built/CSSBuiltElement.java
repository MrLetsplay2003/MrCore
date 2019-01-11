package me.mrletsplay.mrcore.http.css.built;

import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.mrcore.http.css.CSSElement;

public class CSSBuiltElement {

	private CSSElement element;
	private String target, selector;
	private List<CSSBuiltProperty> properties;
	
	public CSSBuiltElement(CSSElement element, String target, String selector) {
		this.element = element;
		this.target = target;
		this.selector = selector;
		this.properties = new ArrayList<>();
	}
	
	public CSSElement getElement() {
		return element;
	}
	
	public String getTarget() {
		return target;
	}
	
	public String getSelector() {
		return selector;
	}
	
	public void addProperty(CSSBuiltProperty property) {
		properties.add(property);
	}
	
	public List<CSSBuiltProperty> getProperties() {
		return properties;
	}
	
	public String asString() {
		StringBuilder b = new StringBuilder();
		b.append(target).append(':').append(selector).append("{");
		for(CSSBuiltProperty p : properties) {
			b.append(p.asString());
		}
		b.append("}");
		return b.toString();
	}
	
}
