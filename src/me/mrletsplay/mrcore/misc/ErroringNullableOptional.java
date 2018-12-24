package me.mrletsplay.mrcore.misc;

public class ErroringNullableOptional<T, E extends Throwable> extends NullableOptional<T> {

	private E exception;
	
	protected ErroringNullableOptional(boolean present, T value, E error) {
		super(present, value);
	}
	
	public E getException() {
		if(isPresent()) throw new IllegalStateException("Value is present");
		return exception;
	}
	
	public T orElseThrowException() throws E {
		return orElseThrow(() -> exception);
	}
	
	public static <T, E extends Throwable> ErroringNullableOptional<T, E> ofErroring(T value) {
		return new ErroringNullableOptional<>(true, value, null);
	}
	
	public static <T, E extends Throwable> ErroringNullableOptional<T, E> ofErroring(E exception) {
		return new ErroringNullableOptional<>(true, null, exception);
	}
	
	public static <T, E extends Throwable> ErroringNullableOptional<T, E> emptyErroring() {
		return new ErroringNullableOptional<>(false, null, null);
	}
	
}
