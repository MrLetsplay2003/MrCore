package me.mrletsplay.mrcore.http.server.js;

import java.util.function.Consumer;

public class JSFunctionCallable extends JSFunction {

	private Consumer<JSFunctionInvokedEvent> invoked;
	
	public JSFunctionCallable(Consumer<JSFunctionInvokedEvent> invoked) {
		this.invoked = invoked;
	}
	
	public void invoke(JSFunctionInvokedEvent event) {
		invoked.accept(event);
	}
	
	@Override
	public String asString() {
		return
				"function " + getName() + "() {" +
				"functionCallback(\"" + getName() + "\")" +
				"}";
	}
	
}
