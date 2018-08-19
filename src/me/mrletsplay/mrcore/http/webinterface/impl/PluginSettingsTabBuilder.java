package me.mrletsplay.mrcore.http.webinterface.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import me.mrletsplay.mrcore.http.server.css.CSSStyleElement.CSSTarget.CSSTargetType;
import me.mrletsplay.mrcore.http.server.css.CSSStyleSheet;
import me.mrletsplay.mrcore.http.server.html.HTMLElement;
import me.mrletsplay.mrcore.http.server.html.HTMLElementCheckInput;
import me.mrletsplay.mrcore.http.server.html.HTMLElementCheckInput.CheckInputChangedEvent;
import me.mrletsplay.mrcore.http.server.html.HTMLElementLabel;
import me.mrletsplay.mrcore.http.server.html.HTMLElementTextInput;
import me.mrletsplay.mrcore.http.server.html.HTMLElementTextInput.TextInputChangedEvent;
import me.mrletsplay.mrcore.http.server.html.built.HTMLBuiltElementCheckInput;
import me.mrletsplay.mrcore.http.server.html.built.HTMLBuiltElementLabel;
import me.mrletsplay.mrcore.http.server.js.JSFunction;
import me.mrletsplay.mrcore.http.webinterface.Webinterface;
import me.mrletsplay.mrcore.http.webinterface.Webinterface.WebinterfacePageAccessedEvent;
import me.mrletsplay.mrcore.http.webinterface.WebinterfaceAccount;
import me.mrletsplay.mrcore.http.webinterface.impl.PluginSettingsTabBuilder.CheckBoxList.CheckBoxListElement.CheckBoxListElementChangedEvent;

public class PluginSettingsTabBuilder {

	private String name;
	private List<PluginTabElement> elements;
	
	public PluginSettingsTabBuilder(String name) {
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
		return new PluginTabImpl(name, content, new CSSStyleSheet());
	}
	
	public static interface ElementListable {
		
		public String getName();
		
		public String getDescription();
		
		public HTMLElement getTD();
		
	}
	
	public static interface PluginTabElement {
		
		public PluginTabElement setLayoutWidth(LayoutConstant layoutWidth);
		
		public PluginTabElement setLayoutHeight(LayoutConstant layoutHeight);
		
		public LayoutConstant getLayoutWidth();
		
		public LayoutConstant getLayoutHeight();
		
		public HTMLElement toHTML();
		
	}
	
	public static abstract class AbstractPluginTabElement implements PluginTabElement {
		
		private LayoutConstant layoutWidth, layoutHeight;
		
		public AbstractPluginTabElement() {
			this.layoutWidth = LayoutConstant.HALF_PAGE;
			this.layoutHeight = LayoutConstant.QUARTER_PAGE;
		}
		
		public AbstractPluginTabElement setLayoutWidth(LayoutConstant layoutWidth) {
			this.layoutWidth = layoutWidth;
			return this;
		}
		
		public AbstractPluginTabElement setLayoutHeight(LayoutConstant layoutHeight) {
			this.layoutHeight = layoutHeight;
			return this;
		}
		
		public LayoutConstant getLayoutWidth() {
			return layoutWidth;
		}
		
		public LayoutConstant getLayoutHeight() {
			return layoutHeight;
		}
		
		public abstract HTMLElement toHTML();
		
	}
	
	public static enum LayoutConstant {
		
		QUARTER_PAGE("23%"),
		HALF_PAGE("48%"),
		WHOLE_PAGE("98%");

		public final String css;
		
		private LayoutConstant(String css) {
			this.css = css;
		}
		
	}
	
	public static class InputBox extends AbstractPluginTabElement {

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
		public InputBox setLayoutHeight(LayoutConstant layoutHeight) {
			super.setLayoutHeight(layoutHeight);
			return this;
		}
		
		@Override
		public InputBox setLayoutWidth(LayoutConstant layoutWidth) {
			super.setLayoutWidth(layoutWidth);
			return this;
		}
		
