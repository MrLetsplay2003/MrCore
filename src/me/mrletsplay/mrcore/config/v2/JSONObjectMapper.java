package me.mrletsplay.mrcore.config.v2;

import java.util.function.BiFunction;

import me.mrletsplay.mrcore.config.impl.JSONObjectMapperImpl;
import me.mrletsplay.mrcore.json.JSONObject;

public interface JSONObjectMapper<E> extends ObjectMapper<E, JSONObject> {

	@Override
	public default Class<JSONObject> getMappedClass() {
		return JSONObject.class;
	}
	
	public static <E> JSONObjectMapper<E> create(Class<E> mappingClass, BiFunction<ConfigSection, E, JSONObject> mappingFunction, BiFunction<ConfigSection, JSONObject, E> constructingFunction) {
		return new JSONObjectMapperImpl<>(mappingClass, mappingFunction, constructingFunction);
	}
	
}
