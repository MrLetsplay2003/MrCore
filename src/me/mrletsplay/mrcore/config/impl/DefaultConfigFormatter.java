package me.mrletsplay.mrcore.config.impl;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import me.mrletsplay.mrcore.config.v2.ConfigProperty;

public class DefaultConfigFormatter {

	private BufferedWriter w;
	
	public DefaultConfigFormatter(OutputStream out) {
		w = new BufferedWriter(new OutputStreamWriter(out));
	}
	
	public void writeProperty(ConfigProperty property) {
		switch(property.getValueType()) {
			case BOOLEAN:
				break;
			case CHARACTER:
				break;
			case DECIMAL:
				break;
			case LIST:
				break;
			case NULL:
				break;
			case NUMBER:
				break;
			case SECTION:
				break;
			case STRING:
				break;
			case UNDEFINED:
				break;
			default:
				break;
			
		}
	}
	
}
