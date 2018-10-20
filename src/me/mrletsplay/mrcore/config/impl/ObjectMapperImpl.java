package me.mrletsplay.mrcore.config.impl;

import java.util.function.BiFunction;

import me.mrletsplay.mrcore.config.v2.ConfigSection;
import me.mrletsplay.mrcore.config.v2.ObjectMapper;

public class ObjectMapperImpl<E, O> implements ObjectMapper<E, O>{

	private Class<E> mappingClass;
	private Class<O> mappedClass;
	private BiFunction<ConfigSection, E, O> mappingFunction;
	private BiFunction<ConfigSection, O, E> constructingFunction;
	
	public ObjectMapperImpl(Class<E> mappingClass, Class<O> mappedClass, BiFunction<ConfigSection, E, O> mappingFunction, BiFunction<ConfigSection, O, E> constructingFunction) {
		this.mappingClass = mappingClass;
		this.mappedClass = mappedClass;
		this.mappingFunction = mappingFunction;
		this.constructingFunction = constructingFunction;
	}

	@Override
	public Class<E> getMappingClass() {
		return mappingClass;
	}
	
	@Override
	public Class<O> getMappedClass() {
		return mappedClass;
	}

	@Override
	public O mapObject(ConfigSection section, E object) {
		return mappingFunction.apply(section, object);
	}

	@Override
	public E constructObject(ConfigSection section, O mapped) {
		return constructingFunction.apply(section, mapped);
	}
	
}
