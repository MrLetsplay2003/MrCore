package me.mrletsplay.mrcore.http.server.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import me.mrletsplay.mrcore.http.server.HttpHeaderFields;

public class DefaultHttpHeaderFields implements HttpHeaderFields {

	private Map<String, List<String>> fields;
	
	public DefaultHttpHeaderFields() {
		this.fields = new LinkedHashMap<>();
	}
	
	@Override
	public void set(String name, List<String> values) {
		fields.put(name, new ArrayList<>(values) );
	}

	@Override
	public void add(String name, List<String> value) {
		List<String> vals = fields.getOrDefault(name, new ArrayList<>());
		vals.addAll(value);
		fields.put(name, vals);
	}

	@Override
	public List<String> get(String name) {
		return fields.getOrDefault(name, new ArrayList<>());
	}
	
	@Override
	public Map<String, List<String>> getRawFields() {
		return fields;
	}

}
