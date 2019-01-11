package me.mrletsplay.mrcore.http.server;

import java.util.Optional;
import java.util.function.Function;

import me.mrletsplay.mrcore.http.event.HttpEvent;

@FunctionalInterface
public interface HttpDynamicValue<E extends HttpEvent, T> {

	public Optional<T> get(E event);
	
	public default Function<E, Optional<T>> asFunction() {
		return this::get;
	}
	
	public static <E extends HttpEvent, T> HttpDynamicValue<E, T> empty() {
		return e -> Optional.empty();
	}
	
	public static <E extends HttpEvent, T> HttpDynamicValue<E, T> of(T t) {
		return e -> Optional.ofNullable(t);
	}
	
	public static <E extends HttpEvent, T> HttpDynamicValue<E, T> of(Function<E, T> tFunction) {
		return e -> Optional.ofNullable(tFunction.apply(e));
	}
	
}
