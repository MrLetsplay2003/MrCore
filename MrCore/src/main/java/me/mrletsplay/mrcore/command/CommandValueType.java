package me.mrletsplay.mrcore.command;

import java.util.Collection;

import me.mrletsplay.mrcore.misc.NullableOptional;

public interface CommandValueType<T> {
	
	public NullableOptional<T> parse(String value);
	
	public Collection<String> getTabCompleteValues();

}
