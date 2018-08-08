package me.mrletsplay.mrcore.http.server.html;

import java.util.function.Consumer;

import me.mrletsplay.mrcore.http.server.HttpClientPoll;
import me.mrletsplay.mrcore.http.server.HttpConnectionInstance;
import me.mrletsplay.mrcore.http.server.HttpServer;
import me.mrletsplay.mrcore.http.server.css.CSSStyleElement;
import me.mrletsplay.mrcore.http.server.html.HTMLDocument.HttpSiteAccessedEvent;
import me.mrletsplay.mrcore.http.server.html.built.HTMLBuiltDocument;
import me.mrletsplay.mrcore.http.server.html.built.HTMLBuiltElement;
import me.mrletsplay.mrcore.http.server.html.built.HTMLBuiltElementCheckInput;
import me.mrletsplay.mrcore.http.server.js.JSFunction;
import me.mrletsplay.mrcore.http.server.js.JSFunctionConsumingCallable.JSFunctionConsumingInvokedEvent;
import me.mrletsplay.mrcore.misc.JSON.JSONObject;

public class HTMLElementCheckInput extends HTMLElement {

	private OnChanged onChanged;
	private OnChecked onChecked;
	private OnUnchecked onUnchecked;
	private boolean defaultValue;
	
	protected HTMLElementCheckInput() {
		super("input");
		addAttribute("type", "checkbox");
		this.onChanged = new OnChanged();
		this.onChecked = new OnChecked();
		this.onUnchecked = new OnUnchecked();
	}
	
	protected HTMLElementCheckInput(HTMLElementCheckInput element) {
		super(element);
		this.onChanged = element.onChanged.clone();
		this.onChecked = element.onChecked.clone();
		this.onUnchecked = element.onUnchecked.clone();
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
	
	public OnChecked onChecked() {
		return onChecked;
	}
	
	public OnUnchecked onUnchecked() {
		return onUnchecked;
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
			this.eventHandler = from.eventHandler;
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
	
	public static class OnChecked {
		
		private CSSStyleElement css;
		private JSFunction function;
		private Consumer<CheckInputChangedEvent> eventHandler;
		
		public OnChecked() {
			this.css = new CSSStyleElement();
		}
		
		public OnChecked(OnChecked from) {
			this.css = from.css.clone();
			this.function = from.function;
			this.eventHandler = from.eventHandler;
		}
		
		public OnChecked css(CSSStyleElement css) {
			this.css = css;
			return this;
		}
		
		public CSSStyleElement css() {
			return css;
		}
		
		public OnChecked function(JSFunction function) {
			this.function = function;
			return this;
		}
		
		public JSFunction getFunction() {
			return function;
		}
		
		public OnChecked event(Consumer<CheckInputChangedEvent> eventHandler) {
			this.eventHandler = eventHandler;
			return this;
		}
		
		public Consumer<CheckInputChangedEvent> getEventHandler() {
			return eventHandler;
		}
		
		public OnChecked clone() {
			return new OnChecked(this);
		}
			
	}
	
	public static class OnUnchecked {
		
		private JSFunction function;
		private Consumer<CheckInputChangedEvent> eventHandler;
		
		public OnUnchecked() {}
		
		public OnUnchecked(OnUnchecked from) {
			this.function = from.function;
			this.eventHandler = from.eventHandler;
		}
		
		public OnUnchecked function(JSFunction function) {
			this.function = function;
			return this;
		}
		
		public JSFunction getFunction() {
			return function;
		}
		
		public OnUnchecked event(Consumer<CheckInputChangedEvent> eventHandler) {
			this.eventHandler = eventHandler;
			return this;
		}
		
		public Consumer<CheckInputChangedEvent> getEventHandler() {
			return eventHandler;
		}
		
		public OnUnchecked clone() {
			return new OnUnchecked(this);
		}
			
	}
	
	public static class CheckInputChangedEvent extends JSFunctionConsumingInvokedEvent implements HTMLElementEvent {

		private String elementID;
		private HTMLBuiltElementCheckInput element;
		private boolean input;
		
		public CheckInputChangedEvent(HttpServer server, HttpConnectionInstance connectionInstance, HTMLBuiltDocument context, JSONObject params, String elementID, boolean input) {
			super(server, connectionInstance, context, params);
			this.elementID = elementID;
			this.input = input;
			this.element = (HTMLBuiltElementCheckInput) context.getElementByID(elementID);
		}

		@Override
		public HTMLBuiltElement getElement() {
			return element;
		}
		
		@Override
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
