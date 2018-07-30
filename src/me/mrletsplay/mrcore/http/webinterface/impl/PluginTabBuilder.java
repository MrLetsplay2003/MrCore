package me.mrletsplay.mrcore.http.webinterface.impl;

import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.mrcore.http.server.css.CSSStylesheet;
import me.mrletsplay.mrcore.http.server.html.HTMLElement;
import me.mrletsplay.mrcore.http.server.html.HTMLElementTextInput;
import me.mrletsplay.mrcore.http.server.js.JSFunctionConsumingCallable;

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
	
	public PluginTab build() {
		HTMLElement content = HTMLElement.div();
		elements.forEach(e -> content.addChild(e.toHTML()));
		return new PluginTab(name, content, new CSSStylesheet());
	}
	
	public static interface PluginTabElement {
		
		public HTMLElement toHTML();
		
	}
	
	public static class InputBox implements PluginTabElement {

		private String title, description;
		
		private InputBox(String title) {
			this.title = title;
		}
		
		public InputBox withDescription(String description) {
			this.description = description;
			return this;
		}
		
		@Override
		public HTMLElement toHTML() {
			HTMLElement div = HTMLElement.div();
			
			div.css()
				.position("relative", "2%", "2%", "46%", "21%")
				.addProperty("background-color", "white");
			
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
			
			input.onChanged().function(new JSFunctionConsumingCallable() {
				
				@Override
				public void invoke(JSFunctionConsumingInvokedEvent event) {
					System.out.println(event.getParameters());
				}
				
				@Override
				public String getParamsMethod() {
					return "return {something: self.value}";
				}
			});
			
			input.onChanged().event(event -> {
				System.out.println(event.getInput());
			});
			
			input.css()
				.position("absolute", "50%", "4%", "92%", "30%")
				.addProperty("font-size", "30px")
				.addProperty("border", "1px solig lightgray")
				.addProperty("outline", "none")
				.addProperty("font-family", "'Ek Mukta'")
				.addProperty("padding-left", "10px");
			
			div
				.addChild(ttl)
				.addChild(desc)
				.addChild(input);
			
			return div;
		}
		
	}
	
	public static enum LayoutType {
		
		WIDE,
		THIN;
		
	}
	
}
