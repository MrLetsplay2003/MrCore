package me.mrletsplay.mrcore.misc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.stream.Collectors;

public class JSON {
	
	/**
	 * Implementation of a JSON object according to <a href="http://www.json.org/">The JSON Data Interchange Standard</a>
	 * @author MrLetsplay2003
	 */
	public static class JSONObject extends HashMap<String, Object> {
		
		private static final long serialVersionUID = -7624968898431467371L;
		
		/**
		 * Creates an empty JSONObject
		 */
		public JSONObject() {
			super();
		}
		
		/**
		 * Creates a JSON object from the given source string<br>
		 * The string has to be a valid JSON object
		 * @param source The source string to read from
		 * @throws JSONParseException If a parsing error occurs
		 * @throws ClassCastException If the given string does not represent a JSON object
		 */
		public JSONObject(String source) {
			this((JSONObject) JSONParser.parse(source));
		}
		
		/**
		 * Creates a JSON object which has identical properties to the JSONObject parameter<br>
		 * Giving null as a parameter will result in an empty JSONObject, no exception will be thrown
		 * @param fromObject The JSONObject to copy
		 */
		public JSONObject(JSONObject fromObject) {
			super(fromObject != null ? fromObject : new HashMap<>());
		}
		
		/**
		 * Sets the given key to a value<br>
		 * Already set properties will be overridden<br>
		 * This method only allows for Strings, Numbers, other JSON objects or JSON arrays to be set<br>
		 * If another type of value is set, the implementation will throw an error when calling {@link #toString()}
		 * @param key The key of the property
		 * @param value The value to set it to
		 */
		public void set(String key, Object value) {
			put(key, value);
		}
		
		/**
		 * Checks whether a specific property exists within this JSON object
		 * @param key The key of the property
		 * @return true if the given property exists, false otherwise
		 */
		public boolean has(String key) {
			return containsKey(key);
		}
		
		/**
		 * Gets a property from this JSON object<br>
		 * If the given key can't be found, a {@link JSONException} is thrown<br>
		 * <br>
		 * The get[type] methods return their type respectively<br>
		 * If a type can not be converted, a {@link ClassCastException} is thrown
		 * @param key The key of the property
		 * @return The value of the property
		 * @throws JSONException If the given key is not found
		 */
		public Object get(String key) {
			if(!containsKey(key)) throw new JSONException("Object doesn't have the property \""+key+"\"");
			return super.get(key);
		}
		
		/**
		 * @see #get(String)
		 */
		public String getString(String key) {
			return (String) get(key);
		}

		/**
		 * @see #get(String)
		 */
		public Boolean getBoolean(String key) {
			return (Boolean) get(key);
		}

		/**
		 * @see #get(String)
		 */
		public Number getNumber(String key) {
			return (Number) get(key);
		}

		/**
		 * @see #get(String)
		 */
		public int getInt(String key) {
			return getNumber(key).intValue();
		}

		/**
		 * @see #get(String)
		 */
		public long getLong(String key) {
			return getNumber(key).longValue();
		}

		/**
		 * @see #get(String)
		 */
		public double getDouble(String key) {
			return getNumber(key).doubleValue();
		}

		/**
		 * @see #get(String)
		 */
		public JSONObject getJSONObject(String key) {
			return (JSONObject) get(key);
		}

		/**
		 * @see #get(String)
		 */
		public JSONArray getJSONArray(String key) {
			return (JSONArray) get(key);
		}

		public boolean isOfType(String key, JSONType type) {
			switch (type) {
				case OBJECT:
					return has(key) && get(key) instanceof JSONObject;
				case ARRAY:
					return has(key) && get(key) instanceof JSONArray;
				case STRING:
					return has(key) && get(key) instanceof String;
				case BOOLEAN:
					return has(key) && get(key) instanceof Boolean;
				case DOUBLE:
				case INTEGER:
				case LONG:
				case NUMBER:
					return has(key) && get(key) instanceof Number;
				case NULL:
					return has(key) && get(key) == null;
			}
			return false;
		}
		
