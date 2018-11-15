package me.mrletsplay.mrcore.json;

import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Provides methods for formatting objects into JSON strings
 * @author MrLetsplay2003
 */
public class JSONFormatter {

	/**
	 * Tries to format the object into a JSON strings.<br>
	 * Allowed types are:<br>
	 * - JSON objects<br>
	 * - JSON arrays<br>
	 * - null<br>
	 * - Booleans<br>
	 * - Numbers<br>
	 * - Strings
	 * @param object The object to format
	 * @param fancy Whether this should return the string in a more human-readable form
	 * @return A string representing that object
	 * @throws JSONFormatException If a formatting error occurs
	 */
	public static String formatObject(Object object, boolean fancy) {
		return formatGeneric(object, 0, fancy).toString();
	}
	
	protected static CharSequence formatObject(JSONObject object, int indents, boolean fancy) {
		StringBuilder builder = new StringBuilder("{").append(fancy ? "\n" : "").append(fancy ? space(indents + 1) : "");
		builder.append(object.entrySet().stream()
			.map(en -> new StringBuilder()
					.append("\"")
					.append(escapeString(en.getKey()))
					.append("\"")
					.append(":")
					.append(fancy ? " " : "")
					.append(formatGeneric(en.getValue(), indents + 1, fancy)))
			.collect(Collectors.joining(new StringBuilder(",").append(fancy ? "\n" : "").append(fancy ? space(indents + 1) : ""))));
		return builder.append(fancy ? "\n" : "").append(fancy ? space(indents) : "").append("}");
	}
	
	protected static CharSequence formatArray(JSONArray array, int indents, boolean fancy) {
		StringBuilder builder = new StringBuilder("[").append(fancy ? "\n" : "").append(fancy ? space(indents + 1) : "");
		builder.append(array.stream()
			.map(en -> formatGeneric(en, indents + 1, fancy))
			.collect(Collectors.joining(new StringBuilder(",").append(fancy ? "\n" + space(indents + 1) : ""))));
		return builder.append(fancy ? "\n" : "").append(fancy ? space(indents) : "").append("]");
	}
	
	private static String space(int len){
		return String.join("", Collections.nCopies(len * 2, " "));
	}
	
	private static CharSequence formatGeneric(Object object, int indents, boolean fancy) {
		if(object == null) return "null";
		if(object instanceof Number || object instanceof Boolean) return object.toString();
		if(object instanceof String) return new StringBuilder("\"").append(escapeString((String)object)).append("\"");
		if(object instanceof JSONObject) return formatObject((JSONObject) object, indents, fancy);
		if(object instanceof JSONArray) return formatArray((JSONArray) object, indents, fancy);
		throw new JSONFormatException("Cannot format object of type "+object.getClass().getName());
	}
	
	private static String escapeChar(char c) {
		switch(c) {
			case '"':
				return "\\\"";
			case '\\':
				return "\\\\";
			case '/':
				return "\\/";
			case '\b':
				return "\\b";
			case '\f':
				return "\\f";
			case '\n':
				return "\\n";
			case '\r':
				return "\\r";
			case '\t':
				return "\\t";
			default:
				if(Character.isISOControl(c)) return "\\u" + String.format("%04X", (int) c); // => \\uXXXX
				return Character.toString(c);
		}
	}
	
	/**
	 * Escapes the given string into a JSON-compatible format
	 * @param string The string to format
	 * @return The escaped string
	 */
	public static String escapeJSON(String string) {
		return escapeString(string).toString();
	}
	
	private static CharSequence escapeString(String string) {
		StringBuilder escaped = new StringBuilder();
		string.chars().mapToObj(i -> (char)i).map(c -> escapeChar(c)).forEach(escaped::append);;
		return escaped;
	}
	
}
