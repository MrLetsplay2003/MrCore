package me.mrletsplay.mrcore.http.html.built;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.http.css.built.CSSBuiltStyleSheet;
import me.mrletsplay.mrcore.http.html.HTMLDocument;
import me.mrletsplay.mrcore.http.html.HTMLElement;
import me.mrletsplay.mrcore.http.js.built.JSBuiltScript;

public class HTMLBuiltDocument {

	private HTMLDocument document;
	private List<HTMLBuiltElement> elements;
	private HTMLBuiltElement scriptElement, styleElement;
	private JSBuiltScript builtScript;
	private CSSBuiltStyleSheet builtStyle;
	
	private long lastID = 0;
	
	public HTMLBuiltDocument(HTMLDocument document) {
		this.document = document;
		this.elements = new ArrayList<>();
	}
	
	public HTMLDocument getDocument() {
		return document;
	}
	
	public void addElement(HTMLBuiltElement element) {
		this.elements.add(element);
	}
	
	public HTMLBuiltElement getElement(HTMLElement element) {
		return elements.stream().filter(e -> e.getElement().equals(element)).findFirst().orElse(null);
	}
	
	public HTMLBuiltElement getElementByID(String id) {
		return elements.stream().filter(e -> e.getID().equals(id)).findFirst().orElse(null);
	}
	
	public List<HTMLBuiltElement> getElementsByClassName(String className) {
		return elements.stream().filter(e -> e.getClasses().contains(className)).collect(Collectors.toList());
	}
	
	public List<HTMLBuiltElement> getElements() {
		return elements;
	}
	
	public void setBuiltScript(JSBuiltScript builtScript) {
		this.builtScript = builtScript;
	}
	
	public JSBuiltScript getBuiltScript() {
		return builtScript;
	}
	
	public void setBuiltStyle(CSSBuiltStyleSheet builtStyle) {
		this.builtStyle = builtStyle;
	}
	
	public CSSBuiltStyleSheet getBuiltStyle() {
		return builtStyle;
	}
	
	public void setScriptElement(HTMLBuiltElement scriptElement) {
		this.scriptElement = scriptElement;
	}
	
	public HTMLBuiltElement getScriptElement() {
		return scriptElement;
	}
	
	public void setStyleElement(HTMLBuiltElement styleElement) {
		this.styleElement = styleElement;
	}
	
	public HTMLBuiltElement getStyleElement() {
		return styleElement;
	}
	
	public String asString() {
		StringBuilder b = new StringBuilder("<html>");
		for(HTMLBuiltElement el : elements) {
			b.append(el.asString());
		}
		return b.append("</html>").toString();
	}

	public String newElementID() {
		return "el_" + (lastID++);
	}
	
}
