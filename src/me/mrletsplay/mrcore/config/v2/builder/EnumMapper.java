package me.mrletsplay.mrcore.config.v2.builder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.BiFunction;

import me.mrletsplay.mrcore.config.v2.ConfigSection;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.misc.FriendlyException;
import me.mrletsplay.mrcore.misc.TriConsumer;

public class EnumMapper<Self extends EnumMapper<Self, P, T, E>, P extends SubMappable<P, T>, T, E extends Enum<E>> extends BasicMapper<Self, P,T> {

	private Class<E> enumClass;
	private BiFunction<ConfigSection, T, E> getter;
	private TriConsumer<ConfigSection, T, E> setter;
	
	public EnumMapper(P parent, Class<E> enumClass, BiFunction<ConfigSection, T, E> getter, TriConsumer<ConfigSection, T, E> setter) {
		super(parent);
		this.enumClass = enumClass;
		this.getter = getter;
		this.setter = setter;
	}

	@Override
	public void applyMap(T instance, ConfigSection section, JSONObject json, String key) {
		E val = getter.apply(section, instance);
		json.set(key, val.name());
	}

	@Override
	public void applyConstruct(T instance, ConfigSection section, JSONObject json, String key) {
		if(setter == null) return;
		E val = enumValueOf(enumClass, json.getString(key));
		setter.accept(section, instance, val);
	}

	@SuppressWarnings("unchecked")
	private static <E extends Enum<E>> E enumValueOf(Class<E> enumClass, String name) {
		try {
			Method m = enumClass.getMethod("valueOf", String.class);
			return (E) m.invoke(null, name);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new FriendlyException(e);
		}
	}
	
}
