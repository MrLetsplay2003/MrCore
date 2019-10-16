package me.mrletsplay.mrcore.command.properties.auto;

import me.mrletsplay.mrcore.command.Command;

public abstract class AbstractAutoCommandProperties implements AutoCommandProperties {

	private Command command;
	
	public AbstractAutoCommandProperties(Command command) {
		this.command = command;
	}
	
	@Override
	public Command getCommand() {
		return command;
	}
	
}
