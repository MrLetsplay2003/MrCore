package me.mrletsplay.mrcore.misc;

import java.util.Collection;

public class ListUtils {

	public static <T> boolean isTCollection(Collection<?> collection, Class<T> clazz) {
		return isTCollection(collection, clazz, false);
	}
	
	public static <T> boolean isTCollection(Collection<?> collection, Class<T> clazz, boolean strict) {
		Object[] els = collection.toArray();
		if(collection.isEmpty() || els[0] == null) return !strict;
		return clazz.isAssignableFrom(els.getClass());
	}
	
}
