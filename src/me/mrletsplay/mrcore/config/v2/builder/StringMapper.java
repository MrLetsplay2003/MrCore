package me.mrletsplay.mrcore.config.v2.builder;

import java.util.function.BiFunction;

import me.mrletsplay.mrcore.config.v2.ConfigSection;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.misc.TriConsumer;

public class StringMapper<Self extends StringMapper<Self, P, T>, P extends SubMappable<P, T>, T> extends BasicMapper<Self, P, T> {

	private BiFunction<ConfigSection, T, String> getter;
	private TriConsumer<ConfigSection, T, String> setter;
	
	public StringMapper(P parent, BiFunction<ConfigSection, T, String> getter, TriConsumer<ConfigSection, T, String> setter) {
		super(parent);
		this.getter = getter;
		this.setter = setter;
	}

	@Override
	public void applyMap(T instance, ConfigSection section, JSONObject json, String key) {
		String val = getter.apply(section, instance);
		json.set(key, val);
	}

	@Override
	public void applyConstruct(T instance, ConfigSection section, JSONObject json, String key) {
		if(setter == null) return;
		String val = json.getString(key);
		setter.accept(section, instance, val);
	}

}
