package me.mrletsplay.mrcore.http.server.html.built;

import me.mrletsplay.mrcore.http.server.html.HTMLDocument.HttpSiteAccessedEvent;
import me.mrletsplay.mrcore.http.server.html.HTMLElementCheckInput;
import me.mrletsplay.mrcore.http.server.html.HTMLElementCheckInput.CheckInputChangedEvent;
import me.mrletsplay.mrcore.http.server.html.HTMLElementCheckInput.OnChanged;
import me.mrletsplay.mrcore.http.server.html.HTMLElementCheckInput.OnChecked;
import me.mrletsplay.mrcore.http.server.html.HTMLElementCheckInput.OnUnchecked;
import me.mrletsplay.mrcore.http.server.js.JSFunction;
import me.mrletsplay.mrcore.http.server.js.JSFunctionConsumingCallable;
import me.mrletsplay.mrcore.http.server.js.built.JSBuiltFunction;

public class HTMLBuiltElementCheckInput extends HTMLBuiltElement {
	
	private BuiltOnChanged onChanged;
	private BuiltOnChecked onChecked;
	private BuiltOnUnchecked onUnchecked;
	private boolean checked;

	public HTMLBuiltElementCheckInput(HTMLBuiltElement parent, HTMLBuiltDocument doc, HTMLElementCheckInput base, String id, HttpSiteAccessedEvent event, String[] params) {
		super(parent, doc, base, id, event, params);
		this.onChanged = new BuiltOnChanged(base.onChanged(), this);
		this.onChecked = new BuiltOnChecked(base.onChecked(), this);
		this.onUnchecked = new BuiltOnUnchecked(base.onUnchecked(), this);
		StringBuilder onCh = new StringBuilder();
		if(onChanged.getFunction() != null) {
			onCh.append(onChanged.getFunction().getName() + "(this);");
		}
		if(onChanged.getEventFunction() != null) {
			onCh.append(onChanged.getEventFunction().getName() + "(this);");
		}
		if(onChecked.getOnCheckedFunction() != null) {
			onCh.append(onChecked.getOnCheckedFunction().getName() + "(this);");
		}
		if(onUnchecked.getOnUncheckedFunction() != null) {
			onCh.append(onUnchecked.getOnUncheckedFunction().getName() + "(this);");
		}
		if(onCh.length() != 0) addAttribute("onchange", onCh.toString());
		this.checked = base.getDefaultValue() != null && base.getDefaultValue().apply(event);
		if(checked) addAttribute("checked", "checked");
	}
	
	public BuiltOnChanged onChanged() {
		return onChanged;
	}
	
	public BuiltOnChecked onChecked() {
		return onChecked;
	}
	
	public BuiltOnUnchecked onUnchecked() {
		return onUnchecked;
	}
	
	public boolean isChecked() {
		return checked;
	}
	
	public static class BuiltOnChanged {
		
		private JSBuiltFunction function, eventFunction;
		
		public BuiltOnChanged(OnChanged base, HTMLBuiltElement element) {
			if(base.getFunction() != null) function = element.getDocument().getScript().appendFunction(base.getFunction());
			if(base.getEventHandler() != null) {
				JSFunctionConsumingCallable consC = new JSFunctionConsumingCallable() {
					
					@Override
					public void invoke(JSFunctionConsumingInvokedEvent event) {
						if(!requireParams(event.getParameters(), "input")) return;
						if(!(event.getParameters().get("input") instanceof Boolean)) return;
						boolean inp = event.getParameters().getBoolean("input");
						base.getEventHandler().accept(CheckInputChangedEvent.of(event, element.getID(), inp));
					}
					
					@Override
					public String getParamsMethod() {
						return "return {input: self.checked};";
					}
				};
				eventFunction = element.getDocument().getScript().appendFunction(consC);
			}
		}
		
		public JSBuiltFunction getFunction() {
			return function;
		}
		
		public JSBuiltFunction getEventFunction() {
			return eventFunction;
		}
		
	}
	
	public static class BuiltOnChecked {
		
		private JSBuiltFunction onChecked, function, eventFunction;
		
		public BuiltOnChecked(OnChecked base, HTMLBuiltElement element) {
			if(!base.css().isEmpty()) element.getDocument().getCSS().addElement("#" + element.getID() + ":checked", base.css());
			if(base.getFunction() != null) this.function = element.getDocument().getScript().appendFunction(base.getFunction());
			if(base.getEventHandler() != null) {
				JSFunctionConsumingCallable eventFct = new JSFunctionConsumingCallable() {
					
					@Override
					public void invoke(JSFunctionConsumingInvokedEvent event) {
						base.getEventHandler().accept(CheckInputChangedEvent.of(event, element.getID(), true));
					}
					
					@Override
					public String getParamsMethod() {
						return "return {value: self.checked};";
					}
				};
				this.eventFunction = element.getDocument().getScript().appendFunction(eventFct);
			}
			if(function != null || eventFunction != null) {
				StringBuilder innerJS = new StringBuilder();
				innerJS.append("if(self.checked) {");
				if(function != null) innerJS.append(function.getName() + "();");
				if(eventFunction != null) innerJS.append(eventFunction.getName() + "();");
				innerJS.append("}");
				onChecked = element.getDocument().getScript().appendFunction(JSFunction.of(innerJS.toString()));
			}
		}
		
		public JSBuiltFunction getFunction() {
			return function;
		}
		
		public JSBuiltFunction getEventFunction() {
			return eventFunction;
		}
		
		public JSBuiltFunction getOnCheckedFunction() {
			return onChecked;
		}
		
	}

	public static class BuiltOnUnchecked {
		
		private JSBuiltFunction onUnchecked, function, eventFunction;
		
		public BuiltOnUnchecked(OnUnchecked base, HTMLBuiltElement element) {
			if(base.getFunction() != null) this.function = element.getDocument().getScript().appendFunction(base.getFunction());
			if(base.getEventHandler() != null) {
				JSFunctionConsumingCallable eventFct = new JSFunctionConsumingCallable() {
					
					@Override
					public void invoke(JSFunctionConsumingInvokedEvent event) {
						base.getEventHandler().accept(CheckInputChangedEvent.of(event, element.getID(), true));
					}
					
					@Override
					public String getParamsMethod() {
						return "return {value: self.checked};";
					}
				};
				this.eventFunction = element.getDocument().getScript().appendFunction(eventFct);
			}
			if(function != null || eventFunction != null) {
				StringBuilder innerJS = new StringBuilder();
				innerJS.append("if(!self.checked) {");
				if(function != null) innerJS.append(function.getName() + "();");
				if(eventFunction != null) innerJS.append(eventFunction.getName() + "();");
				innerJS.append("}");
				onUnchecked = element.getDocument().getScript().appendFunction(JSFunction.of(innerJS.toString()));
			}
		}
		
		public JSBuiltFunction getFunction() {
			return function;
		}
		
		public JSBuiltFunction getEventFunction() {
			return eventFunction;
		}
		
		public JSBuiltFunction getOnUncheckedFunction() {
			return onUnchecked;
		}
		
	}
	
}
