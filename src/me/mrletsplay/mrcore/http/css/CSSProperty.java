package me.mrletsplay.mrcore.http.css;

import java.util.Optional;

import me.mrletsplay.mrcore.http.css.built.CSSBuiltProperty;
import me.mrletsplay.mrcore.http.html.HTMLDocumentBuildEvent;
import me.mrletsplay.mrcore.http.server.HttpDynamicValue;

public class CSSProperty {

	private String name;
	private HttpDynamicValue<HTMLDocumentBuildEvent, String> value;
	
	public CSSProperty(String name, HttpDynamicValue<HTMLDocumentBuildEvent, String> value) {
		this.name = name;
		this.value = value;
	}
	
	public CSSProperty(String name, String value) {
		this(name, HttpDynamicValue.of(value));
	}
	
	public String getName() {
		return name;
	}

	public HttpDynamicValue<HTMLDocumentBuildEvent, String> getValue() {
		return value;
	}
	
	public CSSBuiltProperty build(HTMLDocumentBuildEvent event) {
		Optional<String> val = value.get(event);
		if(!val.isPresent()) return null;
		CSSBuiltProperty p = new CSSBuiltProperty(this, name, val.get());
		return p;
	}
	
}
