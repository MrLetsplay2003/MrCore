package me.mrletsplay.mrcore.http.server.html.built;

import me.mrletsplay.mrcore.http.server.html.HTMLDocument.HttpSiteAccessedEvent;
import me.mrletsplay.mrcore.http.server.html.HTMLElementTextInput;
import me.mrletsplay.mrcore.http.server.html.HTMLElementTextInput.OnChanged;
import me.mrletsplay.mrcore.http.server.html.HTMLElementTextInput.TextInputChangedEvent;
import me.mrletsplay.mrcore.http.server.js.JSFunctionConsumingCallable;
import me.mrletsplay.mrcore.http.server.js.built.JSBuiltFunction;

public class HTMLBuiltElementTextInput extends HTMLBuiltElement {
	
	private BuiltOnChanged onChanged;

	public HTMLBuiltElementTextInput(HTMLBuiltElement parent, HTMLBuiltDocument doc, HTMLElementTextInput base, String id, HttpSiteAccessedEvent event, String[] params) {
		super(parent, doc, base, id, event, params);
		this.onChanged = new BuiltOnChanged(base.onChanged(), this);
		StringBuilder onCh = new StringBuilder();
		if(onChanged.getFunction() != null) {
			onCh.append(onChanged.getFunction().getName() + "(this);");
		}
		if(onChanged.getEventFunction() != null) {
			onCh.append(onChanged.getEventFunction().getName() + "(this);");
		}
		if(onCh.length() != 0) addAttribute("onchange", onCh.toString());
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
						if(!(event.getParameters().get("input") instanceof String)) return;
						String inp = event.getParameters().getString("input");
						base.getEventHandler().accept(TextInputChangedEvent.of(event, element.getID(), inp));
					}
					
					@Override
					public String getParamsMethod() {
						return "return {input: self.value};";
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
	
}
