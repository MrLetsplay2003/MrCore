package me.mrletsplay.mrcore.http.html.built;

import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.mrcore.http.html.HTMLDocument;

public class HTMLBuiltDocument {

	private HTMLDocument document;
	private List<HTMLBuiltElement> elements;
	
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
	
	public List<HTMLBuiltElement> getElements() {
		return elements;
	}
	
}
