package me.mrletsplay.mrcore.misc;

import java.util.concurrent.Callable;

public class MiscUtils {

	public static <T> NullableOptional<T> callSafely(Callable<T> t) {
		try {
			return NullableOptional.of(t.call());
		}catch(Exception e) {
			return NullableOptional.empty();
		}
	}
	
	
}
