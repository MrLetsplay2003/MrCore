package me.mrletsplay.mrcore.http.server.js;

public class JSParsedFunction implements JSFunction {

	private JSFunction raw;
	private JSScript context;
	private String name;
	
	public JSParsedFunction(JSScript context, JSFunction raw) {
		this.raw = raw;
		this.context = context;
		this.name = context.randomFunctionName();
	}
	
	public JSFunction getRaw() {
		return raw;
	}
	
	public String getName() {
		return name;
	}
	
	public JSScript getContext() {
		return context;
	}
	
	@Override
	public String innerJS(String name) {
		return raw.innerJS(name);
	}
	
	public String asString() {
		return 
				"function " + name + "() {" +
				innerJS(name) +
				"}";
	}
	
}
