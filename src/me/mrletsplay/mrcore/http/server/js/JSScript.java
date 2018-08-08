package me.mrletsplay.mrcore.http.server.js;

import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.mrcore.http.server.html.HTMLDocument.HttpSiteAccessedEvent;
import me.mrletsplay.mrcore.http.server.html.built.HTMLBuiltDocument;
import me.mrletsplay.mrcore.http.server.js.built.JSBuiltScript;

public class JSScript implements Cloneable {

	private List<JSFunction> functions;
	
	public JSScript() {
		this.functions = new ArrayList<>();
	}
	
	public JSScript(List<JSFunction> functions) {
		this.functions = new ArrayList<>(functions);
	}
	
	public JSScript(JSScript jsScript) {
		this.functions = new ArrayList<>(jsScript.functions);
	}
	
	public void addFunction(JSFunction function) {
		functions.add(function);
	}
	
	public List<JSFunction> getFunctions() {
		return functions;
	}
	
	public void appendScript(JSScript script) {
		functions.addAll(script.functions);
	}
	
	public JSBuiltScript build(HTMLBuiltDocument doc, HttpSiteAccessedEvent event) {
		JSBuiltScript b = new JSBuiltScript(doc, event);
		functions.forEach(b::appendFunction);
		return b;
	}
	
	@Override
	public JSScript clone() {
		return new JSScript(this);
	}
	
}
