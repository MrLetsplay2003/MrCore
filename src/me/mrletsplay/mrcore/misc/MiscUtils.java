package me.mrletsplay.mrcore.misc;

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
	
}
