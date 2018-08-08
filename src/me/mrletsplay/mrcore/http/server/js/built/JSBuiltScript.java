package me.mrletsplay.mrcore.http.server.js.built;

import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.mrcore.http.server.html.HTMLDocument.HttpSiteAccessedEvent;
import me.mrletsplay.mrcore.http.server.html.built.HTMLBuiltDocument;
import me.mrletsplay.mrcore.http.server.js.JSFunction;

public class JSBuiltScript implements Cloneable {

	private List<JSBuiltFunction> functions;
	private HTMLBuiltDocument document;
	private HttpSiteAccessedEvent event;
	private long lastFunctionID;
	
	public JSBuiltScript(HTMLBuiltDocument doc, HttpSiteAccessedEvent event) {
		this.functions = new ArrayList<>();
		this.document = doc;
		this.event = event;
	}
	
	public JSBuiltScript(List<JSBuiltFunction> functions, HTMLBuiltDocument doc, HttpSiteAccessedEvent event) {
		this.functions = new ArrayList<>(functions);
		this.document = doc;
		this.event = event;
	}
	
	public JSBuiltScript(JSBuiltScript jsScript) {
		this.functions = new ArrayList<>(jsScript.functions);
		this.lastFunctionID = jsScript.lastFunctionID;
		this.document = jsScript.document;
		this.event = jsScript.event;
	}

	public String randomFunctionName() {
		return "func_" + (lastFunctionID++);
	}
	
	public JSBuiltFunction appendFunction(JSFunction function) {
		String name = function.getName();
		if(name == null) name = randomFunctionName();
		JSBuiltFunction f = function.build(name, document, event);
		functions.add(f);
		return f;
	}
	
	public JSBuiltFunction getFunction(String name) {
		return functions.stream().filter(f -> f.getName().equals(name)).findFirst().orElse(null);
	}
	
	public JSBuiltFunction getFunction(JSFunction function) {
		return functions.stream().filter(f -> f.getBase().equals(function)).findFirst().orElse(null);
	}
	
	public List<JSBuiltFunction> getFunctions() {
		return functions;
	}
	
	public void appendScript(JSBuiltScript script) {
		functions.addAll(script.functions);
	}
	
	public String asString() {
		functions.forEach(JSBuiltFunction::completeBuild);
		StringBuilder builder = new StringBuilder();
		for(JSBuiltFunction f : functions) {
			builder.append(f.asString());
		}
		return builder.toString();
	}
	
	@Override
	public JSBuiltScript clone() {
		return new JSBuiltScript(this);
	}
	
}
