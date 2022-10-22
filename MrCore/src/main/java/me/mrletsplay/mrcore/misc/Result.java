package me.mrletsplay.mrcore.misc;

import java.util.function.Function;

public class Result<T, E extends Throwable> {

	private T t;
	private E err;

	protected Result(T t, E err) {
		this.t = t;
		this.err = err;
	}

	public boolean isErr() {
		return err != null;
	}

	public T get() throws E {
		if(err != null) throw err;
		return t;
	}

	public T value() throws IllegalStateException {
		if(err != null) throw new IllegalStateException("No value", err);
		return t;
	}

	public E getErr() {
		return err;
	}

	@SuppressWarnings("unchecked")
	public <U> Result<U, E> up() throws IllegalStateException {
		if(err == null) throw new IllegalStateException("No error");
		return (Result<U, E>) this;
	}

	public void throwErr() throws IllegalStateException, E {
		if(err == null) throw new IllegalStateException("No error");
		throw err;
	}

	@SuppressWarnings("unchecked")
	public <O> Result<O, E> map(Function<T, O> map) {
		if(err != null) return (Result<O, E>) this;
		return new Result<>(map.apply(t), null);
	}

	@SuppressWarnings("unchecked")
	public <O extends Throwable> Result<T, O> mapErr(Function<E, O> map) {
		if(err == null) return (Result<T, O>) this;
		return new Result<>(null, map.apply(err));
	}

	public static <T, E extends Throwable> Result<T, E> err(E err) {
		return new Result<>(null, err);
	}

	public static <T, E extends Throwable> Result<T, E> of(T val) {
		return new Result<>(val, null);
	}

	@Override
	public String toString() {
		return String.format("Result<%s>(%s)", err == null ? "Ok" : "Err", err == null ? String.valueOf(t) : String.valueOf(err));
	}

}
