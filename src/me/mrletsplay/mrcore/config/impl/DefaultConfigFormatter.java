package me.mrletsplay.mrcore.config.impl;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.config.v2.ConfigSection;
import me.mrletsplay.mrcore.misc.Complex;
import me.mrletsplay.mrcore.misc.NullableOptional;

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
		if(isEmptyProperty(property)) return;
		if(property instanceof ConfigSection) {
			ConfigSection s = (ConfigSection) property;
			Map<String, Object> props = s.toMap();
			if(!props.isEmpty()) {
				w.newLine();
				writeSubsection(indents + 1, props, s.commentsToMap());
			}
		}else if(property instanceof List) {
			if(!((List<?>) property).isEmpty()) {
				for(Object o : ((List<?>) property)) {
					w.newLine();
					w.write(space(indents) + "- ");
					writeListEntry(indents, o);
				}
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
	
	public void writeSubsection(int indents, Map<String, Object> properties, Map<String, String> comments) throws IOException {
		if(properties.isEmpty()) return;
		for(Map.Entry<String, Object> p : properties.entrySet()) {
			if(isEmptyProperty(p.getValue())) continue;
			String comment = comments.get(p.getKey()); // TODO: ClassCastException!!
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
//				writeSubsection(indents + 1, c.cast(p.getValue()).get(), c.cast(comments.get(p.getKey())).map(en -> en != null ? en : new HashMap<String, String>()).get());
				Map<String, String> sC = comments.entrySet().stream().filter(en -> en.getKey().startsWith(p.getKey() + ".")).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
				Map<String, Object> props = c.cast(p.getValue()).get();
				if(props != null && !props.isEmpty()) {
					writeSubsection(indents + 1, props, sC);
				}
			}else {
				writePropertyValue(indents + 1, p.getValue());
				w.newLine();
			}
		}
	}
	
	private boolean isEmptyProperty(Object o) {
		if(o == null) return false;
		Complex<Map<String, Object>> c = Complex.map(String.class, Object.class);
		Complex<List<Object>> c2 = Complex.list(Object.class);
		NullableOptional<Map<String, Object>> mC = c.cast(o);
		NullableOptional<List<Object>> lC = c2.cast(o);
		if(mC.isPresent()) {
			return mC.get().isEmpty();
		}else if(lC.isPresent()){
			return lC.get().isEmpty();
		}else {
			return false;
		}
	}
	
	public void writeListEntry(int indents, Object value) throws IOException {
		if(value == null) {
			w.write("null");
			return;
		}
		if(isEmptyProperty(value)) return;
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
