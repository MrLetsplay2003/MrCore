package me.mrletsplay.mrcore.http.server;

public enum HttpDefaultStatusCode implements HttpStatusCode {
	
	OK(200, "OK"),
	NOT_FOUND(404, "NOT FOUND");

	private final int statusCode;
	private final String message;
	
	private HttpDefaultStatusCode(int statusCode, String message) {
		this.statusCode = statusCode;
		this.message = message;
	}
	
	@Override
	public int getStatusCode() {
		return statusCode;
	}

	@Override
	public String getMessage() {
		return message;
	}

}
