package me.mrletsplay.mrcore.http.server.js;

import java.util.function.Consumer;

import me.mrletsplay.mrcore.http.server.HttpConnection;
import me.mrletsplay.mrcore.http.server.HttpServer;
import me.mrletsplay.mrcore.http.server.html.HTMLDocument.HTMLBuiltDocument;

public class JSFunction {
	
	private String name;
	private String innerJS;
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public String asString() {
		return innerJS;
	}
	
//	public JSParsedFunction build(JSScript context) {
//		return new JSParsedFunction(context, this);
//	}
	
	public static class JSFunctionInvokedEvent {
		
		private HttpServer server;
		private HttpConnection connection;
		private HTMLBuiltDocument context;
		
		public JSFunctionInvokedEvent(HttpServer server, HttpConnection connection, HTMLBuiltDocument context) {
			this.server = server;
			this.connection = connection;
			this.context = context;
		}
		
		public HttpServer getServer() {
			return server;
		}
		
		public HttpConnection getConnection() {
			return connection;
		}
		
		public HTMLBuiltDocument getContext() {
			return context;
		}
		
	}
	
	public static JSFunction basic(String name, String innerJS) {
		return new JSFunction() {
			
			@Override
			public String getName() {
				return name;
			}
			
			@Override
			public String asString() {
				return innerJS;
			}
		};
	}
	
	public static JSFunctionCallable callable(Consumer<JSFunctionInvokedEvent> callableFunction) {
		return new JSFunctionCallable(callableFunction);
	}
	
	public static JSFunctionConsumingCallable consumingCallable(JSFunctionConsumingCallable consumingCallableFunction) {
		return consumingCallableFunction;
	}
	
}
