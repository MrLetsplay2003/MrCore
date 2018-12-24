package me.mrletsplay.mrcore.http.html;

import java.util.List;

import me.mrletsplay.mrcore.http.html.built.HTMLBuiltDocument;
import me.mrletsplay.mrcore.http.server.HttpDynamicValue;
import me.mrletsplay.mrcore.misc.NullableOptional;

public interface HTMLDocument {

	public List<HttpDynamicValue<HTMLDocumentBuildEvent, ? extends HTMLElement>> getElements();
	
	public void addElement(HTMLElement element);
	
	public void removeElement(HTMLElement element);
	
	public default HTMLBuiltDocument build() {
		HTMLBuiltDocument b = new HTMLBuiltDocument(this);
		HTMLDocumentBuildEvent event = new HTMLDocumentBuildEvent(this, b);
		for(HttpDynamicValue<HTMLDocumentBuildEvent, ? extends HTMLElement> el : getElements()) {
			NullableOptional<? extends HTMLElement> opt = el.get(event);
			if(!opt.isPresent() || opt.get() == null) continue;
			HTMLElement elm = opt.get();
			b.addElement(elm.build(event));
		}
		return b;
	}
	
}
