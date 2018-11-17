package me.mrletsplay.mrcore.config.v2.builder;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import me.mrletsplay.mrcore.config.v2.ConfigSection;
import me.mrletsplay.mrcore.json.JSONArray;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.misc.TriConsumer;

public interface SubMappable<Self extends SubMappable<Self, T>, T> {

	public <Q extends MapperElement<Q, Self, T>> void addMapperElement(String key, MapperElement<Q, Self, T> element);

	public Map<String, MapperElement<?, Self, T>> getElements();
	
	@SuppressWarnings("unchecked")
	public default Self getSelf() {
		return (Self) this;
	}
	
	public default <Q extends StringMapper<Q, Self, T>> StringMapper<Q, Self, T> mapString(String name, Function<T, String> getter, BiConsumer<T, String> setter) {
		return mapString(name, (s, t) -> getter.apply(t), setter == null ? null : (s, t, v) -> setter.accept(t, v));
	}
	
	public default <Q extends StringMapper<Q, Self, T>> StringMapper<Q, Self, T> mapString(String name, BiFunction<ConfigSection, T, String> getter, TriConsumer<ConfigSection, T, String> setter) {
		StringMapper<Q, Self, T> sM = new StringMapper<>(getSelf(), getter, setter);
		addMapperElement(name, sM);
		return sM;	
	}
	
	public default <Q extends IntMapper<Q, Self, T>> IntMapper<Q, Self, T> mapInteger(String name, Function<T, Integer> getter, BiConsumer<T, Integer> setter) {
		return mapInteger(name, (s, t) -> getter.apply(t), setter == null ? null : (s, t, v) -> setter.accept(t, v));
	}
	
	public default <Q extends IntMapper<Q, Self, T>> IntMapper<Q, Self, T> mapInteger(String name, BiFunction<ConfigSection, T, Integer> getter, TriConsumer<ConfigSection, T, Integer> setter) {
		IntMapper<Q, Self, T> sM = new IntMapper<>(getSelf(), getter, setter);
		addMapperElement(name, sM);
		return sM;	
	}
	
	public default <Q extends DoubleMapper<Q, Self, T>> DoubleMapper<Q, Self, T> mapDouble(String name, Function<T, Double> getter, BiConsumer<T, Double> setter) {
		return mapDouble(name, (s, t) -> getter.apply(t), setter == null ? null : (s, t, v) -> setter.accept(t, v));
	}
	
	public default <Q extends DoubleMapper<Q, Self, T>> DoubleMapper<Q, Self, T> mapDouble(String name, BiFunction<ConfigSection, T, Double> getter, TriConsumer<ConfigSection, T, Double> setter) {
		DoubleMapper<Q, Self, T> sM = new DoubleMapper<>(getSelf(), getter, setter);
		addMapperElement(name, sM);
		return sM;	
	}
	
	public default <Q extends EnumMapper<Q, Self, T, E>, E extends Enum<E>> EnumMapper<Q, Self, T, E> mapEnum(Class<E> enumClass, String name, Function<T, E> getter, BiConsumer<T, E> setter) {
		return mapEnum(enumClass, name, (s, t) -> getter.apply(t), setter == null ? null : (s, t, v) -> setter.accept(t, v));
	}
	
	public default <Q extends EnumMapper<Q, Self, T, E>, E extends Enum<E>> EnumMapper<Q, Self, T, E> mapEnum(Class<E> enumClass, String name, BiFunction<ConfigSection, T, E> getter, TriConsumer<ConfigSection, T, E> setter) {
		EnumMapper<Q, Self, T, E> sM = new EnumMapper<>(getSelf(), enumClass, getter, setter);
		addMapperElement(name, sM);
		return sM;	
	}
	
	public default <Q extends JSONArrayMapper<Q, Self, T>> JSONArrayMapper<Q, Self, T> mapJSONArray(String name, Function<T, JSONArray> getter, BiConsumer<T, JSONArray> setter) {
		return mapJSONArray(name, (s, t) -> getter.apply(t), setter == null ? null : (s, t, v) -> setter.accept(t, v));
	}
	
	public default <Q extends JSONArrayMapper<Q, Self, T>> JSONArrayMapper<Q, Self, T> mapJSONArray(String name, BiFunction<ConfigSection, T, JSONArray> getter, TriConsumer<ConfigSection, T, JSONArray> setter) {
		JSONArrayMapper<Q, Self, T> sM = new JSONArrayMapper<>(getSelf(), getter, setter);
		addMapperElement(name, sM);
		return sM;	
	}
	
	public default <Q extends JSONObjectMapper<Q, Self, T>> JSONObjectMapper<Q, Self, T> mapJSONObject(String name, Function<T, JSONObject> getter, BiConsumer<T, JSONObject> setter) {
		return mapJSONObject(name, (s, t) -> getter.apply(t), setter == null ? null : (s, t, v) -> setter.accept(t, v));
	}
	
	public default <Q extends JSONObjectMapper<Q, Self, T>> JSONObjectMapper<Q, Self, T> mapJSONObject(String name, BiFunction<ConfigSection, T, JSONObject> getter, TriConsumer<ConfigSection, T, JSONObject> setter) {
		JSONObjectMapper<Q, Self, T> sM = new JSONObjectMapper<>(getSelf(), getter, setter);
		addMapperElement(name, sM);
		return sM;	
	}
	
	public default <Q extends SubMapper<Q, Self, T, S>, S> SubMapper<Q, Self, T, S> mapObject(String name, Function<JSONObject, S> constructor, Function<T, S> getter, BiConsumer<T, S> setter) {
		return mapObject(name, (s, j) -> constructor.apply(j), (s, t) -> getter.apply(t), setter == null ? null : (s, t, v) -> setter.accept(t, v));
	}
	
	public default <Q extends SubMapper<Q, Self, T, S>, S> SubMapper<Q, Self, T, S> mapObject(String name, BiFunction<ConfigSection, JSONObject, S> constructor, BiFunction<ConfigSection, T, S> getter, TriConsumer<ConfigSection, T, S> setter) {
		SubMapper<Q, Self, T, S> sM = new SubMapper<>(getSelf(), constructor, getter, setter);
		addMapperElement(name, sM);
		return sM;	
	}
	
}
