package me.mrletsplay.mrcore.http.server.js;

public class JSFunctionBasic extends JSFunction {
	
	private String innerJS;
	
	public JSFunctionBasic(String innerJS) {
		this.innerJS = innerJS;
	}
	
	public JSFunctionBasic(String innerJS, String name) {
		this.innerJS = innerJS;
		setName(name);
	}
	
	@Override
	public String asString(String name) {
		return
				"function " + name + "(self) {" +
				innerJS +
				"}";
	}
	
}
