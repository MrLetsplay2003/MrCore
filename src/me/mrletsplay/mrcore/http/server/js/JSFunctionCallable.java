package me.mrletsplay.mrcore.http.server.js;

import java.util.function.Consumer;

public class JSFunctionCallable extends JSFunction {

	private Consumer<JSFunctionInvokedEvent> invoked;
	
	public JSFunctionCallable(Consumer<JSFunctionInvokedEvent> invoked) {
		this.invoked = invoked;
		setInnerJS(event -> {
			return "functionCallback(\"" + event.getFunctionName() + "\");";
		});
	}
	
	public void invoke(JSFunctionInvokedEvent event) {
		invoked.accept(event);
	}
	
//	@Override
//	public String asString(String name, HTMLBuiltDocument doc, HttpSiteAccessedEvent event) {
//		return
//				"function " + name + "(self) {" +
//				"functionCallback(\"" + name + "\")" +
//				"}";
//	}
	
}
