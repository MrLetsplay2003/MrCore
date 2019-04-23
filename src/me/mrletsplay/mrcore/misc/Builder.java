package me.mrletsplay.mrcore.misc;

public interface Builder<T, Self extends Builder<T, Self>> {

	@SuppressWarnings("unchecked")
	public default Self getSelf() {
		return (Self) this;
	}
	
	public T create() throws IllegalStateException;
	
}
