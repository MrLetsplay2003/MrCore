package me.mrletsplay.mrcore.config.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.config.ConfigException;

class DefaultConfigParser {

	public CharReader r;
	
	public DefaultConfigParser(String[] raw) {
		r = new CharReader(raw);
	}
	
	public String readHeader() {
		List<String> rHeader = new ArrayList<>();
		while(true) {
			int sk = r.skipWhitespacesUntil('\n').length();
			if(!r.hasNext()) throw new ConfigException("Failed to read header: EOF reached");
			if(r.next() == '\n') throw new ConfigException("Invalid header", r.currentLine, r.currentIndex);
			r.revert();
			if(r.next(2).equals("##")) {
				rHeader.add(r.readUntil('\n'));
				if(!r.hasNext()) break;
			}else {
				r.revert(sk + 2);
				break;
			}
		}
		return rHeader.stream().collect(Collectors.joining("\n"));
	}
	
	public String readVersionDescriptor() {
		r.skipWhitespacesUntil('\n');
		if(!r.hasNext()) throw new ConfigException("Failed to read property descriptor: EOF reached");
		if(r.next() == '\n') throw new ConfigException("Invalid version descriptor", r.currentLine, r.currentIndex);
		r.revert();
		if(!"###".equals(r.next(3))) throw new ConfigException("Version descriptor expected", r.currentLine, r.currentIndex);
		r.skipWhitespacesUntil('\n');
		if(!r.hasNext()) throw new ConfigException("Failed to read property descriptor: EOF reached");
		if(r.next() == '\n') throw new ConfigException("Invalid version descriptor", r.currentLine, r.currentIndex);
		r.revert();
		String vDesc = r.readUntil('\n');
		String vS = "CustomConfig version: ";
		if(!vDesc.startsWith(vS)) throw new ConfigException("Invalid version descriptor", r.currentLine, r.currentIndex);
		return vDesc.substring(vS.length()).trim();
	}
	
	public Object readCommentOrPropertyDescriptor(int indents) {
		int sk = r.skipWhitespacesUntil('\n').length();
		if(!r.hasNext()) throw new ConfigException("Failed to read property descriptor: EOF reached");
		if(r.next() == '\n') throw new ConfigException("Invalid comment/property descriptor", r.currentLine, r.currentIndex);
		r.revert();
		if(r.next() == '#') {
			return r.readUntil('\n');
		}else {
			r.revert(sk + 1);
			return readPropertyDescriptor(indents);
		}
	}
	
	public PropertyDescriptor readPropertyDescriptor(int indents) {
		int rIndents = r.skipWhitespacesUntil('\n').length();
		if(!r.hasNext()) throw new ConfigException("Failed to read property descriptor: EOF reached");
		if(r.next() == '\n') throw new ConfigException("Invalid property descriptor", r.currentLine, r.currentIndex);
		r.revert();
		if(rIndents % 2 != 0) throw new ConfigException("Invalid property descriptor: indents % 2 != 0", r.currentLine, r.currentIndex);
		Marker s = r.createMarker();
		if(rIndents / 2 != indents) {
			r.next(); // FIXME
			return new PropertyDescriptor(rIndents, "", s);
		}
		String d = r.readUntil(':');
		if(d == null) throw new ConfigException("Failed to read property descriptor: EOF reached");
		if(d.contains("\n")) throw new ConfigException("Invalid property descriptor", r.currentLine, r.currentIndex);
		return new PropertyDescriptor(rIndents, d, s);
	}
	
	public Object readPropertyValue(int propertyIndents) {
		r.skipWhitespacesUntil('\n');
		if(!r.hasNext()) throw new ConfigException("Empty property value", r.currentLine, r.currentIndex);
		char c = r.next();
		Object value;
		if(Character.isDigit(c) || c == '-') {
			r.revert();
			value = readNumber();
		}else {
			switch(c) {
				case 'n':
					if(!r.next(3).equals("ull")) throw new ConfigException("Invalid property value", r.currentLine, r.currentIndex - 3);
					value = null;
					break;
				case 't':
					if(!r.next(3).equals("rue")) throw new ConfigException("Invalid property value", r.currentLine, r.currentIndex - 3);
					value = true;
					break;
				case 'f':
					if(!r.next(4).equals("alse")) throw new ConfigException("Invalid property value", r.currentLine, r.currentIndex - 4);
					value = false;
					break;
				case '"':
					r.revert();
					value = readString();
					break;
				case '\'':
					r.revert();
					value = readCharacter();
					break;
				case '\n':
					return readListOrSubsection(r.createMarker(), propertyIndents + 1);
				default:
					throw new ConfigException("Invalid property value: " + c, r.currentLine, r.currentIndex);
			}
		}
		if(r.hasNext()) {
			r.skipWhitespacesUntil('\n');
			r.next();
			r.skipUntilLastOf('\n');
			if(r.current() != '\n') throw new ConfigException("Unexpected character '" + r.current() + "'", r.currentLine, r.currentIndex);
		}
		return value;
	}
	
