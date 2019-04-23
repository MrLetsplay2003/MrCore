package me.mrletsplay.mrcore.bukkitimpl.command;

import java.util.function.Function;

import me.mrletsplay.mrcore.misc.NullableOptional;

public interface CommandValueType<T> {

	public String getFriendlyName();
	
	public NullableOptional<T> parse(String value);
	
	public static <T> CommandValueType<T> of(String name, Function<String, NullableOptional<T>> func) {
		return new CommandValueType<T>() {

			@Override
			public String getFriendlyName() {
				return name;
			}

			@Override
			public NullableOptional<T> parse(String value) {
				return func.apply(value);
			}
		};
	}
	
}
