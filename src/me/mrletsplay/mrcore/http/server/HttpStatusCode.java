package me.mrletsplay.mrcore.http.server;

public enum HttpStatusCode {

	OKAY_200("200 OK"),
	NOT_FOUND_404("404 Not Found"),
	INTERNAL_ERROR_500("500 Internal Server Error");
	
	private final String http;
	
	private HttpStatusCode(String http) {
		this.http = http;
	}
	
	@Override
	public String toString() {
		return http;
	}
	
}
