package me.mrletsplay.mrcore.http.css;

import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.mrcore.http.css.builder.CSSElementEdit;
import me.mrletsplay.mrcore.http.css.built.CSSBuiltElement;
import me.mrletsplay.mrcore.http.css.built.CSSBuiltProperty;
import me.mrletsplay.mrcore.http.html.HTMLDocumentBuildEvent;

public class CSSElement {

	private String target, selector;
	private List<CSSProperty> properties;
	
	public CSSElement(String target, String selector) {
		this.target = target;
		this.selector = selector;
		this.properties = new ArrayList<>();
	}
	
	public String getTarget() {
		return target;
	}
	
	public String getSelector() {
		return selector;
	}
	
	public void addProperty(CSSProperty property) {
		properties.add(property);
	}
	
	public List<? extends CSSProperty> getProperties() {
		return properties;
	}
	
	public CSSElementEdit edit() {
		return new CSSElementEdit(this);
	}
	
	public CSSBuiltElement build(HTMLDocumentBuildEvent event) {
		CSSBuiltElement el = new CSSBuiltElement(this, target, selector);
		for(CSSProperty p : properties) {
			CSSBuiltProperty pr = p.build(event);
			if(pr != null) el.addProperty(pr);
		}
		return el;
	}
	
}
