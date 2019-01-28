package me.mrletsplay.mrcore.bukkitimpl.command;

import me.mrletsplay.mrcore.misc.NullableOptional;

public class CommandArgument {

	private String raw;
	
	public CommandArgument(String raw) {
		this.raw = raw;
	}
	
	public <T> NullableOptional<T> as(CommandValueType<T> type) {
		return type.parse(raw);
	}
	
	public String getRaw() {
		return raw;
	}
	
	@Override
	public String toString() {
		return raw;
	}
	
}
