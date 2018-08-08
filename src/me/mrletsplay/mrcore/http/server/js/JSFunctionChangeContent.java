package me.mrletsplay.mrcore.http.server.js;

import me.mrletsplay.mrcore.http.server.html.HTMLElement;

public class JSFunctionChangeContent extends JSFunction {

	private HTMLElement element;
	private JSFunction getContentFunction;
	
	public JSFunctionChangeContent(HTMLElement element, JSFunction getContentFunction) {
		this.element = element;
		this.getContentFunction = getContentFunction;
		addAdditional(getContentFunction);
		setInnerJS(event -> {
			return "setContent(\"" + event.getDocument().getElement(element).getID() + "\", " + event.getDocument().getScript().getFunction(getContentFunction).getName() + "());";
		});
	}
	
	public JSFunctionChangeContent(HTMLElement element, String content) {
		this(element, JSFunction.of("return \"" + content + "\";"));
	}
	
	public HTMLElement getElement() {
		return element;
	}
	
	public JSFunction getGetContentFunction() {
		return getContentFunction;
	}

}
