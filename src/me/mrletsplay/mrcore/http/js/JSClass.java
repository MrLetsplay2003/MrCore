package me.mrletsplay.mrcore.http.js;

import java.util.List;

public interface JSClass {

	public String getName();
	
	public List<? extends JSFunction> getInstanceFunctions();
	
	public List<? extends JSFunction> getStaticFunctions();
	
}
