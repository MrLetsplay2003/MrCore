package me.mrletsplay.mrcore.misc;

public class DualNullableOptional<T, A> extends NullableOptional<T> {

	private A other;
	
	protected DualNullableOptional(boolean present, T value, A other) {
		super(present, value);
		this.other = other;
	}
	
	public A getOther() {
		if(isPresent()) throw new IllegalStateException("Value is present");
		return other;
	}
	
	@Override
	public String toString() {
		return isPresent() ? "DualNullableOptional<present>[" + get() + "]" : "DualNullableOptional<absent>[" + getOther() + "]";
	}
	
	public static <T, A> DualNullableOptional<T, A> ofDual(T value) {
		return new DualNullableOptional<>(true, value, null);
	}
	
	public static <T, A> DualNullableOptional<T, A> ofDualAbsent(A other) {
		return new DualNullableOptional<>(false, null, other);
	}
	
	public static <T, A> DualNullableOptional<T, A> emptyDual() {
		return new DualNullableOptional<>(false, null, null);
	}
	
}
