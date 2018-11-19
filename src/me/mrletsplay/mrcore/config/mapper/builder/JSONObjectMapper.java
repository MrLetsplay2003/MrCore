package me.mrletsplay.mrcore.config.mapper.builder;

import java.util.function.BiFunction;

import me.mrletsplay.mrcore.config.ConfigSection;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.misc.TriConsumer;

public class JSONObjectMapper<Self extends JSONObjectMapper<Self, P, T>, P extends SubMappable<P, T>, T> extends BasicMapper<Self, P, T> {

	private BiFunction<ConfigSection, T, JSONObject> getter;
	private TriConsumer<ConfigSection, T, JSONObject> setter;
	
	public JSONObjectMapper(P parent, BiFunction<ConfigSection, T, JSONObject> getter, TriConsumer<ConfigSection, T, JSONObject> setter) {
		super(parent);
		this.getter = getter;
		this.setter = setter;
	}

	@Override
	public void applyMap(T instance, ConfigSection section, JSONObject json, String key) {
		JSONObject val = getter.apply(section, instance);
		json.set(key, val);
	}

	@Override
	public void applyConstruct(T instance, ConfigSection section, JSONObject json, String key) {
		if(setter == null) return;
		JSONObject val = json.getJSONObject(key);
		setter.accept(section, instance, val);
	}

}
