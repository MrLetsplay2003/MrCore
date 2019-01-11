package me.mrletsplay.mrcore.http.css.built;

import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.mrcore.http.css.CSSStyleSheet;

public class CSSBuiltStyleSheet {
	
	private CSSStyleSheet styleSheet;
	private List<CSSBuiltElement> elements;

	public CSSBuiltStyleSheet(CSSStyleSheet styleSheet) {
		this.styleSheet = styleSheet;
		this.elements = new ArrayList<>();
	}
	
	public CSSStyleSheet getStyleSheet() {
		return styleSheet;
	}
	
	public void addElement(CSSBuiltElement element) {
		elements.add(element);
	}
	
	public List<CSSBuiltElement> getElements() {
		return elements;
	}
	
	public String asString() {
		StringBuilder b = new StringBuilder();
		for(CSSBuiltElement el : elements) {
			b.append(el.asString());
		}
		return b.toString();
	}
	
}
