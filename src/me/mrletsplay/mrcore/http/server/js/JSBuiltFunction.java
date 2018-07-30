package me.mrletsplay.mrcore.http.server.js;

public class JSBuiltFunction {

	private JSFunction base;
	private String name;
	
	public JSBuiltFunction(JSFunction base, String name) {
		this.base = base;
		this.name = name;
	}
	
	public JSFunction getBase() {
		return base;
	}
	
	public String getName() {
		return name;
	}

	public Object asString() {
		return base.asString(name);
	}
	
}
