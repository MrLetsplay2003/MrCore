package me.mrletsplay.mrcore.http.server.js.built;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.http.server.html.HTMLDocument.HttpSiteAccessedEvent;
import me.mrletsplay.mrcore.http.server.html.built.HTMLBuiltDocument;
import me.mrletsplay.mrcore.http.server.js.JSFunction;

public class JSBuiltFunction {

	private JSFunction base;
	private String name;
	private String innerJS;
	private JSFunctionBuildEvent buildEvent;
	private List<JSBuiltFunction> additionalFunctions;
	
	public JSBuiltFunction(JSFunction base, String name, HTMLBuiltDocument doc, HttpSiteAccessedEvent event) {
		this.base = base;
		this.name = name;
		this.buildEvent = new JSFunctionBuildEvent(name, doc, event);
		this.additionalFunctions = new ArrayList<>();
		base.getAdditionalFunctions().forEach(f -> additionalFunctions.add(doc.getScript().appendFunction(f)));
	}
	
	public void completeBuild() {
		this.innerJS = base.getInnerJS(buildEvent);
	}
	
	public JSFunction getBase() {
		return base;
	}
	
	public String getName() {
		return name;
	}
	
	public String getInnerJS() {
		return innerJS;
	}
	
	public List<String> getParameters() {
		return base.getParameters();
	}

	public String asString() {
		List<String> params = new ArrayList<>(getParameters());
		params.add("self");
		StringBuilder builder = new StringBuilder();
		builder.append(
				"function " + name + "(" + params.stream().collect(Collectors.joining(",")) + ") {" +
				innerJS +
				"}");
		additionalFunctions.forEach(f -> builder.append(f.asString()));
		return builder.toString();
		
	}
	
	public static class JSFunctionBuildEvent {
		
		private String name;
		private HTMLBuiltDocument doc;
		private HttpSiteAccessedEvent accessEvent;
		
		public JSFunctionBuildEvent(String name, HTMLBuiltDocument doc, HttpSiteAccessedEvent event) {
			this.name = name;
			this.doc = doc;
			this.accessEvent = event;
		}
		
		public String getFunctionName() {
			return name;
		}
		
		public HTMLBuiltDocument getDocument() {
			return doc;
		}
		
		public HttpSiteAccessedEvent getAccessEvent() {
			return accessEvent;
		}
		
	}
	
}
