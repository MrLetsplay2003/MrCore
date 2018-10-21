package me.mrletsplay.mrcore.config.v2;

import java.util.function.BiFunction;

import me.mrletsplay.mrcore.config.impl.JSONObjectMapperImpl;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.misc.Complex;

public interface JSONObjectMapper<E> extends ObjectMapper<E, JSONObject> {

	@Override
	public default Complex<JSONObject> getMappedClass() {
		return Complex.value(JSONObject.class);
	}
	
	public static <E> JSONObjectMapper<E> create(Class<E> mappingClass, BiFunction<ConfigSection, E, JSONObject> mappingFunction, BiFunction<ConfigSection, JSONObject, E> constructingFunction) {
		return create(Complex.value(mappingClass), mappingFunction, constructingFunction);
	}
	
	public static <E> JSONObjectMapper<E> create(Complex<E> mappingClass, BiFunction<ConfigSection, E, JSONObject> mappingFunction, BiFunction<ConfigSection, JSONObject, E> constructingFunction) {
		return new JSONObjectMapperImpl<>(mappingClass, mappingFunction, constructingFunction);
	}
	
}
