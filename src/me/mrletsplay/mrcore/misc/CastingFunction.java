package me.mrletsplay.mrcore.misc;

import java.util.Optional;

@FunctionalInterface
public interface CastingFunction {

	public <T> Optional<T> cast(Object o, Class<T> typeClass);
	
}