	public Object readListOrSubsection(Marker start, int indents) {
		int d = r.skipWhitespaces().length();
		switch(r.next()) {
			case '-':
				r.revert(d + 1);
				return readList(start, indents);
			default:
				r.revert(d + 1);
				return readSubsection(start, indents);
		}
	}
	
	public ConfigSectionDescriptor readSubsection(Marker start, int indents) {
		ConfigSectionDescriptor section = new ConfigSectionDescriptor();
		List<String> commentBuffer = new ArrayList<>();
		while(r.hasNext()) {
			Object cop = readCommentOrPropertyDescriptor(indents);
			if(cop instanceof PropertyDescriptor) {
				PropertyDescriptor s = (PropertyDescriptor) cop;
				if(s.getIndents() > indents) throw new ConfigException("Invalid amount of indents", s.start.line, s.start.index + 1);
				if(s.getIndents() < indents) {
					if(section.properties.isEmpty()) throw new ConfigException("Empty subsection", start.line, start.index);
					r.revert(s.getRawIndents() + s.getKey().length() + 1);
					return section;
				}
				if(!commentBuffer.isEmpty()) {
					section.comments.put(s.getKey(), commentBuffer.stream().collect(Collectors.joining("\n")));
					commentBuffer = new ArrayList<>();
				}
				section.properties.put(s.getKey(), readPropertyValue(indents));
			}else if(cop instanceof String) { // Comment
				commentBuffer.add((String) cop);
			}
		}
		return section;
	}
	
	public PropertyDescriptor readListEntryDescriptor(int indents) {
		int ind = r.skipWhitespacesUntil('\n').length();
		if(!r.hasNext()) throw new ConfigException("Failed to read property descriptor: EOF reached");
		if(r.next() == '\n') throw new ConfigException("Invalid list entry descriptor", r.currentIndex, r.currentLine);
		r.revert();
		if(ind % 2 != 0) throw new ConfigException("Invalid list entry descriptor: indents % 2 != 0", r.currentLine, r.currentIndex);
		Marker s = r.createMarker();
		if(ind / 2 != indents) return new PropertyDescriptor(ind, "", s);
		if(r.next() != '-') throw new ConfigException("Invalid list entry, unexpected character '" + r.current() + "'", r.currentLine, r.currentIndex);
		return new PropertyDescriptor(ind, "-", s);
	}
	
	public PropertyDescriptor readListClosingBracketDescriptor(int indents) {
		int ind = r.skipWhitespacesUntil('\n').length();
		if(!r.hasNext()) throw new ConfigException("Failed to read closing bracket: EOF reached");
		if(r.next() == '\n') throw new ConfigException("Missing closing bracket", r.currentIndex, r.currentLine);
		r.revert();
		if(ind % 2 != 0) throw new ConfigException("Invalid closing bracket: indents % 2 != 0", r.currentLine, r.currentIndex);
		Marker s = r.createMarker();
		if(ind / 2 != indents) return new PropertyDescriptor(ind, "", s);
		if(r.next() != '}') throw new ConfigException("Missing closing bracket, unexpected character '" + r.current() + "'", r.currentLine, r.currentIndex);
		r.skipWhitespacesUntil('\n');
		if(r.next() != '\n') throw new ConfigException("Unexpected character '" + r.current() + "'", r.currentLine, r.currentIndex);
		return new PropertyDescriptor(ind, "}", s);
	}
	
	public List<Object> readList(Marker start, int indents) {
		List<Object> list = new ArrayList<>();
		while(r.hasNext()) {
			PropertyDescriptor s = readListEntryDescriptor(indents);
			if(s.getIndents() > indents) throw new ConfigException("Invalid amount of indents", r.currentLine, r.currentIndex);
			if(s.getIndents() < indents) {
				if(list.isEmpty()) throw new ConfigException("Empty list", start.line, start.index);
				r.revert(s.getRawIndents() + s.getKey().length());
				return list;
			}
			list.add(readListEntryValue(indents));
		}
		return list;
	}
	
