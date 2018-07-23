package me.mrletsplay.mrcore.http.server.js;

import java.util.Arrays;

import me.mrletsplay.mrcore.misc.JSON.JSONObject;

public abstract class JSFunctionConsumingCallable extends JSFunction {

	public abstract void invoke(JSFunctionInvokedEvent event, JSONObject params);
	
	public abstract String getParamsMethod();
	
	public static boolean requireParams(JSONObject params, String... keys) {
		return Arrays.stream(keys).allMatch(params::containsKey);
	}
	
	@Override
	public String asString() {
		return
				"function " + getName() + "_params() {" +
				getParamsMethod() +
				"}" +
				"function " + getName() + "() {" +
				"functionCallbackConsuming(\"" + getName() + "\", " + getName() + "_params())" +
				"}";
	}
	
}
