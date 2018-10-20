package me.mrletsplay.mrcore.misc;

@FunctionalInterface
public interface CastingFunction {

	public <T> NullableOptional<T> cast(Object o, Class<T> typeClass);
	
}
