package me.mrletsplay.mrcore.http.server.html;

import java.util.function.Consumer;

import me.mrletsplay.mrcore.http.server.HttpConnectionInstance;
import me.mrletsplay.mrcore.http.server.HttpServer;
import me.mrletsplay.mrcore.http.server.html.HTMLDocument.HTMLBuiltDocument;
import me.mrletsplay.mrcore.http.server.js.JSFunction;
import me.mrletsplay.mrcore.http.server.js.JSFunctionConsumingCallable.JSFunctionConsumingInvokedEvent;
import me.mrletsplay.mrcore.misc.JSON.JSONObject;

public class HTMLElementTextInput extends HTMLElement {

	private TextInputType inputType;
	private OnChanged onChanged;
	
	protected HTMLElementTextInput(HTMLElementTextInput element) {
		super(element);
		this.inputType = element.inputType;
		this.onChanged = element.onChanged.clone();
	}
	
	protected HTMLElementTextInput(TextInputType type) {
		super("input type=" + type.html);
		this.inputType = type;
		this.onChanged = new OnChanged();
	}
	
	protected HTMLElementTextInput(TextInputType type, String placeholder) {
		super("input type=" + type.html + " placeholder=" + placeholder);
		this.inputType = type;
		this.onChanged = new OnChanged();
	}
	
	public TextInputType getInputType() {
		return inputType;
	}
	
	public OnChanged onChanged() {
		return onChanged;
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

		private String input;
		
		public TextInputChangedEvent(HttpServer server, HttpConnectionInstance connectionInstance, HTMLBuiltDocument context, JSONObject params, String input) {
			super(server, connectionInstance, context, params);
			this.input = input;
		}
		
		public String getInput() {
			return input;
		}
		
		public static TextInputChangedEvent of(JSFunctionConsumingInvokedEvent jsEvent, String input) {
			return new TextInputChangedEvent(jsEvent.getServer(), jsEvent.getConnectionInstance(), jsEvent.getContext(), jsEvent.getParameters(), input);
		}

	}
	
}
