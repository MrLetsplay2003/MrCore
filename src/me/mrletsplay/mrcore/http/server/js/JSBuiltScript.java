package me.mrletsplay.mrcore.http.server.js;

import java.util.ArrayList;
import java.util.List;

public class JSBuiltScript implements Cloneable {

	private List<JSBuiltFunction> functions;
	private long lastFunctionID;
	
	public JSBuiltScript() {
		this.functions = new ArrayList<>();
	}
	
	public JSBuiltScript(List<JSBuiltFunction> functions) {
		this.functions = new ArrayList<>(functions);
	}
	
	public JSBuiltScript(JSBuiltScript jsScript) {
		this.functions = new ArrayList<>(jsScript.functions);
		this.lastFunctionID = jsScript.lastFunctionID;
	}

	public String randomFunctionName() {
		return "func_" + (lastFunctionID++);
	}
	
	public JSBuiltFunction appendFunction(JSFunction function) {
		System.out.println(function + " -> " + function.getName());
		String name = function.getName();
		if(name == null) name = randomFunctionName();
		System.out.println(name);
		JSBuiltFunction f = new JSBuiltFunction(function, name);
		functions.add(f);
		return f;
	}
	
	public JSBuiltFunction getFunction(String name) {
		return functions.stream().filter(f -> f.getName().equals(name)).findFirst().orElse(null);
	}
	
	public List<JSBuiltFunction> getFunctions() {
		return functions;
	}
	
	public void appendScript(JSBuiltScript script) {
		functions.addAll(script.functions);
	}
	
	public String asString() {
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
