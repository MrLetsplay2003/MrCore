package me.mrletsplay.mrcore.http.js;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import me.mrletsplay.mrcore.http.html.HTMLDocumentBuildEvent;
import me.mrletsplay.mrcore.http.js.built.JSBuiltClass;
import me.mrletsplay.mrcore.http.js.built.JSBuiltFunction;
import me.mrletsplay.mrcore.http.server.HttpDynamicValue;

public class JSClass {

	private HttpDynamicValue<HTMLDocumentBuildEvent, String> name;
	private List<HttpDynamicValue<HTMLDocumentBuildEvent, ? extends JSFunction>> instanceFunctions;
	private List<HttpDynamicValue<HTMLDocumentBuildEvent, ? extends JSFunction>> staticFunctions;
	
	public JSClass(HttpDynamicValue<HTMLDocumentBuildEvent, String> name) {
		this.name = name;
		this.instanceFunctions = new ArrayList<>();
		this.staticFunctions = new ArrayList<>();
	}
	
	public JSClass(String name) {
		this(HttpDynamicValue.of(name));
	}
	
	public void addInstanceFunction(HttpDynamicValue<HTMLDocumentBuildEvent, ? extends JSFunction> function) {
		instanceFunctions.add(function);
	}
	
	public void addInstanceFunction(JSFunction function) {
		addInstanceFunction(HttpDynamicValue.of(function));
	}
	
	public void addStaticFunction(HttpDynamicValue<HTMLDocumentBuildEvent, ? extends JSFunction> function) {
		staticFunctions.add(function);
	}
	
	public void addStaticFunction(JSFunction function) {
		addStaticFunction(HttpDynamicValue.of(function));
	}
	
	public JSBuiltClass build(HTMLDocumentBuildEvent event) {
		Optional<String> nm = name.get(event);
		if(!nm.isPresent()) throw new IllegalStateException("Name must be present");
		JSBuiltClass bC = new JSBuiltClass(this, nm.get());
		for(HttpDynamicValue<HTMLDocumentBuildEvent, ? extends JSFunction> f : instanceFunctions) {
			Optional<? extends JSFunction> fn = f.get(event);
			if(!fn.isPresent()) continue;
			JSBuiltFunction bF = fn.get().build(event);
			if(bF == null) continue;
			bC.addInstanceFunction(bF);
		}
		for(HttpDynamicValue<HTMLDocumentBuildEvent, ? extends JSFunction> f : staticFunctions) {
			Optional<? extends JSFunction> fn = f.get(event);
			if(!fn.isPresent()) continue;
			JSBuiltFunction bF = fn.get().build(event);
			if(bF == null) continue;
			bC.addStaticFunction(bF);
		}
		return bC;
	}
	
}
