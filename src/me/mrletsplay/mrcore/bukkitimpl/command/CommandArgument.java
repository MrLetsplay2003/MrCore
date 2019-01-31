package me.mrletsplay.mrcore.bukkitimpl.command;

import me.mrletsplay.mrcore.misc.NullableOptional;

public class CommandArgument {

	private String raw, stripped;
	
	public CommandArgument(String raw, String stripped) {
		this.raw = raw;
		this.stripped = stripped;
	}
	
	public <T> NullableOptional<T> as(CommandValueType<T> type) {
		return type.parse(raw);
	}
	
	public boolean isOfType(CommandValueType<?> type) {
		return as(type).isPresent();
	}
	
	public String getRaw() {
		return raw;
	}
	
	public String getStripped() {
		return stripped;
	}
	
	@Override
	public String toString() {
		return stripped;
	}
	
}
