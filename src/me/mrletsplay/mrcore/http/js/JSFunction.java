package me.mrletsplay.mrcore.http.js;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.http.html.HTMLDocumentBuildEvent;
import me.mrletsplay.mrcore.http.js.built.JSBuiltFunction;
import me.mrletsplay.mrcore.http.server.HttpDynamicValue;

public class JSFunction {
	
	private HttpDynamicValue<HTMLDocumentBuildEvent, String> name, body;
	private List<HttpDynamicValue<HTMLDocumentBuildEvent, String>> parameters;
	
	public JSFunction(HttpDynamicValue<HTMLDocumentBuildEvent, String> name, HttpDynamicValue<HTMLDocumentBuildEvent, String> body) {
		this.name = name;
		this.body = body;
		this.parameters = new ArrayList<>();
	}
	
	public JSFunction(String name, HttpDynamicValue<HTMLDocumentBuildEvent, String> body) {
		this(HttpDynamicValue.of(name), body);
	}
	
	public JSFunction(String name, String body) {
		this(HttpDynamicValue.of(name), HttpDynamicValue.of(body));
	}

	public HttpDynamicValue<HTMLDocumentBuildEvent, String> getName() {
		return name;
	}
	
	public void addParameter(HttpDynamicValue<HTMLDocumentBuildEvent, String> param) {
		parameters.add(param);
	}
	
	public List<HttpDynamicValue<HTMLDocumentBuildEvent, String>> getParameters() {
		return parameters;
	}
	
	public HttpDynamicValue<HTMLDocumentBuildEvent, String> getBody() {
		return body;
	}
	
	public JSBuiltFunction build(HTMLDocumentBuildEvent event) {
		Optional<String> nm = name.get(event);
		Optional<String> bd = body.get(event);
		if(!nm.isPresent()) throw new IllegalStateException("Name has to be present");
		if(!bd.isPresent()) return null;
		List<String> params = parameters.stream().map(e -> e.get(event)).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
		JSBuiltFunction f = new JSBuiltFunction(this, nm.get(), params, bd.get());
		return f;
	}
	
}
