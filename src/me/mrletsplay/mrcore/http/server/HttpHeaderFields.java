package me.mrletsplay.mrcore.http.server;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public interface HttpHeaderFields {

	public void set(String name, List<String> values);
	
	public void add(String name, List<String> value);
	
	public List<String> get(String name);
	
	public Map<String, List<String>> getRawFields();

	public default void set(String name, String... values) {
		set(name, Arrays.asList(values));
	}

	public default void add(String name, String... values) {
		add(name, Arrays.asList(values));
	}

	public default String getFirst(String name) {
		List<String> vals = get(name);
		if(vals.isEmpty()) return null;
		return vals.get(0);
	}

	public default String getLast(String name) {
		List<String> vals = get(name);
		if(vals.isEmpty()) return null;
		return vals.get(vals.size() - 1);
	}
	
}
