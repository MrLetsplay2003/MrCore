package me.mrletsplay.mrcore.http.css.builder;

import me.mrletsplay.mrcore.http.css.CSSElement;
import me.mrletsplay.mrcore.http.css.CSSProperty;
import me.mrletsplay.mrcore.http.html.HTMLDocumentBuildEvent;
import me.mrletsplay.mrcore.http.server.HttpDynamicValue;

public class CSSElementEdit{

	private CSSElement element;
	
	public CSSElementEdit(CSSElement element) {
		this.element = element;
	}
	
	public CSSElementEdit addProperty(String name, String value) {
		element.addProperty(new CSSProperty(name, value));
		return this;
	}
	
	public CSSElementEdit addProperty(String name, HttpDynamicValue<HTMLDocumentBuildEvent, String> value) {
		element.addProperty(new CSSProperty(name, value));
		return this;
	}
	
}
