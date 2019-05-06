package me.mrletsplay.mrcore.misc;

import java.util.function.Function;

public class ErroringNullableOptional<T, E extends Throwable> extends DualNullableOptional<T, E> {

	protected ErroringNullableOptional(boolean present, T value, E exception) {
		super(present, value, exception);
	}
	
	public boolean hasErrored() {
		return !isPresent();
	}
	
	public E getException() {
		return getOther();
	}
	
	public <X extends Throwable> T orElseThrowOther(Function<E, X> exceptionSupplier) throws X {
		return orElseThrow(() -> exceptionSupplier.apply(getOther()));
	}
	
	public T orElseThrowException() throws E {
		return orElseThrow(() -> getOther());
	}
	
	public static <T, E extends Throwable> ErroringNullableOptional<T, E> ofErroring(T value) {
		return new ErroringNullableOptional<>(true, value, null);
	}
	
	public static <T, E extends Throwable> ErroringNullableOptional<T, E> ofErroring(E exception) {
		return new ErroringNullableOptional<>(false, null, exception);
	}
	
	public static <T, E extends Throwable> ErroringNullableOptional<T, E> emptyErroring() {
		return new ErroringNullableOptional<>(false, null, null);
	}
	
}
