package me.mrletsplay.mrcore.config.impl;

import java.util.function.BiFunction;

import me.mrletsplay.mrcore.config.v2.ConfigSection;
import me.mrletsplay.mrcore.config.v2.ObjectMapper;
import me.mrletsplay.mrcore.misc.Complex;

public class ObjectMapperImpl<E, O> implements ObjectMapper<E, O>{

	private Complex<E> mappingClass;
	private Complex<O> mappedClass;
	private BiFunction<ConfigSection, E, O> mappingFunction;
	private BiFunction<ConfigSection, O, E> constructingFunction;
	
	public ObjectMapperImpl(Complex<E> mappingClass, Complex<O> mappedClass, BiFunction<ConfigSection, E, O> mappingFunction, BiFunction<ConfigSection, O, E> constructingFunction) {
		this.mappingClass = mappingClass;
		this.mappedClass = mappedClass;
		this.mappingFunction = mappingFunction;
		this.constructingFunction = constructingFunction;
	}

	@Override
	public Complex<E> getMappingClass() {
		return mappingClass;
	}
	
	@Override
	public Complex<O> getMappedClass() {
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
	
	@Override
	public String toString() {
		return "(" + mappingClass.getFriendlyClassName() + " -> " + mappedClass + ")";
	}
	
}
