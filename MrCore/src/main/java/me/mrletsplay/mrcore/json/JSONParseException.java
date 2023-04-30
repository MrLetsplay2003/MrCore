package me.mrletsplay.mrcore.json;

public class JSONParseException extends JSONException {

	private static final long serialVersionUID = 79760301767911820L;

	public JSONParseException(String message, int index) {
		super(message + (index != -1 ? " (at char " + index + ")" : ""));
	}

	public JSONParseException(String message, int index, Throwable cause) {
		super(message + (index != -1 ? " (at char " + index + ")" : ""), cause);
	}

}
