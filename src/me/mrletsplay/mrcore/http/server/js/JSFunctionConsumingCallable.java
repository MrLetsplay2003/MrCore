package me.mrletsplay.mrcore.http.server.js;

import java.util.Arrays;

import me.mrletsplay.mrcore.http.server.HttpConnectionInstance;
import me.mrletsplay.mrcore.http.server.HttpServer;
import me.mrletsplay.mrcore.http.server.html.built.HTMLBuiltDocument;
import me.mrletsplay.mrcore.misc.JSON.JSONObject;

public abstract class JSFunctionConsumingCallable extends JSFunction {
	
	public JSFunctionConsumingCallable() {
		JSFunction paramsMethod = JSFunction.of(getParamsMethod());
		addAdditional(paramsMethod);
		
		setInnerJS(event -> {
			return "functionCallbackConsuming(\"" + event.getFunctionName() + "\", " + event.getDocument().getScript().getFunction(paramsMethod).getName() + "(self));";
		});
	}
	
	public abstract void invoke(JSFunctionConsumingInvokedEvent event);
	
	public abstract String getParamsMethod();
	
	public static boolean requireParams(JSONObject params, String... keys) {
		return Arrays.stream(keys).allMatch(params::containsKey);
	}
	
	public static class JSFunctionConsumingInvokedEvent extends JSFunctionInvokedEvent {

		private JSONObject params;
		
		public JSFunctionConsumingInvokedEvent(HttpServer server, HttpConnectionInstance connectionInstance, HTMLBuiltDocument context, JSONObject params) {
			super(server, connectionInstance, context);
			this.params = params;
		}
		
		public JSONObject getParameters() {
			return params;
		}
		
	}
	
}
