package me.mrletsplay.mrcore.misc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;

public class JSON {
	
	public static class JSONObject {
		
		private HashMap<String, Object> properties;
		
		public JSONObject() {
			properties = new HashMap<>();
		}
		
		public JSONObject(String source) {
			properties = ((JSONObject) JSONParser.parse(source)).properties;
		}
		
		public void set(String key, Object value) {
			properties.put(key, value);
		}
		
		public Object get(String key) {
			if(!properties.containsKey(key)) throw new JSONException("Object doesn't have the property \""+key+"\"");
			return properties.get(key);
		}
		
		public String getString(String key) {
			return (String) get(key);
		}
		
		public Boolean getBoolean(String key) {
			return (Boolean) get(key);
		}
		
		public Number getNumber(String key) {
			return (Number) get(key);
		}
		
		public int getInt(String key) {
			return getNumber(key).intValue();
		}
		
		public long getLong(String key) {
			return getNumber(key).longValue();
		}
		
		public double getDouble(String key) {
			return getNumber(key).doubleValue();
		}
		
		public JSONObject getJSONObject(String key) {
			return (JSONObject) get(key);
		}
		
		public JSONArray getJSONArray(String key) {
			return (JSONArray) get(key);
		}
		
		@Override
		public String toString() {
			return JSONFormatter.formatObject(this).toString();
		}
		
	}
	
	public static class JSONArray extends ArrayList<Object> {

		private static final long serialVersionUID = 7419047721431768026L;

		public JSONArray() {
			super();
		}

		public JSONArray(Collection<Object> collection) {
			super(collection);
		}
		
		public JSONArray(String source) {
			super((JSONArray) JSONParser.parse(source));
		}
		
		public String getString(int index) {
			return (String) get(index);
		}
		
		public Boolean getBoolean(int index) {
			return (Boolean) get(index);
		}
		
		public Number getNumber(int index) {
			return (Number) get(index);
		}
		
		public int getInt(int index) {
			return getNumber(index).intValue();
		}
		
		public long getLong(int index) {
			return getNumber(index).longValue();
		}
		
		public double getDouble(int index) {
			return getNumber(index).doubleValue();
		}
		
		public JSONObject getJSONObject(int index) {
			return (JSONObject) get(index);
		}
		
		public JSONArray getJSONArray(int index) {
			return (JSONArray) get(index);
		}
		
		@Override
		public String toString() {
			return JSONFormatter.formatArray(this).toString();
		}
		
	}
	
	public static class CharReader {
		
		private int currentIndex;
		private String string;
		
		public CharReader(String string) {
			this.string = string;
			this.currentIndex = 0;
		}
		
		public char current() {
			return string.charAt(currentIndex);
		}
		
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
		
		public CharReader skip(int num) {
			currentIndex += num;
			if(currentIndex >= string.length()) throw new RuntimeException();
			return this;
		}
		
		public CharReader revert(int num) {
			currentIndex -= num;
			if(currentIndex < 0) throw new RuntimeException();
			return this;
		}
		
		public boolean hasNext() {
			return currentIndex < string.length();
		}
		
	}
	
	public static class JSONParser {
		
		public static Object parse(String source) {
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
						if(!hasComma && !obj.properties.isEmpty()) throw new JSONParseException("Missing comma separator", reader.currentIndex);
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
	
	public static class JSONFormatter {
		
		public static String formatObject(Object object) {
			return formatGeneric(object).toString();
		}
		
		private static CharSequence formatObject(JSONObject object) {
			StringBuilder builder = new StringBuilder("{");
			builder.append(object.properties.entrySet().stream()
				.map(en -> new StringBuilder()
						.append("\"")
						.append(escapeString(en.getKey()))
						.append("\"")
						.append(": ")
						.append(formatGeneric(en.getValue())))
				.collect(Collectors.joining(", ")));
			return builder.append("}");
		}
		
		private static CharSequence formatArray(JSONArray array) {
			StringBuilder builder = new StringBuilder("[");
			builder.append(array.stream()
				.map(en -> formatGeneric(en))
				.collect(Collectors.joining(", ")));
			return builder.append("]");
		}
		
		private static CharSequence formatGeneric(Object object) {
			if(object == null) return "null";
			if(object instanceof Number || object instanceof Boolean) return object.toString();
			if(object instanceof String) return new StringBuilder("\"").append(escapeString((String)object)).append("\"");
			if(object instanceof JSONObject) return formatObject((JSONObject) object);
			if(object instanceof JSONArray) return formatArray((JSONArray) object);
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
