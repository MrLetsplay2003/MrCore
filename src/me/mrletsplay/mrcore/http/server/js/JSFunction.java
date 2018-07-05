package me.mrletsplay.mrcore.http.server.js;

public interface JSFunction {

	public String innerJS(String name);
	
	public default JSParsedFunction build(JSScript context) {
		return new JSParsedFunction(context, this);
	}
	
}
