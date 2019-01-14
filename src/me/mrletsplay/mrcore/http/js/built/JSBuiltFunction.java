package me.mrletsplay.mrcore.http.js.built;

import java.util.List;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.http.js.JSFunction;

public class JSBuiltFunction {

	private JSFunction function;
	private String name;
	private List<String> parameters;
	private String body;
	
	public JSBuiltFunction(JSFunction function, String name, List<String> parameters, String body) {
		this.function = function;
		this.name = name;
		this.parameters = parameters;
		this.body = body;
	}
	
	public JSFunction getFunction() {
		return function;
	}
	
	public String getName() {
		return name;
	}
	
	public List<String> getParameters() {
		return parameters;
	}
	
	public String getBody() {
		return body;
	}
	
	public String asString() {
		StringBuilder b = new StringBuilder();
		b.append("function ").append(name).append("(");
		b.append(parameters.stream().collect(Collectors.joining(","))).append(")");
		b.append("{");
		b.append(body);
		b.append("}");
		return b.toString();
	}
	
}
