package me.mrletsplay.mrcore.http.js;

import java.util.List;

public interface JSFunction {

	public String getName();
	
	public List<String> getParameters();
	
	public String getBody();
	
}
