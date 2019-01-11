package me.mrletsplay.mrcore.http.css;

import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.mrcore.http.css.built.CSSBuiltElement;
import me.mrletsplay.mrcore.http.css.built.CSSBuiltStyleSheet;
import me.mrletsplay.mrcore.http.html.HTMLDocumentBuildEvent;

public class CSSStyleSheet {

	private List<CSSElement> elements;
	
	public CSSStyleSheet() {
		this.elements = new ArrayList<>();
	}
	
	public List<? extends CSSElement> getElements() {
		return elements;
	}
	
	public void addElement(CSSElement element) {
		elements.add(element);
	}
	
	public void removeElement(CSSElement element) {
		elements.add(element);
	}
	
	public CSSBuiltStyleSheet build(HTMLDocumentBuildEvent event) {
		CSSBuiltStyleSheet bS = new CSSBuiltStyleSheet(this);
		for(CSSElement el : elements) {
			CSSBuiltElement elm = el.build(event);
			if(elm != null) bS.addElement(elm);
		}
		return bS;
	}
	
}
