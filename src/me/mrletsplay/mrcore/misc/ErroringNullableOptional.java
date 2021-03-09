package me.mrletsplay.mrcore.misc;

import java.util.function.Consumer;
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
	
	public void ifPresentOrElse(Consumer<? super T> consumer, Runnable empty, Consumer<? super E> exception) {
		if(isPresent()) {
			consumer.accept(get());
		}else {
			E ex = getException();
			if(ex == null) {
				empty.run();
			}else {
				exception.accept(ex);
			}
		}
	}
	
	public static <T, E extends Throwable> ErroringNullableOptional<T, E> ofErroring(T value) {
		return new ErroringNullableOptional<>(true, value, null);
	}
	
	public static <T, E extends Throwable> ErroringNullableOptional<T, E> ofErroring(E exception) {
		return new ErroringNullableOptional<>(false, null, exception);
	}
	
	/**
	 * @deprecated Doesn't make too much sense becaue it will create an optional with both values absent. Use {@link #ofErroring(Object)} with a null exception to explicitly create an absent optional with a null value
	 * @param <T> The value type
	 * @param <E> The exception type
	 * @return An {@link ErroringNullableOptional} with both values absent (the exception is null)
	 */
	public static <T, E extends Throwable> ErroringNullableOptional<T, E> emptyErroring() {
		return new ErroringNullableOptional<>(false, null, null);
	}
	
}
