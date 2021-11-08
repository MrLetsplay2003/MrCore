package me.mrletsplay.mrcore.json.converter;

import me.mrletsplay.mrcore.json.JSONException;

public class JSONEncodeException extends JSONException {

	private static final long serialVersionUID = -6330608097888280178L;

	public JSONEncodeException(String message) {
		super(message);
	}
	
	public JSONEncodeException(String message, Throwable cause) {
		super(message, cause);
	}

}
