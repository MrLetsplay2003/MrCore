package me.mrletsplay.mrcore.http.server;

public enum HttpClientPollType {

	REDIRECT("redirect"),
	ALERT("alert"),
	CUSTOM("custom");
	
	public final String jsName;
	
	private HttpClientPollType(String jsName) {
		this.jsName = jsName;
	}
	
}
