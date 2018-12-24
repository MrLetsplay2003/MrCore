package me.mrletsplay.mrcore.http.html;

import java.util.List;
import java.util.Map;

import me.mrletsplay.mrcore.http.html.built.HTMLBuiltElement;
import me.mrletsplay.mrcore.http.server.HttpDynamicValue;
import me.mrletsplay.mrcore.misc.NullableOptional;

public interface HTMLElement {

	public Map<String, HttpDynamicValue<HTMLDocumentBuildEvent, String>> getAttributes();
	
	public List<HttpDynamicValue<HTMLDocumentBuildEvent, ? extends HTMLElement>> getChildren();
	
	public default HTMLBuiltElement build(HTMLDocumentBuildEvent event) {
		HTMLBuiltElement el = new HTMLBuiltElement(this);
		for(Map.Entry<String, HttpDynamicValue<HTMLDocumentBuildEvent, String>> en : getAttributes().entrySet()) {
			NullableOptional<String> opt = en.getValue().get(event);
			if(!opt.isPresent() || opt.get() == null) continue;
			el.addAttribute(en.getKey(), opt.get());
		}
		for(HttpDynamicValue<HTMLDocumentBuildEvent, ? extends HTMLElement> en : getChildren()) {
			NullableOptional<? extends HTMLElement> opt = en.get(event);
			if(!opt.isPresent() || opt.get() == null) continue;
			el.addChild(opt.get().build(event));
		}
		return el;
	}
	
}
