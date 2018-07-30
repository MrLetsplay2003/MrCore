package me.mrletsplay.mrcore.http.server.js;

public class JSFunctionRaw extends JSFunction {
	
	private String raw;
	
	public JSFunctionRaw(String raw) {
		this.raw = raw;
	}
	
	@Override
	public String asString(String name) {
		return raw;
	}
	
}
