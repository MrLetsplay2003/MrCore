package me.mrletsplay.mrcore.http.webinterface.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import me.mrletsplay.mrcore.http.server.css.CSSStylesheet;
import me.mrletsplay.mrcore.http.server.html.HTMLElement;
import me.mrletsplay.mrcore.http.server.html.HTMLElementCheckInput;
import me.mrletsplay.mrcore.http.server.html.HTMLElementCheckInput.CheckInputChangedEvent;
import me.mrletsplay.mrcore.http.server.html.HTMLElementTextInput;
import me.mrletsplay.mrcore.http.server.html.HTMLElementTextInput.TextInputChangedEvent;
import me.mrletsplay.mrcore.http.webinterface.Webinterface;
import me.mrletsplay.mrcore.http.webinterface.WebinterfaceAccount;
import me.mrletsplay.mrcore.http.webinterface.impl.PluginTabBuilder.CheckBoxList.CheckBoxListElement.CheckBoxListElementChangedEvent;

public class PluginTabBuilder {

	private String name;
	private List<PluginTabElement> elements;
	
	public PluginTabBuilder(String name) {
		this.name = name;
		this.elements = new ArrayList<>();
	}
	
	public InputBox addInputBox(String title) {
		InputBox box = new InputBox(title);
		elements.add(box);
		return box;
	}
	
	public CheckBoxList addCheckBoxList(String title) {
		CheckBoxList box = new CheckBoxList(title);
		elements.add(box);
		return box;
	}
	
	public PluginTab build() {
		HTMLElement content = HTMLElement.div();
		elements.forEach(e -> content.addChild(e.toHTML()));
		return new PluginTab(name, content, new CSSStylesheet());
	}
	
	public static interface PluginTabElement {
		
		public HTMLElement toHTML();
		
	}
	
	public static class InputBox implements PluginTabElement {

		private String title, description, placeholder;
		private Consumer<InputBoxChangedEvent> changedListener;
		
		private InputBox(String title) {
			this.title = title;
		}
		
		public InputBox withDescription(String description) {
			this.description = description;
			return this;
		}
		
		public InputBox withPlaceholder(String placeholder) {
			this.placeholder = placeholder;
			return this;
		}
		
		public InputBox changedEvent(Consumer<InputBoxChangedEvent> listener) {
			this.changedListener = listener;
			return this;
		}
		
		@Override
		public HTMLElement toHTML() {
			HTMLElement div = HTMLElement.div();
			div.css()
				.position("relative", "0", "0", "48%", "23%")
				.addProperty("background-color", "white")
				.addProperty("display", "inline-block")
				.addProperty("margin-top", "1%")
				.addProperty("margin-left", "1%")
				.addProperty("margin-right", "1%")
				.addProperty("margin-bottom", "1%");
			
			HTMLElement ttl = HTMLElement.div(title);
			
			ttl.css()
				.position("absolute", "4%", "4%", "92%", "32%")
				.addProperty("font-size", "35px")
				.addProperty("font-weight", "bold")
				.addProperty("font-family", "'Ek Mukta'");
			
			HTMLElement desc = HTMLElement.div(description);
			
			desc.css()
				.position("absolute", "32%", "4%", "92%", "10%")
				.addProperty("font-size", "12px")
				.addProperty("font-family", "'Ek Mukta'");
			
			HTMLElementTextInput input = HTMLElement.inputText();
			
			input.setPlaceholder(placeholder);
			
			input.onChanged().event(event -> {
				if(!Webinterface.isLoggedIn(event.getConnection())) return;
				WebinterfaceAccount acc = Webinterface.getLoggedInAccount(event.getConnection());
				if(changedListener != null) changedListener.accept(new InputBoxChangedEvent(event, acc));
			});
			
			input.css()
				.position("absolute", "50%", "4%", "92%", "30%")
				.addProperty("font-size", "30px")
				.addProperty("border", "1px solid lightgray")
				.addProperty("outline", "none")
				.addProperty("font-family", "'Ek Mukta'")
				.addProperty("padding-left", "10px");
			
			div
				.addChild(ttl)
				.addChild(desc)
				.addChild(input);
			
			return div;
		}
		
