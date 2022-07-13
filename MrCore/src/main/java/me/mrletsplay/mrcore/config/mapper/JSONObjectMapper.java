package me.mrletsplay.mrcore.config.mapper;

import java.util.EnumSet;
import java.util.function.BiFunction;

import me.mrletsplay.mrcore.config.ConfigSection;
import me.mrletsplay.mrcore.config.mapper.impl.JSONObjectMapperImpl;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.json.converter.DeserializationOption;
import me.mrletsplay.mrcore.json.converter.JSONConverter;
import me.mrletsplay.mrcore.json.converter.JSONConvertible;
import me.mrletsplay.mrcore.json.converter.SerializationOption;
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
	
	public static <E extends JSONConvertible> JSONObjectMapper<E> create(Class<E> mappingClass) {
		return new JSONObjectMapperImpl<>(Complex.value(mappingClass), (s, e) -> e.toJSON(), (s, j) -> JSONConverter.decodeObject(j, mappingClass));
	}
	
	public static <E extends JSONConvertible> JSONObjectMapper<E> create(Class<E> mappingClass, EnumSet<SerializationOption> serializationOptions, EnumSet<DeserializationOption> deserializationOptions) {
		return new JSONObjectMapperImpl<>(Complex.value(mappingClass), (s, e) -> e.toJSON(serializationOptions), (s, j) -> JSONConverter.decodeObject(j, mappingClass, deserializationOptions));
	}
	
}
