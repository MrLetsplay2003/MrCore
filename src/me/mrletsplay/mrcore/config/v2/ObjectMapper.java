package me.mrletsplay.mrcore.config.v2;

import java.util.function.BiFunction;

import me.mrletsplay.mrcore.config.impl.ObjectMapperImpl;

public interface ObjectMapper<E, O> {
	
	public O mapObject(ConfigSection section, E object);
	
	public E constructObject(ConfigSection section, O mapped);
	
	public Class<E> getMappingClass();
	
	public Class<O> getMappedClass();
	
	public default O mapRawObject(ConfigSection config, Object object) {
		if(!canMap(object)) throw new IllegalArgumentException("Cannot map object of type " + object.getClass().getName());
		return mapObject(config, getMappingClass().cast(object));
	}
	
	public default boolean canMap(Object object) {
		return getMappingClass().isInstance(object);
	}
	
	public default boolean canMap(Class<?> clazz) {
		return getMappingClass().isAssignableFrom(clazz);
	}
	
	public default int getClassDepth(Object object) {
		return getClassDepth(object.getClass());
	}
	
	public default int getClassDepth(Class<?> clazz) {
		if(!canMap(clazz)) return -1;
		Class<?> cClass = clazz;
		int depth = 0;
		while(!cClass.equals(getMappingClass())) {
			cClass = cClass.getSuperclass();
			depth++;
		}
		return depth;
	}
	
	public static <E, O> ObjectMapper<E, O> create(Class<E> mappingClass, Class<O> mappedClass, BiFunction<ConfigSection, E, O> mappingFunction, BiFunction<ConfigSection, O, E> constructingFunction) {
		return new ObjectMapperImpl<>(mappingClass, mappedClass, mappingFunction, constructingFunction);
	}
	
}