		public static class InputBoxChangedEvent extends TextInputChangedEvent {

			private WebinterfaceAccount account;
			
			public InputBoxChangedEvent(TextInputChangedEvent event, WebinterfaceAccount account) {
				super(event.getServer(), event.getConnectionInstance(), event.getContext(), event.getParameters(), event.getElementID(), event.getInputText());
				this.account = account;
			}
			
			public WebinterfaceAccount getAccount() {
				return account;
			}
			
		}
		
	}
	
	public static class CheckBoxList implements PluginTabElement {

		private String title, description;
		private List<CheckBoxListElement> checkBoxes;
		
		private CheckBoxList(String title) {
			this.title = title;
			this.checkBoxes = new ArrayList<>();
		}
		
		public CheckBoxList withDescription(String description) {
			this.description = description;
			return this;
		}
		
		public CheckBoxListElement addElement(String name) {
			CheckBoxListElement el = new CheckBoxListElement(name);
			checkBoxes.add(el);
			return el;
		}
		
		public List<CheckBoxListElement> getElements() {
			return checkBoxes;
		}
		
		@Override
		public HTMLElement toHTML() {
			HTMLElement div = HTMLElement.div();
			div.css()
				.position("relative", "0", "0", "48%", "23%")
				.addProperty("background-color", "white")
				.addProperty("display", "inline-block")
				.addProperty("margin-top", "1%")
				.addProperty("margin-left", "1%")
				.addProperty("margin-right", "1%")
				.addProperty("margin-bottom", "1%");
			
			HTMLElement ttl = HTMLElement.div(title);
			
			ttl.css()
				.position("absolute", "4%", "4%", "92%", "32%")
				.addProperty("font-size", "35px")
				.addProperty("font-weight", "bold")
				.addProperty("font-family", "'Ek Mukta'");
			
			HTMLElement desc = HTMLElement.div(description);
			
			desc.css()
				.position("absolute", "32%", "4%", "92%", "10%")
				.addProperty("font-size", "12px")
				.addProperty("font-family", "'Ek Mukta'");
			
			div
				.addChild(ttl)
				.addChild(desc);
			
			HTMLElement listDiv = HTMLElement.div();
			
			listDiv.css()
				.addProperty("overflow-y", "scroll");
			
			for(CheckBoxListElement el : checkBoxes) {
				HTMLElementCheckInput input = HTMLElement.inputCheckBox();
				
				input.onChanged().event(event -> {
					if(!Webinterface.isLoggedIn(event.getConnection())) return;
					WebinterfaceAccount acc = Webinterface.getLoggedInAccount(event.getConnection());
					if(el.changedListener != null) el.changedListener.accept(new CheckBoxListElementChangedEvent(event, acc));
				});
				
				input.css()
					.position("absolute", "50%", "4%", "92%", "30%")
					.addProperty("font-size", "30px")
					.addProperty("border", "1px solig lightgray")
					.addProperty("outline", "none")
					.addProperty("font-family", "'Ek Mukta'")
					.addProperty("padding-left", "10px");
				
				div.addChild(input);
			}
			
			return div;
		}
		
		public static class CheckBoxListElement {
			
			private String name;
			private Consumer<CheckBoxListElementChangedEvent> changedListener;
			
			public CheckBoxListElement(String name) {
				this.name = name;
			}
			
			public CheckBoxListElement changedEvent(Consumer<CheckBoxListElementChangedEvent> changedListener) {
				this.changedListener = changedListener;
				return this;
			}
			
			public static class CheckBoxListElementChangedEvent extends CheckInputChangedEvent {

				private WebinterfaceAccount account;
				
				public CheckBoxListElementChangedEvent(CheckInputChangedEvent event, WebinterfaceAccount account) {
					super(event.getServer(), event.getConnectionInstance(), event.getContext(), event.getParameters(), event.getElementID(), event.getInputValue());
					this.account = account;
				}
				
				public WebinterfaceAccount getAccount() {
					return account;
				}
				
			}
			
		}
		
	}
	
	public static enum LayoutType {
		
		WIDE,
		THIN;
		
	}
	
}
