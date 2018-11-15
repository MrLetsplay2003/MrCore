package me.mrletsplay.mrcore.misc;

public interface QuadPredicate<T, U, V, W> {

	public boolean test(T t, U u, V v, W w);
	
	public default QuadPredicate<T, U, V, W> and(QuadPredicate<? super T, ? super U, ? super V, ? super W> other) {
		return (t, u, v, w) -> test(t, u, v, w) && other.test(t, u, v, w);
	}
	
	public default QuadPredicate<T, U, V, W> or(QuadPredicate<? super T, ? super U, ? super V, ? super W> other) {
		return (t, u, v, w) -> test(t, u, v, w) || other.test(t, u, v, w);
	}
	
	public default QuadPredicate<T, U, V, W> negate() {
		return (t, u, v, w) -> !test(t, u, v, w);
	}
	
}
