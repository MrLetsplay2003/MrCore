package me.mrletsplay.mrcore.config.mapper.builder;

import java.util.function.BiFunction;

import me.mrletsplay.mrcore.config.ConfigSection;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.misc.TriConsumer;

public class IntMapper<Self extends IntMapper<Self, P, T>, P extends SubMappable<P, T>, T> extends BasicMapper<Self, P, T> {

	private BiFunction<ConfigSection, T, Integer> getter;
	private TriConsumer<ConfigSection, T, Integer> setter;
	
	public IntMapper(P parent, BiFunction<ConfigSection, T, Integer> getter, TriConsumer<ConfigSection, T, Integer> setter) {
		super(parent);
		this.getter = getter;
		this.setter = setter;
	}

	@Override
	public void applyMap(T instance, ConfigSection section, JSONObject json, String key) {
		int val = getter.apply(section, instance);
		json.set(key, val);
	}

	@Override
	public void applyConstruct(T instance, ConfigSection section, JSONObject json, String key) {
		if(setter == null) return;
		int val = json.getInt(key);
		setter.accept(section, instance, val);
	}

}
