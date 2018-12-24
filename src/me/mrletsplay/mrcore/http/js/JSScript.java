package me.mrletsplay.mrcore.http.js;

import java.util.List;

public interface JSScript {

	public List<? extends JSFunction> getFunctions();
	
	public void addFunction(JSFunction function);
	
	public void removeFunction(JSFunction function);
	
	public String getBody();
	
}
