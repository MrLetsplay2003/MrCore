package me.mrletsplay.mrcore.config.mapper.builder;

import java.util.function.BiFunction;

import me.mrletsplay.mrcore.config.ConfigSection;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.misc.TriConsumer;

public class LongMapper<S extends LongMapper<S, P, T>, P extends SubMappable<P, T>, T> extends BasicMapper<S, P, T> {

	private BiFunction<ConfigSection, T, Long> getter;
	private TriConsumer<ConfigSection, T, Long> setter;
	
	public LongMapper(P parent, BiFunction<ConfigSection, T, Long> getter, TriConsumer<ConfigSection, T, Long> setter) {
		super(parent);
		this.getter = getter;
		this.setter = setter;
	}

	@Override
	public void applyMap(T instance, ConfigSection section, JSONObject json, String key) {
		long val = getter.apply(section, instance);
		json.set(key, val);
	}

	@Override
	public void applyConstruct(T instance, ConfigSection section, JSONObject json, String key) {
		if(setter == null) return;
		long val = json.getLong(key);
		setter.accept(section, instance, val);
	}

}
