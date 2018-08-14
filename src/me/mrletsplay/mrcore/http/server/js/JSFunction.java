package me.mrletsplay.mrcore.http.server.js;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import me.mrletsplay.mrcore.http.server.HttpConnection;
import me.mrletsplay.mrcore.http.server.HttpConnectionInstance;
import me.mrletsplay.mrcore.http.server.HttpServer;
import me.mrletsplay.mrcore.http.server.html.HTMLElement;
import me.mrletsplay.mrcore.http.server.html.HTMLDocument.HttpSiteAccessedEvent;
import me.mrletsplay.mrcore.http.server.html.built.HTMLBuiltDocument;
import me.mrletsplay.mrcore.http.server.js.built.JSBuiltFunction;
import me.mrletsplay.mrcore.http.server.js.built.JSBuiltFunction.JSFunctionBuildEvent;

public class JSFunction {
	
	private String name;
	private Function<JSFunctionBuildEvent, String> innerJS;
	private List<JSFunction> additionalFunctions;
	private List<String> parameters;
	
	public JSFunction() {
		this.additionalFunctions = new ArrayList<>();
		this.parameters = new ArrayList<>();
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setInnerJS(String innerJS) {
		this.innerJS = (event) -> innerJS;
	}

	public void setInnerJS(Function<JSFunctionBuildEvent, String> innerJS) {
		this.innerJS = innerJS;
	}
	
	public String getInnerJS(JSFunctionBuildEvent event) {
		return innerJS.apply(event);
	}
	
	public void setParameters(List<String> parameters) {
		this.parameters = parameters;
	}
	
	public void setParameters(String... parameters) {
		this.parameters = Arrays.asList(parameters);
	}
	
	public void addParameter(String parameter) {
		this.parameters.add(parameter);
	}
	
	public void addParameters(String... parameters) {
		this.parameters.addAll(Arrays.asList(parameters));
	}
	
	public List<String> getParameters() {
		return parameters;
	}
	
	public void addAdditional(JSFunction function) {
		additionalFunctions.add(function);
	}
	
	public List<JSFunction> getAdditionalFunctions() {
		return additionalFunctions;
	}
	
	public JSBuiltFunction build(String name, HTMLBuiltDocument doc, HttpSiteAccessedEvent event) {
		return new JSBuiltFunction(this, name, doc, event);
	}
	
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
	
	public static JSFunctionBasic of(String innerJS) {
		return new JSFunctionBasic(innerJS);
	}
	
	public static JSFunctionBasic of(String name, String innerJS) {
		return new JSFunctionBasic(name, innerJS);
	}
	
	public static JSFunctionBasic of(Function<JSFunctionBuildEvent, String> innerJS) {
		return new JSFunctionBasic(innerJS);
	}
	
	public static JSFunctionBasic of(String name, Function<JSFunctionBuildEvent, String> innerJS) {
		return new JSFunctionBasic(name, innerJS);
	}
	
	public static JSFunctionChangeContent changeContent(HTMLElement element, String content) {
		return new JSFunctionChangeContent(element, content);
	}
	
	public static JSFunctionChangeContent changeContent(HTMLElement element, JSFunction getContentFunction) {
		return new JSFunctionChangeContent(element, getContentFunction);
	}
	
}
