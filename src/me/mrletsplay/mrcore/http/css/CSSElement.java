package me.mrletsplay.mrcore.http.css;

import java.util.List;

public interface CSSElement {

	public String getTarget();
	
	public String getSelector();
	
	public List<? extends CSSProperty> getProperties();
	
}
