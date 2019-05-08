package me.mrletsplay.mrcore.json;

import me.mrletsplay.mrcore.misc.FriendlyException;

/**
 * Provides methods for parsing JSON strings into objects
 * @author MrLetsplay2003
 */
public class JSONParser {
	
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
				return '\t';
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
			if(c == '-' || Character.isDigit(c)) {
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
	
	private static class CharReader {
		
		private int currentIndex;
		private String string;
		
		public CharReader(String string) {
			this.string = string;
			this.currentIndex = 0;
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
		
		public CharReader revert(int num) {
			currentIndex -= num;
			if(currentIndex < 0) throw new FriendlyException("Illegal reader move");
			return this;
		}
		
		public boolean hasNext() {
			return currentIndex < string.length();
		}
		
	}
	
}
