package me.mrletsplay.mrcore.http.server;

public enum HttpClientPollType {

	REDIRECT("redirect"),
	ALERT("alert"),
	SET_PROPERTY("set_property"),
	SET_ATTRIBUTE("set_attribute"),
	RELOAD_PAGE("reload_page"),
	CUSTOM("custom");
	
	public final String jsName;
	
	private HttpClientPollType(String jsName) {
		this.jsName = jsName;
	}
	
}
