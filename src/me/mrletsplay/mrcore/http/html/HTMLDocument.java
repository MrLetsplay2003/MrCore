package me.mrletsplay.mrcore.http.html;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import me.mrletsplay.mrcore.http.css.CSSStyleSheet;
import me.mrletsplay.mrcore.http.event.HttpPageBuildEvent;
import me.mrletsplay.mrcore.http.html.built.HTMLBuiltDocument;
import me.mrletsplay.mrcore.http.js.JSScript;
import me.mrletsplay.mrcore.http.server.HttpDynamicValue;

public class HTMLDocument {

	private List<HttpDynamicValue<HTMLDocumentBuildEvent, ? extends HTMLElement>> elements;
	private HTMLElement scriptElement, styleElement;
	private JSScript script;
	private CSSStyleSheet style;
	
	public HTMLDocument() {
		this.elements = new ArrayList<>();
		this.script = new JSScript();
		this.style = new CSSStyleSheet();
		this.scriptElement = new HTMLElement("script");
		scriptElement.setContent(event -> Optional.of(event.getBuiltDocument().getBuiltScript().asString()));
		addElement(scriptElement);
		this.styleElement = new HTMLElement("style");
		styleElement.setContent(event -> Optional.of(event.getBuiltDocument().getBuiltStyle().asString()));
		addElement(styleElement);
	}
	
	public List<HttpDynamicValue<HTMLDocumentBuildEvent, ? extends HTMLElement>> getElements() {
		return elements;
	}
	
	public void addElement(HttpDynamicValue<HTMLDocumentBuildEvent, ? extends HTMLElement> element) {
		elements.add(element);
	}
	
	public void addElement(HTMLElement element) {
		elements.add(event -> Optional.of(element));
	}
	
	public JSScript getScript() {
		return script;
	}
	
	public HTMLElement getScriptElement() {
		return scriptElement;
	}
	
	public CSSStyleSheet getStyle() {
		return style;
	}
	
	public HTMLElement getStyleElement() {
		return styleElement;
	}
	
	public HTMLBuiltDocument build(HttpPageBuildEvent buildEvent) {
		HTMLBuiltDocument b = new HTMLBuiltDocument(this);
		HTMLDocumentBuildEvent event = new HTMLDocumentBuildEvent(buildEvent, this, b);
		b.setBuiltScript(script.build(event));
		b.setBuiltStyle(style.build(event));
		for(HttpDynamicValue<HTMLDocumentBuildEvent, ? extends HTMLElement> el : getElements()) {
			Optional<? extends HTMLElement> opt = el.get(event);
			if(!opt.isPresent()) continue;
			HTMLElement elm = opt.get();
			b.addElement(elm.build(event));
		}
		b.setScriptElement(b.getElement(scriptElement));
		b.setStyleElement(b.getElement(styleElement));
		return b;
	}
	
}
