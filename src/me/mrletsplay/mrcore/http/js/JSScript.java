package me.mrletsplay.mrcore.http.js;

import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.mrcore.http.html.HTMLDocumentBuildEvent;
import me.mrletsplay.mrcore.http.js.built.JSBuiltFunction;
import me.mrletsplay.mrcore.http.js.built.JSBuiltScript;

public class JSScript {
	
	private List<JSClass> classes;
	private List<JSFunction> functions;
	private String body;
	
	public JSScript() {
		this.classes = new ArrayList<>();
		this.functions = new ArrayList<>();
		this.body = "";
	}
	
	public List<JSClass> getClasses() {
		return classes;
	}

	public List<? extends JSFunction> getFunctions() {
		return functions;
	}
	
	public void addFunction(JSFunction function) {
		functions.add(function);
	}
	
	public void removeFunction(JSFunction function) {
		functions.remove(function);
	}
	
	public void setBody(String body) {
		this.body = body;
	}
	
	public void appendBody(String body) {
		this.body += body;
	}
	
	public String getBody() {
		return body;
	}
	
	public JSBuiltScript build(HTMLDocumentBuildEvent event) {
		JSBuiltScript bS = new JSBuiltScript(this, body);
		for(JSFunction f : functions) {
			JSBuiltFunction bF = f.build(event);
			if(bF != null) bS.addFunction(bF);
		}
		return bS;
	}
	
}