		/**
		 * Converts this JSON object into a JSON string according to <a href="http://www.json.org/">The JSON Data Interchange Standard</a>
		 * @throws JSONException If a conversion error occurs (e.g. a property has an invalid type)
		 */
		@Override
		public String toString() {
			return JSONFormatter.formatObject(this, false).toString();
		}
		
		/**
		 * A more human-readable form of {@link #toString()}
		 * @throws JSONException If a conversion error occurs (e.g. a property has an invalid type)
		 */
		public String toFancyString() {
			return JSONFormatter.formatObject(this, true).toString();
		}
		
	}

	/**
	 * Implementation of a JSON array according to <a href="http://www.json.org/">The JSON Data Interchange Standard</a><br>
	 * This also extends the ArrayList class to provide iterator/stream support
	 * @author MrLetsplay2003
	 */
	public static class JSONArray extends ArrayList<Object> {

		private static final long serialVersionUID = 7419047721431768026L;

		/**
		 * Creates an empty JSON array
		 */
		public JSONArray() {
			super();
		}

		/**
		 * Creates a JSON array from the given collection
		 * @param collection The collection to create from
		 */
		public JSONArray(Collection<?> collection) {
			super(collection);
		}

		/**
		 * Creates a JSON array from the given source string<br>
		 * The string has to be a valid JSON array
		 * @param source The source string to read from
		 * @throws JSONParseException If a parsing error occurs
		 * @throws ClassCastException If the given string does not represent a JSON array
		 */
		public JSONArray(String source) {
			super(source != null ? (JSONArray) JSONParser.parse(source) : new ArrayList<>());
		}

		/**
		 * Gets an element from this JSON array<br>
		 * If the given index can't be found, an {@link IndexOutOfBoundsException} is thrown as defined by {@link ArrayList#get(int)}<br>
		 * <br>
		 * The get[type] methods return their type respectively<br>
		 * If a type can not be converted, a {@link ClassCastException} is thrown
		 * @param index The index of the element
		 * @return The value of the property
		 * @throws IndexOutOfBoundsException as defined by {@link ArrayList#get(int)}
		 */
		@Override
		public Object get(int index) {
			return super.get(index);
		}
		
		/**
		 * See {@link #get(int)}
		 */
		public String getString(int index) {
			return (String) get(index);
		}

		/**
		 * See {@link #get(int)}
		 */
		public Boolean getBoolean(int index) {
			return (Boolean) get(index);
		}

		/**
		 * See {@link #get(int)}
		 */
		public Number getNumber(int index) {
			return (Number) get(index);
		}

		/**
		 * See {@link #get(int)}
		 */
		public int getInt(int index) {
			return getNumber(index).intValue();
		}

		/**
		 * See {@link #get(int)}
		 */
		public long getLong(int index) {
			return getNumber(index).longValue();
		}

		/**
		 * See {@link #get(int)}
		 */
		public double getDouble(int index) {
			return getNumber(index).doubleValue();
		}

		/**
		 * See {@link #get(int)}
		 */
		public JSONObject getJSONObject(int index) {
			return (JSONObject) get(index);
		}

		/**
		 * See {@link #get(int)}
		 */
		public JSONArray getJSONArray(int index) {
			return (JSONArray) get(index);
		}

		public boolean isOfType(int index, JSONType type) {
			switch (type) {
				case OBJECT:
					return get(index) instanceof JSONObject;
				case ARRAY:
					return get(index) instanceof JSONArray;
				case STRING:
					return get(index) instanceof String;
				case BOOLEAN:
					return get(index) instanceof Boolean;
				case DOUBLE:
				case INTEGER:
				case LONG:
				case NUMBER:
					return get(index) instanceof Number;
				case NULL:
					return get(index) == null;
			}
			return false;
		}

		/**
		 * Converts this JSON object into a JSON string according to <a href="http://www.json.org/">The JSON Data Interchange Standard</a>
		 * @throws JSONException If a conversion error occurs (e.g. a property has an invalid type)
		 */
		@Override
		public String toString() {
			return JSONFormatter.formatArray(this, 0, false).toString();
		}
		
		/**
		 * A more human-readable form of {@link #toString()}
		 * @throws JSONException If a conversion error occurs (e.g. a property has an invalid type)
		 */
		public String toFancyString() {
			return JSONFormatter.formatArray(this, 0, true).toString();
		}
		
	}
	
