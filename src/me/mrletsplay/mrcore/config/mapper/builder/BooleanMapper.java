package me.mrletsplay.mrcore.config.mapper.builder;

import java.util.function.BiFunction;

import me.mrletsplay.mrcore.config.ConfigSection;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.misc.TriConsumer;

public class BooleanMapper<Self extends BooleanMapper<Self, P, T>, P extends SubMappable<P, T>, T> extends BasicMapper<Self, P, T> {

	private BiFunction<ConfigSection, T, Boolean> getter;
	private TriConsumer<ConfigSection, T, Boolean> setter;
	
	public BooleanMapper(P parent, BiFunction<ConfigSection, T, Boolean> getter, TriConsumer<ConfigSection, T, Boolean> setter) {
		super(parent);
		this.getter = getter;
		this.setter = setter;
	}

	@Override
	public void applyMap(T instance, ConfigSection section, JSONObject json, String key) {
		boolean val = getter.apply(section, instance);
		json.set(key, val);
	}

	@Override
	public void applyConstruct(T instance, ConfigSection section, JSONObject json, String key) {
		if(setter == null) return;
		boolean val = json.getBoolean(key);
		setter.accept(section, instance, val);
	}

}
