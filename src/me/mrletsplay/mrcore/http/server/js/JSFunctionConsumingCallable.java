package me.mrletsplay.mrcore.http.server.js;

import java.util.Arrays;

import me.mrletsplay.mrcore.http.server.HttpConnectionInstance;
import me.mrletsplay.mrcore.http.server.HttpServer;
import me.mrletsplay.mrcore.http.server.html.HTMLDocument.HTMLBuiltDocument;
import me.mrletsplay.mrcore.misc.JSON.JSONObject;

public abstract class JSFunctionConsumingCallable extends JSFunction {

	public abstract void invoke(JSFunctionConsumingInvokedEvent event);
	
	public abstract String getParamsMethod();
	
	public static boolean requireParams(JSONObject params, String... keys) {
		return Arrays.stream(keys).allMatch(params::containsKey);
	}
	
	@Override
	public String asString(String name) {
		return
				"function " + name + "_params(self) {" +
				getParamsMethod() +
				"}" +
				"function " + name + "(self) {" +
				"functionCallbackConsuming(\"" + name + "\", " + name + "_params(self))" +
				"}";
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
