package me.mrletsplay.mrcore.http;

public class HttpException extends RuntimeException {

	private static final long serialVersionUID = -7267836464768955816L;

	public HttpException() {
		super();
	}

	public HttpException(String message) {
		super(message);
	}

	public HttpException(Throwable cause) {
		super(cause);
	}

	public HttpException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
