package me.mrletsplay.mrcore.config.mapper.builder;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import me.mrletsplay.mrcore.config.ConfigSection;
import me.mrletsplay.mrcore.json.JSONArray;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.misc.TriConsumer;

public interface SubMappable<S extends SubMappable<S, T>, T> {

	public <Q extends MapperElement<Q, S, T>> void addMapperElement(String key, MapperElement<Q, S, T> element);

	public Map<String, MapperElement<?, S, T>> getElements();
	
	@SuppressWarnings("unchecked")
	public default S getSelf() {
		return (S) this;
	}
	
	public default <Q extends StringMapper<Q, S, T>> StringMapper<Q, S, T> mapString(String name, Function<T, String> getter, BiConsumer<T, String> setter) {
		return mapString(name, (s, t) -> getter.apply(t), setter == null ? null : (s, t, v) -> setter.accept(t, v));
	}
	
	public default <Q extends StringMapper<Q, S, T>> StringMapper<Q, S, T> mapString(String name, BiFunction<ConfigSection, T, String> getter, TriConsumer<ConfigSection, T, String> setter) {
		StringMapper<Q, S, T> sM = new StringMapper<>(getSelf(), getter, setter);
		addMapperElement(name, sM);
		return sM;	
	}
	
	public default <Q extends IntMapper<Q, S, T>> IntMapper<Q, S, T> mapInteger(String name, Function<T, Integer> getter, BiConsumer<T, Integer> setter) {
		return mapInteger(name, (s, t) -> getter.apply(t), setter == null ? null : (s, t, v) -> setter.accept(t, v));
	}
	
	public default <Q extends IntMapper<Q, S, T>> IntMapper<Q, S, T> mapInteger(String name, BiFunction<ConfigSection, T, Integer> getter, TriConsumer<ConfigSection, T, Integer> setter) {
		IntMapper<Q, S, T> sM = new IntMapper<>(getSelf(), getter, setter);
		addMapperElement(name, sM);
		return sM;	
	}
	
	public default <Q extends LongMapper<Q, S, T>> LongMapper<Q, S, T> mapLong(String name, Function<T, Long> getter, BiConsumer<T, Long> setter) {
		return mapLong(name, (s, t) -> getter.apply(t), setter == null ? null : (s, t, v) -> setter.accept(t, v));
	}
	
	public default <Q extends LongMapper<Q, S, T>> LongMapper<Q, S, T> mapLong(String name, BiFunction<ConfigSection, T, Long> getter, TriConsumer<ConfigSection, T, Long> setter) {
		LongMapper<Q, S, T> sM = new LongMapper<>(getSelf(), getter, setter);
		addMapperElement(name, sM);
		return sM;	
	}
	
	public default <Q extends DoubleMapper<Q, S, T>> DoubleMapper<Q, S, T> mapDouble(String name, Function<T, Double> getter, BiConsumer<T, Double> setter) {
		return mapDouble(name, (s, t) -> getter.apply(t), setter == null ? null : (s, t, v) -> setter.accept(t, v));
	}
	
	public default <Q extends DoubleMapper<Q, S, T>> DoubleMapper<Q, S, T> mapDouble(String name, BiFunction<ConfigSection, T, Double> getter, TriConsumer<ConfigSection, T, Double> setter) {
		DoubleMapper<Q, S, T> sM = new DoubleMapper<>(getSelf(), getter, setter);
		addMapperElement(name, sM);
		return sM;	
	}
	
	public default <Q extends BooleanMapper<Q, S, T>> BooleanMapper<Q, S, T> mapBoolean(String name, Function<T, Boolean> getter, BiConsumer<T, Boolean> setter) {
		return mapBoolean(name, (s, t) -> getter.apply(t), setter == null ? null : (s, t, v) -> setter.accept(t, v));
	}
	
	public default <Q extends BooleanMapper<Q, S, T>> BooleanMapper<Q, S, T> mapBoolean(String name, BiFunction<ConfigSection, T, Boolean> getter, TriConsumer<ConfigSection, T, Boolean> setter) {
		BooleanMapper<Q, S, T> sM = new BooleanMapper<>(getSelf(), getter, setter);
		addMapperElement(name, sM);
		return sM;	
	}
	
	public default <Q extends EnumMapper<Q, S, T, E>, E extends Enum<E>> EnumMapper<Q, S, T, E> mapEnum(Class<E> enumClass, String name, Function<T, E> getter, BiConsumer<T, E> setter) {
		return mapEnum(enumClass, name, (s, t) -> getter.apply(t), setter == null ? null : (s, t, v) -> setter.accept(t, v));
	}
	
	public default <Q extends EnumMapper<Q, S, T, E>, E extends Enum<E>> EnumMapper<Q, S, T, E> mapEnum(Class<E> enumClass, String name, BiFunction<ConfigSection, T, E> getter, TriConsumer<ConfigSection, T, E> setter) {
		EnumMapper<Q, S, T, E> sM = new EnumMapper<>(getSelf(), enumClass, getter, setter);
		addMapperElement(name, sM);
		return sM;	
	}
	
	public default <Q extends JSONArrayMapper<Q, S, T>> JSONArrayMapper<Q, S, T> mapJSONArray(String name, Function<T, JSONArray> getter, BiConsumer<T, JSONArray> setter) {
		return mapJSONArray(name, (s, t) -> getter.apply(t), setter == null ? null : (s, t, v) -> setter.accept(t, v));
	}
	
	public default <Q extends JSONArrayMapper<Q, S, T>> JSONArrayMapper<Q, S, T> mapJSONArray(String name, BiFunction<ConfigSection, T, JSONArray> getter, TriConsumer<ConfigSection, T, JSONArray> setter) {
		JSONArrayMapper<Q, S, T> sM = new JSONArrayMapper<>(getSelf(), getter, setter);
		addMapperElement(name, sM);
		return sM;	
	}
	
	public default <Q extends JSONObjectMapper<Q, S, T>> JSONObjectMapper<Q, S, T> mapJSONObject(String name, Function<T, JSONObject> getter, BiConsumer<T, JSONObject> setter) {
		return mapJSONObject(name, (s, t) -> getter.apply(t), setter == null ? null : (s, t, v) -> setter.accept(t, v));
	}
	
	public default <Q extends JSONObjectMapper<Q, S, T>> JSONObjectMapper<Q, S, T> mapJSONObject(String name, BiFunction<ConfigSection, T, JSONObject> getter, TriConsumer<ConfigSection, T, JSONObject> setter) {
		JSONObjectMapper<Q, S, T> sM = new JSONObjectMapper<>(getSelf(), getter, setter);
		addMapperElement(name, sM);
		return sM;	
	}
	
	public default <Q extends SubMapper<Q, S, T, M>, M> SubMapper<Q, S, T, M> mapObject(String name, Function<JSONObject, M> constructor, Function<T, M> getter, BiConsumer<T, M> setter) {
		return mapObject(name, (s, j) -> constructor.apply(j), (s, t) -> getter.apply(t), setter == null ? null : (s, t, v) -> setter.accept(t, v));
	}
	
	public default <Q extends SubMapper<Q, S, T, M>, M> SubMapper<Q, S, T, M> mapObject(String name, BiFunction<ConfigSection, JSONObject, M> constructor, BiFunction<ConfigSection, T, M> getter, TriConsumer<ConfigSection, T, M> setter) {
		SubMapper<Q, S, T, M> sM = new SubMapper<>(getSelf(), constructor, getter, setter);
		addMapperElement(name, sM);
		return sM;	
	}
	
}