	public static enum JSONType {
		
		NULL,
		NUMBER,
		INTEGER,
		DOUBLE,
		LONG,
		BOOLEAN,
		OBJECT,
		ARRAY,
		STRING,
		;
		
	}
	
	private static class CharReader {
		
		private int currentIndex;
		private String string;
		
		public CharReader(String string) {
			this.string = string;
			this.currentIndex = 0;
		}
		
//		public char current() {
//			return string.charAt(currentIndex);
//		}
		
		public char next() {
			if(currentIndex == string.length()) return '\0'; //null char on eof
			return string.charAt(currentIndex++);
		}
		
		public String next(int amount) {
			char[] chs = new char[amount];
			for(int i = 0; i < amount; i++) chs[i] = next();
			return new String(chs);
		}
		
		public char nextIgnoreWhitespaces() {
			char c = next();
			while(Character.isWhitespace(c)) c = next();
			return c;
		}
		
//		public CharReader skip(int num) {
//			currentIndex += num;
//			if(currentIndex >= string.length()) throw new RuntimeException();
//			return this;
//		}
		
		public CharReader revert(int num) {
			currentIndex -= num;
			if(currentIndex < 0) throw new RuntimeException();
			return this;
		}
		
		public boolean hasNext() {
			return currentIndex < string.length();
		}
		
	}
	
	/**
	 * Provides methods for parsing JSON strings into objects
	 * @author MrLetsplay2003
	 */
	public static class JSONParser {
		
		/**
		 * Tries to parse the string into a JSON generic value.<br>
		 * These include:<br>
		 * - JSON objects<br>
		 * - JSON arrays<br>
		 * - null<br>
		 * - Booleans<br>
		 * - Numbers<br>
		 * - Strings
		 * @param source The string to parse
		 * @return An object of any of the types mentioned above
		 * @throws JSONParseException If a parsing error occurs
		 */
		public static Object parse(String source) {
			if(source == null) return null;
			return readGeneric(new CharReader(source));
		}
		
		private static JSONObject readObject(CharReader reader) {
			JSONObject obj = new JSONObject();
			boolean hasComma = false;
			while(reader.hasNext()) {
				char c = reader.nextIgnoreWhitespaces();
				switch(c) {
					case ',':
						if(hasComma) throw new JSONParseException("Double comma", reader.currentIndex);
						hasComma = true;
						break;
					case '}':
						return obj;
					case '"':
						if(!hasComma && !obj.isEmpty()) throw new JSONParseException("Missing comma separator", reader.currentIndex);
						String key = readString(reader);
						if(reader.nextIgnoreWhitespaces() != ':') throw new JSONParseException("Invalid name/value pair", reader.currentIndex);
						obj.set(key, readGeneric(reader));
						hasComma = false;
						break;
					default:
						throw new JSONParseException("Unexpected char: "+c, reader.currentIndex);
				}
			}
			throw new JSONParseException("Missing end of object", reader.currentIndex);
		}
		
		private static JSONArray readArray(CharReader reader) {
			JSONArray arr = new JSONArray();
			boolean hasComma = false;
			while(reader.hasNext()) {
				char c = reader.nextIgnoreWhitespaces();
				switch(c) {
					case ',':
						if(hasComma) throw new JSONParseException("Double comma", reader.currentIndex);
						hasComma = true;
						break;
					case ']':
						return arr;
					default:
						reader.revert(1);
						if(!hasComma && !arr.isEmpty()) throw new JSONParseException("Missing comma separator", reader.currentIndex);
						Object o = readGeneric(reader);
						arr.add(o);
						hasComma = false;
						break;
				}
			}
			throw new JSONParseException("Missing end of array", reader.currentIndex);
		}
		
