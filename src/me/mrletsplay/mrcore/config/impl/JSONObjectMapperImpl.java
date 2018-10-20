package me.mrletsplay.mrcore.config.impl;

import java.util.function.BiFunction;

import me.mrletsplay.mrcore.config.v2.ConfigSection;
import me.mrletsplay.mrcore.config.v2.JSONObjectMapper;
import me.mrletsplay.mrcore.json.JSONObject;

public class JSONObjectMapperImpl<E> implements JSONObjectMapper<E> {

	private Class<E> mappingClass;
	private BiFunction<ConfigSection, E, JSONObject> mappingFunction;
	private BiFunction<ConfigSection, JSONObject, E> constructingFunction;
	
	public JSONObjectMapperImpl(Class<E> mappingClass, BiFunction<ConfigSection, E, JSONObject> mappingFunction, BiFunction<ConfigSection, JSONObject, E> constructingFunction) {
		this.mappingClass = mappingClass;
		this.mappingFunction = mappingFunction;
		this.constructingFunction = constructingFunction;
	}

	@Override
	public Class<E> getMappingClass() {
		return mappingClass;
	}

	@Override
	public JSONObject mapObject(ConfigSection section, E object) {
		return mappingFunction.apply(section, object);
	}

	@Override
	public E constructObject(ConfigSection section, JSONObject mapped) {
		return constructingFunction.apply(section, mapped);
	}

}
