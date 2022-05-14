package me.mrletsplay.mrcore.command.option;

import me.mrletsplay.mrcore.command.CommandValueType;

public interface CommandOption<T> {
	
	public String getShortName();
	
	public String getLongName();
	
	public boolean needsValue();

	public CommandValueType<T> getType();
	
}
