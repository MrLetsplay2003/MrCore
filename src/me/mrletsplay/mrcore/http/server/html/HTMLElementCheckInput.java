package me.mrletsplay.mrcore.http.server.html;

import java.util.function.Consumer;

import me.mrletsplay.mrcore.http.server.HttpClientPoll;
import me.mrletsplay.mrcore.http.server.HttpConnectionInstance;
import me.mrletsplay.mrcore.http.server.HttpServer;
import me.mrletsplay.mrcore.http.server.html.HTMLDocument.HttpSiteAccessedEvent;
import me.mrletsplay.mrcore.http.server.html.built.HTMLBuiltDocument;
import me.mrletsplay.mrcore.http.server.html.built.HTMLBuiltElement;
import me.mrletsplay.mrcore.http.server.html.built.HTMLBuiltElementCheckInput;
import me.mrletsplay.mrcore.http.server.js.JSFunction;
import me.mrletsplay.mrcore.http.server.js.JSFunctionConsumingCallable.JSFunctionConsumingInvokedEvent;
import me.mrletsplay.mrcore.misc.JSON.JSONObject;

public class HTMLElementCheckInput extends HTMLElement {

	private OnChanged onChanged;
	private boolean defaultValue;
	
	protected HTMLElementCheckInput(HTMLElementCheckInput element) {
		super(element);
		this.onChanged = element.onChanged.clone();
	}
	
	protected HTMLElementCheckInput() {
		super("input");
		addAttribute("type", "checkbox");
		this.onChanged = new OnChanged();
	}
	
	public HTMLElementCheckInput setDefaultValue(boolean defaultValue) {
		this.defaultValue = defaultValue;
		return this;
	}
	
	public boolean getDefaultValue() {
		return defaultValue;
	}
	
	public OnChanged onChanged() {
		return onChanged;
	}
	
	@Override
	public HTMLBuiltElement build(HTMLBuiltElement parent, HTMLBuiltDocument doc, String id, HttpSiteAccessedEvent event, String... params) {
		return new HTMLBuiltElementCheckInput(parent, doc, this, id, event, params);
	}
	
	public static class OnChanged {
		
		private JSFunction function;
		private Consumer<CheckInputChangedEvent> eventHandler;
		
		public OnChanged() {}
		
		public OnChanged(OnChanged from) {
			this.function = from.function;
		}
		
		public OnChanged function(JSFunction function) {
			this.function = function;
			return this;
		}
		
		public OnChanged event(Consumer<CheckInputChangedEvent> handler) {
			this.eventHandler = handler;
			return this;
		}
		
		public JSFunction getFunction() {
			return function;
		}
		
		public Consumer<CheckInputChangedEvent> getEventHandler() {
			return eventHandler;
		}
		
		public OnChanged clone() {
			return new OnChanged(this);
		}
			
	}
	
	public static class CheckInputChangedEvent extends JSFunctionConsumingInvokedEvent {

		private String elementID;
		private boolean input;
		
		public CheckInputChangedEvent(HttpServer server, HttpConnectionInstance connectionInstance, HTMLBuiltDocument context, JSONObject params, String elementID, boolean input) {
			super(server, connectionInstance, context, params);
			this.elementID = elementID;
			this.input = input;
		}
		
		public String getElementID() {
			return elementID;
		}
		
		public boolean getInputValue() {
			return input;
		}
		
		public void setInputText(String text) {
			getConnection().addPoll(HttpClientPoll.setProperty(elementID, "value", text));
		}
		
		public void setPlaceholder(String placeholder) {
			getConnection().addPoll(HttpClientPoll.setAttribute(elementID, "placeholder", placeholder));
		}
		
		public static CheckInputChangedEvent of(JSFunctionConsumingInvokedEvent jsEvent, String elementID, boolean input) {
			return new CheckInputChangedEvent(jsEvent.getServer(), jsEvent.getConnectionInstance(), jsEvent.getContext(), jsEvent.getParameters(), elementID, input);
		}

	}
	
}
