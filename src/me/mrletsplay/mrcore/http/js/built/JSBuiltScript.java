package me.mrletsplay.mrcore.http.js.built;

import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.mrcore.http.js.JSScript;

public class JSBuiltScript {

	private JSScript script;
	private List<JSBuiltFunction> functions;
	private String body;
	
	public JSBuiltScript(JSScript script, String body) {
		this.script = script;
		this.functions = new ArrayList<>();
		this.body = body;
	}
	
	public JSScript getScript() {
		return script;
	}
	
	public void addFunction(JSBuiltFunction function) {
		functions.add(function);
	}
	
	public List<JSBuiltFunction> getFunctions() {
		return functions;
	}
	
	public String getBody() {
		return body;
	}
	
	public String asString() {
		StringBuilder b = new StringBuilder();
		for(JSBuiltFunction f : functions) {
			b.append(f);
		}
		if(body != null) b.append(body);
		return b.toString();
	}
	
}
