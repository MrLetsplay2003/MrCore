package me.mrletsplay.mrcore.http.html;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import me.mrletsplay.mrcore.http.css.CSSStyleSheet;
import me.mrletsplay.mrcore.http.event.HttpPageBuildEvent;
import me.mrletsplay.mrcore.http.html.built.HTMLBuiltDocument;
import me.mrletsplay.mrcore.http.html.built.HTMLBuiltElement;
import me.mrletsplay.mrcore.http.js.JSScript;
import me.mrletsplay.mrcore.http.server.HttpDynamicValue;

public class HTMLDocument {

	private List<HttpDynamicValue<HTMLDocumentBuildEvent, ? extends HTMLElement>> elements;
	private HTMLElement headElement, bodyElement, scriptElement, styleElement;
	private JSScript script;
	private CSSStyleSheet style;
	
	public HTMLDocument() {
		this.elements = new ArrayList<>();
		this.script = new JSScript();
		this.style = new CSSStyleSheet();
		this.headElement = new HTMLElement("head");
		addElement(headElement);
		this.bodyElement = new HTMLElement("body");
		addElement(bodyElement);
		this.scriptElement = new HTMLElement("script");
		scriptElement.getFlags().addFlag(HTMLFlag.ELEMENT_REMOVE_IF_EMPTY);
		scriptElement.setContent(event -> Optional.of(event.getBuiltDocument().getBuiltScript().asString()));
		headElement.addChild(scriptElement);
		this.styleElement = new HTMLElement("style");
		styleElement.getFlags().addFlag(HTMLFlag.ELEMENT_REMOVE_IF_EMPTY);
		styleElement.setContent(event -> Optional.of(event.getBuiltDocument().getBuiltStyle().asString()));
		headElement.addChild(styleElement);
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
	
	public HTMLElement getHeadElement() {
		return headElement;
	}
	
	public HTMLElement getBodyElement() {
		return bodyElement;
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
			HTMLBuiltElement bel = elm.build(event);
			if(elm.getFlags().hasFlag(HTMLFlag.ELEMENT_REMOVE_IF_EMPTY) && bel.isEmpty()) continue;
			b.addElement(bel);
		}
		b.setScriptElement(b.getElement(scriptElement));
		b.setStyleElement(b.getElement(styleElement));
		return b;
	}
	
}
