package me.mrletsplay.mrcore.http.server;

import java.util.function.Function;

import me.mrletsplay.mrcore.misc.NullableOptional;

@FunctionalInterface
public interface HttpDynamicValue<E extends HttpEvent, T> {

	public NullableOptional<T> get(E event);
	
	public default Function<E, NullableOptional<T>> asFunction() {
		return this::get;
	}
	
	public static <E extends HttpEvent, T> HttpDynamicValue<E, T> of(T t) {
		return e -> NullableOptional.of(t);
	}
	
	public static <E extends HttpEvent, T> HttpDynamicValue<E, T> of(Function<E, T> tFunction) {
		return e -> NullableOptional.of(tFunction.apply(e));
	}
	
}
