package me.mrletsplay.mrcore.misc;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class MiscUtils {

	public static <T> ErroringNullableOptional<T, ? extends Exception> callSafely(Callable<T> t) {
		try {
			return ErroringNullableOptional.ofErroring(t.call());
		}catch(Exception e) {
			return ErroringNullableOptional.ofErroring(e);
		}
	}

	public static <T> ErroringNullableOptional<Void, ? extends Exception> runSafely(UnsafeRunnable run) {
		try {
			run.run();
			return ErroringNullableOptional.ofErroring((Void) null);
		}catch(Exception e) {
			return ErroringNullableOptional.ofErroring(e);
		}
	}

	public static <K, V> Map.Entry<K, V> newMapEntry(K key, V value) {
		return new AbstractMap.SimpleEntry<>(key, value);
	}
	
	public static <A extends Annotation> Object getAnnotationValueOrDefault(Class<A> annotationClass, A annotation, String valueName) {
		try {
			if(annotation == null) return getAnnotationDefault(annotationClass, valueName);
			return annotationClass.getMethod(valueName).invoke(annotation);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new FriendlyException(e);
		}
	}
	
	public static Object getAnnotationDefault(Class<? extends Annotation> annotationClass, String valueName) {
		try {
			return annotationClass.getMethod(valueName).getDefaultValue();
		} catch (NoSuchMethodException | SecurityException e) {
			throw new FriendlyException(e);
		}
	}
	
}
