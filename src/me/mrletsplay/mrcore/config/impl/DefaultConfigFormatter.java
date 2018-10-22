package me.mrletsplay.mrcore.config.impl;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.mrletsplay.mrcore.config.v2.ConfigSection;
import me.mrletsplay.mrcore.misc.Complex;

class DefaultConfigFormatter {

	private BufferedWriter w;
	
	public DefaultConfigFormatter(BufferedWriter out) {
		this.w = out;
	}
	
	public void writeHeader(String header) throws IOException {
		String[] sHeader = header.split("\n");
		for(String h : sHeader) {
			w.write("##" + h);
			w.newLine();
		}
	}
	
	public void writeConfigVersionDescriptor(String version) throws IOException {
		w.write("### CustomConfig version: " + version);
		w.newLine();
	}
	
	public void writePropertyValue(int indents, Object property) throws IOException {
		if(property == null) {
			w.write("null");
			return;
		}
		if(property instanceof ConfigSection) {
			w.newLine();
			ConfigSection s = (ConfigSection) property;
			writeSubsection(indents + 1,  s.toMap(), s.commentsToMap());
		}else if(property instanceof List) {
			for(Object o : ((List<?>) property)) {
				w.newLine();
				w.write(space(indents) + "- ");
				writeListEntry(indents, o);
			}
		}else if(property instanceof String) {
			w.write("\"");
			w.write(escapeString((String) property).toString());
			w.write("\"");
		}else if(property instanceof Character) {
			w.write("\'");
			w.write(escapeChar((char) property));
			w.write("\'");
		}else {
			w.write(property.toString());
		}
	}
	
	public void writeSubsection(int indents, Map<String, Object> properties, Map<String, Object> comments) throws IOException {
		for(Map.Entry<String, Object> p : properties.entrySet()) {
			String comment = (String) comments.get(p.getKey());
			if(comment != null) {
				String[] sComment = comment.split("\n");
				for(String c : sComment) {
					w.write(space(indents) + "#" + c);
					w.newLine();
				}
			}
			w.write(space(indents) + p.getKey());
			w.write(": ");
			Complex<Map<String, Object>> c = Complex.map(String.class, Object.class);
			if(c.isInstance(p.getValue())) {
				w.newLine();
				writeSubsection(indents + 1, c.cast(p.getValue()).get(), c.cast(comments.get(p.getKey())).map(en -> en != null ? en : new HashMap<String, Object>()).get());
			}else {
				writePropertyValue(indents + 1, p.getValue());
				w.newLine();
			}
		}
	}
	
	public void writeListEntry(int indents, Object value) throws IOException {
		if(value == null) {
			w.write("null");
			return;
		}
		if(value instanceof ConfigSection) {
			w.write("{");
			writePropertyValue(indents + 1, value);
			w.write(space(indents) + "}");
		}else if(value instanceof List) {
			w.write("{");
			writePropertyValue(indents + 1, value);
			w.newLine();
			w.write(space(indents) + "}");
		}else {
			writePropertyValue(indents, value);
		}
	}
	
	public void writeString(String str) throws IOException {
		w.write("\"" + escapeString(str) + "\"");
	}
	
	private static String escapeChar(char c) {
			switch(c) {
			case '"':
				return "\\\"";
			case '\\':
				return "\\\\";
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
	
	private static CharSequence escapeString(String string) {
		StringBuilder escaped = new StringBuilder();
		string.chars().mapToObj(i -> (char)i).map(c -> escapeChar(c)).forEach(escaped::append);;
		return escaped;
	}
	
	public static String space(int length) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < length; i++) sb.append("  ");
		return sb.toString();
	}

}
