package me.mrletsplay.mrcore.config.v2;

import java.util.function.BiFunction;

import me.mrletsplay.mrcore.config.impl.ObjectMapperImpl;
import me.mrletsplay.mrcore.misc.CastingFunction;
import me.mrletsplay.mrcore.misc.Complex;

public interface ObjectMapper<E, O> {
	
	public O mapObject(ConfigSection section, E object);
	
	public E constructObject(ConfigSection section, O mapped);
	
	public Complex<E> getMappingClass();
	
	public Complex<O> getMappedClass();
	
	public default O mapRawObject(ConfigSection config, Object object, CastingFunction castingFunction) {
		if(!canMap(object, castingFunction)) {
			throw new IllegalArgumentException("Cannot map object of type " + object.getClass().getName());
		}
		return mapObject(config, getMappingClass().cast(object, castingFunction).orElseThrow(() -> new ClassCastException("Can't cast " + object.getClass().getName() + " to " + getMappingClass().getFriendlyClassName())));
	}
	
	public default E constructRawObject(ConfigSection config, Object object, CastingFunction castingFunction) {
		if(!canConstruct(object, castingFunction)) throw new IllegalArgumentException("Cannot construct object of type " + object.getClass().getName());
		return constructObject(config, getMappedClass().cast(object, castingFunction).orElseThrow(() -> new ClassCastException("Can't cast " + object.getClass().getName() + " to " + getMappingClass().getFriendlyClassName())));
	}
	
	public default boolean canMap(Object object, CastingFunction castingFunction) {
		return getMappingClass().isInstance(object, castingFunction);
	}
	
//	public default boolean canMap(Class<?> clazz) {
//		return getMappingClass().isAssignableFrom(clazz);
//	}
//	
//	public default boolean canMap(Complex<?> clazz) {
//		return getMappingClass().isAssignableFrom(clazz);
//	}
	
	public default boolean canConstruct(Object object, CastingFunction castingFunction) {
		return getMappedClass().isInstance(object, castingFunction);
	}
	
//	public default boolean canConstruct(Class<?> clazz) {
//		return getMappedClass().isAssignableFrom(clazz);
//	}
//	
//	public default boolean canConstruct(Complex<?> clazz) {
//		return getMappedClass().isAssignableFrom(clazz);
//	}
	
	public default int getClassDepth(Object object) {
		return getClassDepth(object.getClass());
	}
	
	public default int getClassDepth(Class<?> clazz) {
		return getClassDepth(Complex.value(clazz));
	}
	
	public default int getClassDepth(Complex<?> clazz) {
//		if(!canMap(clazz)) return -1;
		Class<?> cClass = clazz.getBaseClass();
		int depth = 0;
		while(!cClass.isAssignableFrom(getMappingClass().getBaseClass())) {
			System.out.println(cClass);
			cClass = cClass.getSuperclass();
			if(cClass == null) cClass = Object.class;
			depth++;
		}
		return depth;
		
//		return Complex.getCommonClass(clazz, getMappingClass());
	}
	
	public static <E, O> ObjectMapper<E, O> create(Class<E> mappingClass, Class<O> mappedClass, BiFunction<ConfigSection, E, O> mappingFunction, BiFunction<ConfigSection, O, E> constructingFunction) {
		return create(Complex.value(mappingClass), Complex.value(mappedClass), mappingFunction, constructingFunction);
	}
	
	public static <E, O> ObjectMapper<E, O> create(Complex<E> mappingClass, Complex<O> mappedClass, BiFunction<ConfigSection, E, O> mappingFunction, BiFunction<ConfigSection, O, E> constructingFunction) {
		return new ObjectMapperImpl<>(mappingClass, mappedClass, mappingFunction, constructingFunction);
	}
	
}
