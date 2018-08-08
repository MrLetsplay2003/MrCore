package me.mrletsplay.mrcore.http.server.js;

import java.util.function.Function;

import me.mrletsplay.mrcore.http.server.js.built.JSBuiltFunction.JSFunctionBuildEvent;

public class JSFunctionBasic extends JSFunction {
	
	public JSFunctionBasic() {}
	
	public JSFunctionBasic(String innerJS) {
		setInnerJS(innerJS);
	}
	
	public JSFunctionBasic(String name, String innerJS) {
		setName(name);
		setInnerJS(innerJS);
	}
	
	public JSFunctionBasic(Function<JSFunctionBuildEvent, String> innerJS) {
		setInnerJS(innerJS);
	}
	
	public JSFunctionBasic(String name, Function<JSFunctionBuildEvent, String> innerJS) {
		setName(name);
		setInnerJS(innerJS);
	}
	
}
