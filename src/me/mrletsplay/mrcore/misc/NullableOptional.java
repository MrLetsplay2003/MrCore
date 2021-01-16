package me.mrletsplay.mrcore.misc;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Pretty much a 1:1 recreation of {@link Optional} but with the ability to have <code>null</code> as a value
 * to allow distinction between "not present" (to be interpreted by the implementing code) and an actual <code>null</code> value
 * @author MrLetsplay2003
 *
 * @param <T> The type of value this NullableOptional holds
 */
public class NullableOptional<T> {

	private boolean present;
	private T value;
	
	protected NullableOptional(boolean present, T value) {
		this.present = present;
		this.value = value;
	}
	
	public boolean isPresent() {
		return present;
	}
	
	public T get() throws NoSuchElementException {
		if(!present) throw new NoSuchElementException("No value is present");
		return value;
	}
	
	public <R> NullableOptional<R> cast(Class<R> type) {
		return map(t -> type.cast(value));
	}
	
	public T orElse(T other) {
		return present ? value : other;
	}
	
	public T orElseGet(Supplier<? extends T> other) {
		return present ? value : other.get();
	}
	
	public <X extends Throwable> T orElseThrow(Supplier<X> exceptionSupplier) throws X {
		if(present) return value;
		throw exceptionSupplier.get();
	}
	
	public NullableOptional<T> filter(Predicate<T> predicate) {
		if(present && predicate.test(value)) return this;
		return empty();
	}
	
	public <U> NullableOptional<U> flatMap(Function<? super T, NullableOptional<U>> mapper) {
		if(!present) return empty();
		return mapper.apply(value);
	}
	
	public <U> NullableOptional<U> map(Function<? super T, U> mapper) {
		if(!present) return empty();
		return of(mapper.apply(value));
	}
	
	public <U> NullableOptional<U> mapExact(Function<T, U> mapper) {
		if(!present) return empty();
		return of(mapper.apply(value));
	}
	
	public void ifPresent(Consumer<? super T> consumer) {
		if(present) consumer.accept(value);
	}
	
	public void ifPresentOrElse(Consumer<? super T> consumer, Runnable empty) {
		if(present) {
			consumer.accept(value);
		}else {
			empty.run();
		}
	}
	
	public <X extends Throwable> ErroringNullableOptional<T, X> asErroring(Supplier<X> exceptionSupplier) {
		if(present) return ErroringNullableOptional.ofErroring(value);
		return ErroringNullableOptional.ofErroring(exceptionSupplier.get());
	}
	
	@Override
	public int hashCode() {
		return present ? value.hashCode() : 0;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof NullableOptional<?>)) return false;
		NullableOptional<?> other = (NullableOptional<?>) obj;
		if(!present) return !other.present;
		if(value == null) return other.value == null;
		return value.equals(other.value);
	}
	
	@Override
	public String toString() {
		return "NullableOptional" + (present ? "[" + String.valueOf(value) + "]" : "<Empty>");
	}
	
	public static <T> NullableOptional<T> of(T value) {
		return new NullableOptional<>(true, value);
	}
	
	public static <T> NullableOptional<T> empty() {
		return new NullableOptional<>(false, null);
	}
	
}