		private static Object readGeneric(CharReader reader) {
			char c = reader.nextIgnoreWhitespaces();
			if(Character.isDigit(c)) {
				reader.revert(1);
				return readNumber(reader);
			}
			switch(c) {
				case 'n':
					if(!reader.next(3).equals("ull")) throw new JSONParseException("Invalid property value", reader.currentIndex);
					return null;
				case 't':
					if(!reader.next(3).equals("rue")) throw new JSONParseException("Invalid property value", reader.currentIndex);
					return true;
				case 'f':
					if(!reader.next(4).equals("alse")) throw new JSONParseException("Invalid property value", reader.currentIndex);
					return false;
				case '"':
					return readString(reader);
				case '-':
					reader.revert(1);
					return readNumber(reader);
				case '{':
					return readObject(reader);
				case '[':
					return readArray(reader);
				default:
					throw new JSONParseException("Invalid generic value: "+c, reader.currentIndex);
			}
		}
		
		private static String readString(CharReader reader) {
			StringBuilder sb = new StringBuilder();
			while(reader.hasNext()) {
				char c = reader.next();
				switch(c) {
					case '"':
						return sb.toString();
					default:
						if(c == '\\') {
							sb.append(unescapeSpecial(reader));
						}else {
							sb.append(c);
						}
						break;
				}
			}
			throw new JSONParseException("Missing end of string", reader.currentIndex);
		}
		
		private static char unescapeSpecial(CharReader reader) {
			char c = reader.next(); //after the "\"
			switch(c) {
				case 'b':
					return '\b';
				case 'f':
					return '\f';
				case 'n':
					return '\n';
				case 'r':
					return '\r';
				case 't':
					return '\r';
				case 'u':
					String hex = reader.next(4);
					try {
						int ch = Integer.parseInt(hex, 16);
						return (char) ch;
					}catch(NumberFormatException e) {
						throw new JSONParseException("Invalid special char: "+hex, reader.currentIndex, e);
					}
				case '"':
				case '\\':
				case '/':
				default:
					return c;
			}
		}
		
		private static Number readNumber(CharReader reader) {
			StringBuilder numberString = new StringBuilder();
			boolean isDouble = false;
			while(reader.hasNext()) {
				char c = reader.nextIgnoreWhitespaces();
				if(c == 'c' || Character.isDigit(c)) {
					numberString.append(c);
				}else if(c == '.' || c == 'e' || c == 'E') {
					isDouble = true;
					numberString.append(c);
				}else {
					reader.revert(1);
					try {
						if(isDouble) {
							return Double.parseDouble(numberString.toString());
						}else {
							return Long.parseLong(numberString.toString());
						}
					}catch(NumberFormatException e) {
						throw new JSONParseException("Failed to parse number", reader.currentIndex, e);
					}
				}
			}
			throw new JSONParseException("Invalid number", reader.currentIndex);
		}
		
	}
	
	/**
	 * Provides methods for formatting objects into JSON strings
	 * @author MrLetsplay2003
	 */
	public static class JSONFormatter {

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
		
		private static CharSequence formatObject(JSONObject object, int indents, boolean fancy) {
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
		
		private static CharSequence formatArray(JSONArray array, int indents, boolean fancy) {
			StringBuilder builder = new StringBuilder("[").append(fancy ? "\n" : "").append(fancy ? space(indents + 1) : "");
			builder.append(array.stream()
				.map(en -> formatGeneric(en, indents + 1, fancy))
				.collect(Collectors.joining(new StringBuilder(",").append(fancy ? " " : ""))));
			return builder.append(fancy ? "\n" : "").append(fancy ? space(indents) : "").append("]");
		}
		
		private static String space(int len){
			return String.join("", Collections.nCopies(len * 4, " "));
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
	
	public static class JSONException extends RuntimeException {

		private static final long serialVersionUID = -3632660903729890508L;

		public JSONException(String message) {
			super(message);
		}
		
		public JSONException(String message, Throwable cause) {
			super(message, cause);
		}
		
	}
	
	public static class JSONParseException extends JSONException {
		
		private static final long serialVersionUID = 79760301767911820L;

		public JSONParseException(String message, int index) {
			super(message + (index != -1 ? " (at char "+index+")" : ""));
		}
		
		public JSONParseException(String message, int index, Throwable cause) {
			super(message + (index != -1 ? " (at char "+index+")" : ""), cause);
		}
		
	}
	
	public static class JSONFormatException extends JSONException {

		private static final long serialVersionUID = -7705694369818306658L;

		public JSONFormatException(String message) {
			super(message);
		}
		
	}
	
}
