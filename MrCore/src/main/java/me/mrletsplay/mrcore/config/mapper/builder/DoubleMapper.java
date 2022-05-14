package me.mrletsplay.mrcore.config.mapper.builder;

import java.util.function.BiFunction;

import me.mrletsplay.mrcore.config.ConfigSection;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.misc.TriConsumer;

public class DoubleMapper<S extends DoubleMapper<S, P, T>, P extends SubMappable<P, T>, T> extends BasicMapper<S, P, T> {

	private BiFunction<ConfigSection, T, Double> getter;
	private TriConsumer<ConfigSection, T, Double> setter;
	
	public DoubleMapper(P parent, BiFunction<ConfigSection, T, Double> getter, TriConsumer<ConfigSection, T, Double> setter) {
		super(parent);
		this.getter = getter;
		this.setter = setter;
	}

	@Override
	public void applyMap(T instance, ConfigSection section, JSONObject json, String key) {
		double val = getter.apply(section, instance);
		json.set(key, val);
	}

	@Override
	public void applyConstruct(T instance, ConfigSection section, JSONObject json, String key) {
		if(setter == null) return;
		double val = json.getDouble(key);
		setter.accept(section, instance, val);
	}

}
