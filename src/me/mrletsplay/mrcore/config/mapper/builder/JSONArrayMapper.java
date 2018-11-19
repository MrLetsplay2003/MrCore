package me.mrletsplay.mrcore.config.mapper.builder;

import java.util.function.BiFunction;

import me.mrletsplay.mrcore.config.ConfigSection;
import me.mrletsplay.mrcore.json.JSONArray;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.misc.TriConsumer;

public class JSONArrayMapper<Self extends JSONArrayMapper<Self, P, T>, P extends SubMappable<P, T>, T> extends BasicMapper<Self, P, T> {

	private BiFunction<ConfigSection, T, JSONArray> getter;
	private TriConsumer<ConfigSection, T, JSONArray> setter;
	
	public JSONArrayMapper(P parent, BiFunction<ConfigSection, T, JSONArray> getter, TriConsumer<ConfigSection, T, JSONArray> setter) {
		super(parent);
		this.getter = getter;
		this.setter = setter;
	}

	@Override
	public void applyMap(T instance, ConfigSection section, JSONObject json, String key) {
		JSONArray val = getter.apply(section, instance);
		json.set(key, val);
	}

	@Override
	public void applyConstruct(T instance, ConfigSection section, JSONObject json, String key) {
		if(setter == null) return;
		JSONArray val = json.getJSONArray(key);
		setter.accept(section, instance, val);
	}

}
