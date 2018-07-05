package me.mrletsplay.mrcore.http.server.js;

import java.util.ArrayList;
import java.util.List;

public class JSScript {

	private List<JSParsedFunction> functions;
	private long lastFunctionID;
	
	public JSScript() {
		this.functions = new ArrayList<>();
	}
	
	public String randomFunctionName() {
		return "func_" + (lastFunctionID++);
	}
	
	public JSParsedFunction appendFunction(JSFunction function) {
		JSParsedFunction parsed = function.build(this);
		functions.add(parsed);
		return parsed;
	}
	
	public JSParsedFunction getFunction(String name) {
		return functions.stream().filter(f -> f.getName().equals(name)).findFirst().orElse(null);
	}
	
	public String asString() {
		StringBuilder builder = new StringBuilder();
		for(JSParsedFunction f : functions) {
			builder.append(f.asString());
		}
		return builder.toString();
	}
	
}
