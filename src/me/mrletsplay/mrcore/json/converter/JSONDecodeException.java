package me.mrletsplay.mrcore.json.converter;

import me.mrletsplay.mrcore.json.JSONException;

public class JSONDecodeException extends JSONException {

	private static final long serialVersionUID = -8344308548351285863L;

	public JSONDecodeException(String message) {
		super(message);
	}
	
	public JSONDecodeException(String message, Throwable cause) {
		super(message, cause);
	}

}