		@Override
		public HTMLElement toHTML() {
			HTMLElement div = HTMLElement.div();
			div.css()
				.position("relative", "0", "0", getLayoutWidth().css, getLayoutHeight().css)
				.addProperty("background-color", "white")
				.addProperty("display", "inline-block")
				.addProperty("margin-top", "1%")
				.addProperty("margin-left", "1%")
				.addProperty("margin-right", "1%")
				.addProperty("margin-bottom", "1%")
				.addProperty("vertical-align", "top");
			
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
	
	public static class CheckBoxList extends AbstractPluginTabElement {

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
		public CheckBoxList setLayoutHeight(LayoutConstant layoutHeight) {
			super.setLayoutHeight(layoutHeight);
			return this;
		}
		
		@Override
		public CheckBoxList setLayoutWidth(LayoutConstant layoutWidth) {
			super.setLayoutWidth(layoutWidth);
			return this;
		}
		
		@Override
		public HTMLElement toHTML() {
			HTMLElement div = HTMLElement.div();
			div.css()
				.position("relative", "0", "0", getLayoutWidth().css, getLayoutHeight().css)
				.addProperty("background-color", "white")
				.addProperty("display", "inline-block")
				.addProperty("margin-top", "1%")
				.addProperty("margin-left", "1%")
				.addProperty("margin-right", "1%")
				.addProperty("margin-bottom", "1%")
				.addProperty("vertical-align", "top")
				.addProperty("font-family", "'Ek Mukta'");
			
			div.css().targetQuery(CSSTargetType.CHILD, "th")
				.addProperty("border-bottom", "1px solid lightgray");
			
			HTMLElement ttl = HTMLElement.div(title);
			
			ttl.css()
				.position("absolute", "0", "10px", "calc(100% - 20px)", "auto")
				.addProperty("display", "block")
				.addProperty("font-size", "35px")
				.addProperty("font-weight", "bold")
				.addProperty("font-family", "'Ek Mukta'");
			
			HTMLElement desc = HTMLElement.div(description);
			
			desc.css()
				.position("absolute", "50px", "10px", "92%", "auto")
				.addProperty("font-size", "12px")
				.addProperty("font-family", "'Ek Mukta'");
			
			div
				.addChild(ttl)
				.addChild(desc);
			
			HTMLElement listDiv = HTMLElement.div();
			
			listDiv.css()
				.position("absolute", "70px", "10px", "96%", "calc(100% - 75px)")
				.addProperty("overflow-y", "scroll");
			
			HTMLElement table = HTMLElement.table();
			
			table.css()
				.addProperty("width", "100%")
				.addProperty("text-align", "left");
			
			table
				.addChild(HTMLElement.thead()
					.addChild(HTMLElement.th("Name").addAttribute("width", "30%"))
					.addChild(HTMLElement.th("Description").addAttribute("width", "60%"))
					.addChild(HTMLElement.th("Value").addAttribute("width", "15%")));
			
			HTMLElement tbody = HTMLElement.tbody();
			
			for(CheckBoxListElement el : checkBoxes) {
				HTMLElement tr = HTMLElement.tr();
				
				HTMLElement td1 = HTMLElement.td(el.name);
//				td1.css()
//					.addProperty("min-width", "100px")
//					.addProperty("text-align", "center");
				
				tr.addChild(td1);
				
				HTMLElement td2 = HTMLElement.td(el.description);
				
				tr.addChild(td2);
				
				HTMLElement td = HTMLElement.td();
				
//				td.css()
//					.addProperty("text-align", "center");
				
				HTMLElementCheckInput input = HTMLElement.inputCheckBox();
				if(el.defaultValue != null) input.setDefaultValue(event -> el.defaultValue.apply(new WebinterfacePageAccessedEvent(event)));
				
				HTMLElementLabel lbl = HTMLElement.dynamic(HTMLElement.label(input),
						event -> {
							HTMLBuiltElementCheckInput cEl = (HTMLBuiltElementCheckInput) event.getElement().getDocument().getElement(((HTMLBuiltElementLabel) event.getElement()).getTarget());
							return cEl.isChecked() ? "Enabled" : "Disabled";
						});
				
				lbl.css()
					.addProperty("display", "inline-block")
					.addProperty("height", "100%")
					.addProperty("border-radius", "5px")
					.addProperty("padding", "2px")
					.addProperty("background-color", "orangered")
					.addProperty("user-select", "none")
					.addProperty("min-width", "100px")
					.addProperty("text-align", "center");
				
				lbl.onHover().css()
					.addProperty("cursor", "pointer");
				
				input.onChecked().css().targetElement(CSSTargetType.ADJACENT_SIBLING, lbl)
					.addProperty("background-color", "limegreen");
				
				input.onChecked().function(JSFunction.changeContent(lbl, "Enabled"));
				input.onUnchecked().function(JSFunction.changeContent(lbl, "Disabled"));
				
				input.css()
					.addProperty("display", "none");
				
				input.onChanged().event(event -> {
					if(!Webinterface.isLoggedIn(event.getConnection())) return;
					WebinterfaceAccount acc = Webinterface.getLoggedInAccount(event.getConnection());
					if(el.changedListener != null) el.changedListener.accept(new CheckBoxListElementChangedEvent(event, acc));
				});
				
				input.css()
//					.position("absolute", "50%", "4%", "92%", "30%")
//					.addProperty("width", "100%")
					.addProperty("font-size", "30px")
					.addProperty("border", "1px solid lightgray")
					.addProperty("outline", "none")
					.addProperty("font-family", "'Ek Mukta'")
					.addProperty("padding-left", "10px");
				
				td.addChild(input);
				td.addChild(lbl);	
				tr.addChild(td);
				
				tbody.addChild(tr);
			}
			
			table.addChild(tbody);
			listDiv.addChild(table);
			div.addChild(listDiv);
			
			return div;
		}
		
		public static class CheckBoxListElement {
			
			private String name, description;
			private Consumer<CheckBoxListElementChangedEvent> changedListener;
			private Function<WebinterfacePageAccessedEvent, Boolean> defaultValue;
			
			public CheckBoxListElement(String name) {
				this.name = name;
			}
			
			public CheckBoxListElement withDescription(String description) {
				this.description = description;
				return this;
			}
			
			public CheckBoxListElement withDefaultValue(Function<WebinterfacePageAccessedEvent, Boolean> defaultValue) {
				this.defaultValue = defaultValue;
				return this;
			}
			
			public CheckBoxListElement withDefaultValue(boolean defaultValue) {
				this.defaultValue = event -> defaultValue;
				return this;
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
