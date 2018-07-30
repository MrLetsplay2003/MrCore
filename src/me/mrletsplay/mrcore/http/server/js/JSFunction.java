package me.mrletsplay.mrcore.http.server.js;

import java.util.function.Consumer;

import me.mrletsplay.mrcore.http.server.HttpConnection;
import me.mrletsplay.mrcore.http.server.HttpConnectionInstance;
import me.mrletsplay.mrcore.http.server.HttpServer;
import me.mrletsplay.mrcore.http.server.html.HTMLDocument.HTMLBuiltDocument;

public abstract class JSFunction {
	
	private String name;
	
	public void setName(String name) {
		Thread.dumpStack();
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public abstract String asString(String name);
	
	public static class JSFunctionInvokedEvent {
		
		private HttpServer server;
		private HttpConnectionInstance connectionInstance;
		private HTMLBuiltDocument context;
		
		public JSFunctionInvokedEvent(HttpServer server, HttpConnectionInstance connectionInstance, HTMLBuiltDocument context) {
			this.server = server;
			this.connectionInstance = connectionInstance;
			this.context = context;
		}
		
		public HttpServer getServer() {
			return server;
		}
		
		public HttpConnectionInstance getConnectionInstance() {
			return connectionInstance;
		}
		
		public HttpConnection getConnection() {
			return connectionInstance.getConnection();
		}
		
		public HTMLBuiltDocument getContext() {
			return context;
		}
		
	}
	
	public static JSFunctionCallable callable(Consumer<JSFunctionInvokedEvent> callableFunction) {
		return new JSFunctionCallable(callableFunction);
	}
	
	public static JSFunctionConsumingCallable consumingCallable(JSFunctionConsumingCallable consumingCallableFunction) {
		return consumingCallableFunction;
	}
	
	public static JSFunctionRaw raw(String js) {
		return new JSFunctionRaw(js);
	}
	
}
