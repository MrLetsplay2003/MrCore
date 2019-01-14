package me.mrletsplay.mrcore.http.js.built;

import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.mrcore.http.js.JSClass;

public class JSBuiltClass {

	private JSClass rawClass;
	private String name;
	private List<JSBuiltFunction> instanceFunctions, staticFunctions;
	
	public JSBuiltClass(JSClass rawClass, String name) {
		this.rawClass = rawClass;
		this.name = name;
		this.instanceFunctions = new ArrayList<>();
		this.staticFunctions = new ArrayList<>();
	}
	
	public JSClass getRawClass() {
		return rawClass;
	}
	
	public String getName() {
		return name;
	}
	
	public void addInstanceFunction(JSBuiltFunction function) {
		this.instanceFunctions.add(function);
	}
	
	public List<JSBuiltFunction> getInstanceFunctions() {
		return instanceFunctions;
	}
	
	public void addStaticFunction(JSBuiltFunction function) {
		this.staticFunctions.add(function);
	}
	
	public List<JSBuiltFunction> getStaticFunctions() {
		return staticFunctions;
	}
	
}
