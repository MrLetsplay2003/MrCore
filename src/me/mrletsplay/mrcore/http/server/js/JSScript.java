package me.mrletsplay.mrcore.http.server.js;

import java.util.ArrayList;
import java.util.List;

public class JSScript implements Cloneable {

	private List<JSFunction> functions;
//	private long lastFunctionID;
	
	public JSScript() {
		this.functions = new ArrayList<>();
	}
	
	public JSScript(List<JSFunction> functions) {
		this.functions = new ArrayList<>(functions);
	}
	
	public JSScript(JSScript jsScript) {
		this.functions = new ArrayList<>(jsScript.functions);
//		this.lastFunctionID = jsScript.lastFunctionID;
	}

//	public String randomFunctionName() {
//		return "func_" + (lastFunctionID++);
//	}
	
//	public void appendFunction(JSFunction function) {
//		System.out.println(function + " -> " + function.getName());
//		if(function.getName() == null) function.setName(randomFunctionName());
//		functions.add(function);
//	}
	
//	public JSFunction getFunction(String name) {
//		return functions.stream().filter(f -> f.getName().equals(name)).findFirst().orElse(null);
//	}
	
	public void addFunction(JSFunction function) {
		functions.add(function);
	}
	
	public List<JSFunction> getFunctions() {
		return functions;
	}
	
	public void appendScript(JSScript script) {
		functions.addAll(script.functions);
	}
	
//	public String asString() {
//		StringBuilder builder = new StringBuilder();
//		for(JSFunction f : functions) {
//			builder.append(f.asString());
//		}
//		return builder.toString();
//	}
	
	public JSBuiltScript build() {
		JSBuiltScript b = new JSBuiltScript();
		functions.forEach(b::appendFunction);
		return b;
	}
	
	@Override
	public JSScript clone() {
		return new JSScript(this);
	}
	
}
