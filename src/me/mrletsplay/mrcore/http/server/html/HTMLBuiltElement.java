package me.mrletsplay.mrcore.http.server.html;

/*
 * TODO
 */
public class HTMLBuiltElement {

	private HTMLElement base;
	private String id;
	
	public HTMLBuiltElement(HTMLElement base, String id) {
		this.base = base;
		this.id = id;
	}
	
	public HTMLElement getBase() {
		return base;
	}
	
	public String getID() {
		return id;
	}

}
