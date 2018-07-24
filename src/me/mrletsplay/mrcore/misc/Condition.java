package me.mrletsplay.mrcore.misc;

import java.util.function.Function;

@FunctionalInterface
public interface Condition<T> extends Function<T, Boolean> {
	
	public default Condition<T> negate() {
		final Condition<T> cond = this;
		return new Condition<T>() {

			@Override
			public Boolean apply(T t) {
				return !cond.apply(t);
			}
		};
	}
	
	public static <T> Condition<T> always() {
		return new Condition<T>() {

			@Override
			public Boolean apply(T t) {
				return true;
			}
		};
	}
	
	public static <T> Condition<T> never() {
		return new Condition<T>() {

			@Override
			public Boolean apply(T t) {
				return false;
			}
		};
	}
	
}
