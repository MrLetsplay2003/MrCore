package me.mrletsplay.mrcore.http.server;

public enum HttpStatusCode {

	OKAY_200("200 OK"),
	NOT_FOUND_404("404 Not Found"),
	INTERNAL_ERROR_500("500 Internal Server Error"),
	MOVED_PERMANENTLY_301("301 Moved Permanently"),
	SEE_OTHER_303("303 See Other"),
	ACCESS_DENIED_403("403 Access Denied");
	
	private final String http;
	
	private HttpStatusCode(String http) {
		this.http = http;
	}
	
	@Override
	public String toString() {
		return http;
	}
	
}