	public Object readListEntryValue(int propertyIndents) {
		r.skipWhitespacesUntil('\n');
		char c = r.next();
		if(!r.hasNext()) throw new ConfigException("Empty list entry value", r.currentLine, r.currentIndex);
		Object value;
		if(Character.isDigit(c) || c == '-') {
			r.revert();
			value = readNumber();
		}else {
			switch(c) {
				case 'n':
					if(!r.next(3).equals("ull")) throw new ConfigException("Invalid list entry value", r.currentLine, r.currentIndex - 3);
					value = null;
					break;
				case 't':
					if(!r.next(3).equals("rue")) throw new ConfigException("Invalid list entry value", r.currentLine, r.currentIndex - 3);
					value = true;
					break;
				case 'f':
					if(!r.next(4).equals("alse")) throw new ConfigException("Invalid list entry value", r.currentLine, r.currentIndex - 4);
					value = false;
					break;
				case '"':
					r.revert();
					value = readString();
					break;
				case '\'':
					r.revert();
					value = readCharacter();
					break;
				case '{':
					if(r.next() != '\n') throw new ConfigException("Invalid list entry value, unexpected character '" + r.current() + "'", r.currentLine, r.currentIndex);
					Object los = readListOrSubsection(r.createMarker(), propertyIndents + 1);
					readListClosingBracketDescriptor(propertyIndents);
					return los;
				default:
					throw new ConfigException("Invalid property value: " + c, r.currentLine, r.currentIndex);
			}
		}
		if(r.hasNext()) {
			r.skipWhitespacesUntil('\n');
			r.next();
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
				case '\\':
					sb.append(readEscaped());
					break;
				default:
					sb.append(c);
					break;
			}
		}
		throw new ConfigException("Missing end of string", r.currentLine, r.currentIndex);
	}
	
	public char readCharacter() {
		if(r.next() != '\'') throw new UnsupportedOperationException();
		char c = r.next();
		switch(c) {
			case '\\':
				c = readEscaped();
		}
		if(r.next() != '\'') throw new ConfigException("Missing end of string", r.currentLine, r.currentIndex);
		return c;
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
		if(!r.hasNext()) throw new ConfigException("Failed to read special character: EOF reached", r.currentLine, r.currentIndex);
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
				if(hex == null) throw new ConfigException("Failed to read special character: EOF reached", r.currentLine, r.currentIndex);
				try {
					int ch = Integer.parseInt(hex, 16);
					return (char) ch;
				}catch(NumberFormatException e) {
					throw new ConfigException("Invalid special char: \\u"+hex, e, r.currentLine, r.currentIndex - 5);
				}
			case '"':
			case '\\':
			default:
				return c;
		}
	}
	
	public boolean hasMore() {
		return r.hasNextIgnoreWhitespaces();
	}
	
	public static class PropertyDescriptor {
		
		private int indents;
		private String key;
		private Marker start;
		
		public PropertyDescriptor(int indents, String key, Marker start) {
			this.indents = indents;
			this.key = key;
			this.start = start;
		}
		
		public int getRawIndents() {
			return indents;
		}
		
		public int getIndents() {
			return indents / 2;
		}
		
		public String getKey() {
			return key;
		}
		
		public Marker getStart() {
			return start;
		}
		
	}
	
	public static class ConfigSectionDescriptor {
		
		private Map<String, Object> properties;
		private Map<String, String> comments;
		
		public ConfigSectionDescriptor() {
			this.properties = new LinkedHashMap<>();
			this.comments = new LinkedHashMap<>();
		}
		
		public Map<String, Object> getProperties() {
			return properties;
		}
		
		public Map<String, String> getComments() {
			return comments;
		}
		
		public Map<String, ConfigSectionDescriptor> getSubSections() {
			return properties.entrySet().stream()
					.filter(en -> en.getValue() instanceof ConfigSectionDescriptor)
					.collect(Collectors.toMap(en -> en.getKey(), en -> (ConfigSectionDescriptor) en.getValue()));
		}
		
		public Map<String, Object> toPropertyMap() {
			Map<String, Object> map = new LinkedHashMap<>();
			for(Map.Entry<String, Object> en : properties.entrySet()) {
				if(en.getValue() instanceof ConfigSectionDescriptor) {
					map.put(en.getKey(), ((ConfigSectionDescriptor) en.getValue()).toPropertyMap());
					continue;
				}
				map.put(en.getKey(), en.getValue());
			}
			return map;
		}
		
		public Map<String, String> toCommentMap() {
			Map<String, String> comments2 = new LinkedHashMap<>(comments);
			getSubSections().forEach((k, v) -> {
//				comments2.put(k, v.toCommentMap());
				v.toCommentMap().forEach((ck, cv) -> {
					comments2.put(k + ck, cv);
				});
			});
			return comments2;
		}
		
		@Override
		public String toString() {
			return "[SEC] " + properties;
		}
		
	}
	
	public static class Marker {
		
		private int line, index;
		
		public Marker(int line, int index) {
			this.line = line;
			this.index = index;
		}
		
		public int getLine() {
			return line;
		}
		
		public int getIndex() {
			return index;
		}
		
	}
	
	public static class CharReader {
		
		public int currentLine, currentIndex;
		public String[] lines;
		
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
				if(currentLine == lines.length - 1) throw new UnsupportedOperationException("Reached EOF");
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
			revert();
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
		
		public CharReader revert() {
			if(!hasPrevious()) throw new UnsupportedOperationException("Already at beginning");
			if(currentIndex == 0) {
				if(currentLine == 0) {
					currentIndex = -1;
					return this;
				}
				currentLine --;
				currentIndex = lines[currentLine].length() - 1;
				return this;
			}
			currentIndex --;
			if(currentIndex < 0) throw new UnsupportedOperationException("Already at beginning");
			return this;
		}
		
		public boolean hasPrevious() {
			return currentIndex >= 0 || currentLine >= 0;
		}
		
		public CharReader revert(int num) {
			for(int i = 0; i < num; i++) revert();
			return this;
		}
		
		public boolean hasNext() {
			return lines.length > 0 && (currentIndex < lines[currentLine].length() - 1 || currentLine < lines.length - 1);
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
		
		public Marker createMarker() {
			return new Marker(currentLine, currentIndex);
		}
		
	}
	
}