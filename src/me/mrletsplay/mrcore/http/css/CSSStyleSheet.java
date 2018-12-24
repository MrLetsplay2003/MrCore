package me.mrletsplay.mrcore.http.css;

import java.util.List;

public interface CSSStyleSheet {

	public List<? extends CSSElement> getElements();
	
	public void addElement(CSSElement element);
	
	public void removeElement(CSSElement element);
	
}
