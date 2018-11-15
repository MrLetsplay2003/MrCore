package me.mrletsplay.mrcore.json;

public class JSONException extends RuntimeException {

	private static final long serialVersionUID = -3632660903729890508L;

	public JSONException(String message) {
		super(message);
	}
	
	public JSONException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
