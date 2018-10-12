   package me.mrletsplay.mrcore.config.impl;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import me.mrletsplay.mrcore.config.v2.ConfigProperty;

public class DefaultConfigFormatter {

	private BufferedWriter w;
	
	public DefaultConfigFormatter(OutputStream out) {
		w = new BufferedWriter(new OutputStreamWriter(out));
	}
	
	public void writePropertyValue(ConfigProperty property) throws IOException {
		switch(property.getValueType()) {
			case NUMBER:
			case DECIMAL:
			case BOOLEAN:
				w.write(property.toString());
				break;
			case STRING:
			case CHARACTER:
				break;
			case LIST:
				break;
			case NULL:
				break;
			case SECTION:
				break;
			case UNDEFINED:
				break;
			default:
				break;
			
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
	
	private static CharSequence escapeString(String string) {
		StringBuilder escaped = new StringBuilder();
		string.chars().mapToObj(i -> (char)i).map(c -> escapeChar(c)).forEach(escaped::append);;
		return escaped;
	}

}
