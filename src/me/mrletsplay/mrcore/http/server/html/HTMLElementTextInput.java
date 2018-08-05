package me.mrletsplay.mrcore.http.server.html;

import java.util.function.Consumer;

import me.mrletsplay.mrcore.http.server.HttpClientPoll;
import me.mrletsplay.mrcore.http.server.HttpConnectionInstance;
import me.mrletsplay.mrcore.http.server.HttpServer;
import me.mrletsplay.mrcore.http.server.html.HTMLDocument.HttpSiteAccessedEvent;
import me.mrletsplay.mrcore.http.server.html.built.HTMLBuiltDocument;
import me.mrletsplay.mrcore.http.server.html.built.HTMLBuiltElement;
import me.mrletsplay.mrcore.http.server.html.built.HTMLBuiltElementTextInput;
import me.mrletsplay.mrcore.http.server.js.JSFunction;
import me.mrletsplay.mrcore.http.server.js.JSFunctionConsumingCallable.JSFunctionConsumingInvokedEvent;
import me.mrletsplay.mrcore.misc.JSON.JSONObject;

public class HTMLElementTextInput extends HTMLElement {

	private TextInputType inputType;
	private OnChanged onChanged;
	private String placeholder;
	
	protected HTMLElementTextInput(HTMLElementTextInput element) {
		super(element);
		this.inputType = element.inputType;
		this.onChanged = element.onChanged.clone();
	}
	
	protected HTMLElementTextInput(TextInputType type) {
		super("input");
		addAttribute("type", type.html);
		this.inputType = type;
		this.onChanged = new OnChanged();
	}
	
	protected HTMLElementTextInput(TextInputType type, String placeholder) {
		super("input");
		addAttribute("type", type.html);
		addAttribute("placeholder", placeholder);
		this.inputType = type;
		this.onChanged = new OnChanged();
	}
	
	public HTMLElementTextInput setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
		addAttribute("placeholder", placeholder);
		return this;
	}
	
	public String getPlaceholder() {
		return placeholder;
	}
	
	public TextInputType getInputType() {
		return inputType;
	}
	
	public OnChanged onChanged() {
		return onChanged;
	}
	
	@Override
	public HTMLBuiltElement build(HTMLBuiltElement parent, HTMLBuiltDocument doc, String id, HttpSiteAccessedEvent event, String... params) {
		return new HTMLBuiltElementTextInput(parent, doc, this, id, event, params);
	}
	
	public static enum TextInputType {
		
		PLAIN("text"),
		PASSWORD("password");
		
		public final String html;
		
		private TextInputType(String html) {
			this.html = html;
		}
		
	}
	
	public static class OnChanged {
		
		private JSFunction function;
		private Consumer<TextInputChangedEvent> eventHandler;
		
		public OnChanged() {}
		
		public OnChanged(OnChanged from) {
			this.function = from.function;
		}
		
		public OnChanged function(JSFunction function) {
			this.function = function;
			return this;
		}
		
		public OnChanged event(Consumer<TextInputChangedEvent> handler) {
			this.eventHandler = handler;
			return this;
		}
		
		public JSFunction getFunction() {
			return function;
		}
		
		public Consumer<TextInputChangedEvent> getEventHandler() {
			return eventHandler;
		}
		
		public OnChanged clone() {
			return new OnChanged(this);
		}
			
	}
	
	public static class TextInputChangedEvent extends JSFunctionConsumingInvokedEvent {

		private String elementID;
		private String input;
		
		public TextInputChangedEvent(HttpServer server, HttpConnectionInstance connectionInstance, HTMLBuiltDocument context, JSONObject params, String elementID, String input) {
			super(server, connectionInstance, context, params);
			this.elementID = elementID;
			this.input = input;
		}
		
		public String getElementID() {
			return elementID;
		}
		
		public String getInputText() {
			return input;
		}
		
		public void setInputText(String text) {
			getConnection().addPoll(HttpClientPoll.setProperty(elementID, "value", text));
		}
		
		public void setPlaceholder(String placeholder) {
			getConnection().addPoll(HttpClientPoll.setAttribute(elementID, "placeholder", placeholder));
		}
		
		public static TextInputChangedEvent of(JSFunctionConsumingInvokedEvent jsEvent, String elementID, String input) {
			return new TextInputChangedEvent(jsEvent.getServer(), jsEvent.getConnectionInstance(), jsEvent.getContext(), jsEvent.getParameters(), elementID, input);
		}

	}
	
}
