package me.mrletsplay.mrcore.config.impl;

import me.mrletsplay.mrcore.config.v2.ConfigException;

public class DefaultConfigParser {

	private CharReader r;
	
	public DefaultConfigParser(String[] raw) {
		r = new CharReader(raw);
	}
	
	public String readPropertyDescriptor() {
		String d = r.readUntil(':');
		if(d == null) throw new ConfigException("Failed to read property descriptor: EOF reached");
		r.skipWhitespaces();
		return d;
	}
	
	public Object readPropertyValue(int propertyIndents) {
		char c = r.next();
		Object value;
		if(Character.isDigit(c) || c == '-') {
			r.revert();
			value = readNumber();
		}else {
			switch(c) {
				case 'n':
					if(!r.next(3).equals("ull")) throw new ConfigException("Invalid property value", r.currentLine, r.currentIndex);
					value = null;
					break;
				case 't':
					if(!r.next(3).equals("rue")) throw new ConfigException("Invalid property value", r.currentLine, r.currentIndex);
					value = true;
					break;
				case 'f':
					if(!r.next(4).equals("alse")) throw new ConfigException("Invalid property value", r.currentLine, r.currentIndex);
					value = false;
					break;
				case '"':
					r.revert();
					value = readString();
					break;
				default:
					throw new ConfigException("Invalid property value: " + c, r.currentLine, r.currentIndex);
			}
		}
		if(r.hasNext()) {
			r.skipWhitespacesUntil('\n');
			r.skipUntilLastOf('\n');
			if(r.current() != '\n') throw new ConfigException("Unexpected character '" + r.current() + "'", r.currentLine, r.currentIndex);
		}
		return value;
	}
	
	public String readString() {
		if(r.next() != '"') throw new UnsupportedOperationException();
		StringBuilder sb = new StringBuilder();
		while(r.hasNext()) {
			char c = r.next();
			switch(c) {
				case '"':
					return sb.toString();
				default:
					if(c == '\\') {
						sb.append(readEscaped());
					}else {
						sb.append(c);
					}
					break;
			}
		}
		throw new ConfigException("Missing end of string", r.currentLine, r.currentIndex);
	}
	
	public Number readNumber() {
		StringBuilder numberString = new StringBuilder();
		boolean isDouble = false;
		while(r.hasNext()) {
			char c = r.next();
			if(c == '-' || Character.isDigit(c)) {
				numberString.append(c);
			}else if(c == '.' || c == 'e' || c == 'E') {
				isDouble = true;
				numberString.append(c);
			}else {
				r.revert();
				try {
					if(isDouble) {
						return Double.valueOf(numberString.toString());
					}else {
						return Long.valueOf(numberString.toString());
					}
				}catch(NumberFormatException e) {
					throw new ConfigException("Failed to parse number", e, r.currentLine, r.currentIndex);
				}
			}
		}
		throw new ConfigException("Invalid number", r.currentLine, r.currentIndex);
	}
	
	private char readEscaped() {
		if(!r.hasNext()) throw new ConfigException("Failed to read special character: EOF reached");
		char c = r.next(); //after the backslash
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
				String hex = r.next(4);
				if(hex == null) throw new ConfigException("Failed to read special character: EOF reached");
				try {
					int ch = Integer.parseInt(hex, 16);
					return (char) ch;
				}catch(NumberFormatException e) {
					throw new ConfigException("Invalid special char: \\u"+hex, e, r.currentLine, r.currentIndex);
				}
			case '"':
			case '\\':
			case '/':
			default:
				return c;
		}
	}
	
	public String debug() {
		return r.currentLine + "/" + r.currentIndex;
	}
	
	public boolean hasMore() {
		return r.hasNextIgnoreWhitespaces();
	}
	
	private static class CharReader {
		
		private int currentLine, currentIndex;
//		private String string;
		private String[] lines;
		
		public CharReader(String[] lines) {
			this.lines = lines;
			this.currentIndex = -1;
			this.currentLine = 0;
		}
		
		public char current() {
			return lines[currentLine].charAt(currentIndex);
		}
		
		public char next() {
			currentIndex ++;
			if(currentIndex >= lines[currentLine].length()) {
				if(currentLine == lines.length - 1) throw new RuntimeException("L: " + currentLine);
				currentLine++;
				currentIndex = 0;
			}
			char c = lines[currentLine].charAt(currentIndex);
			return c;
		}
		
		public String readUntil(char until) {
			StringBuilder b = new StringBuilder();
			while(hasNext()) {
				char n = next();
				if(n == until) return b.toString();
				b.append(n);
			}
			return null;
		}
		
		public String next(int amount) {
			char[] chs = new char[amount];
			for(int i = 0; i < amount; i++) {
				if(!hasNext()) return null;
				chs[i] = next();
			}
			return new String(chs);
		}
		
//		public String peek(int amount) {
//			char[] chs = new char[amount];
//			for(int i = 0; i < amount; i++) {
//				if(!hasNext()) {
//					revert(i);
//					return null;
//				}
//				chs[i] = next();
//			}
//			revert(amount);
//			return new String(chs);
//		}
		
		public String skipWhitespaces() {
			StringBuilder b = new StringBuilder();
			char c = next();
			while(Character.isWhitespace(c)) {
				b.append(c);
				c = next();
			}
			revert();
			return b.toString();
		}
		
		public String skipWhitespacesUntil(char until) {
			StringBuilder b = new StringBuilder();
			char c = next();
			while(c != until && Character.isWhitespace(c)) {
				b.append(c);
				c = next();
			}
//			revert();
			return b.toString();
		}
		
		public int skipUntilLastOf(char type) {
			int num = 0;
			if(current() != type) return 0;
			if(!hasNext()) return 0;
			char c = next();
			while(c == type) {
				num++;
				if(!hasNext()) return num;
				c = next();
			}
			revert();
			return num;
		}
		
//		public char nextIgnoreWhitespaces() {
//			char c = next();
//			while(Character.isWhitespace(c)) c = next();
//			return c;
//		}
		
//		public CharReader skip(int num) {
//			currentIndex += num;
//			if(currentIndex >= string.length()) throw new RuntimeException();
//			return this;
//		}
		
		public CharReader revert() {
			if(currentIndex == 0) {
				currentLine --;
				if(currentLine < 0) throw new RuntimeException();
				currentIndex = lines[currentLine].length() - 1;
				return this;
			}
			currentIndex --;
			if(currentIndex < 0) throw new RuntimeException();
			return this;
		}
		
//		public CharReader revert(int num) {
//			for(int i = 0; i < num; i++) revert();
//			return this;
//		}
		
		public boolean hasNext() {
			return currentIndex < lines[currentLine].length() - 1 || currentLine < lines.length - 1;
		}
		
		public boolean hasNextIgnoreWhitespaces() {
			if(!hasNext()) return false;
			for(int i = currentLine; i < lines.length; i++) {
				for(int j = (i == currentLine ? currentIndex + 1 : 0); j < lines[i].length(); j++) {
					if(j >= lines[i].length()) continue;
					if(!Character.isWhitespace(lines[i].charAt(j))) return true;
				}
			}
			return false;
		}
		
	}
	
}
