package me.mrletsplay.mrcore.bukkitimpl.command;

import java.util.Optional;

public class CommandExecutionProperties {

	private Optional<Boolean> allowSubCommands;
	
	public CommandExecutionProperties() {
		this.allowSubCommands = Optional.empty();
	}
	
	public CommandExecutionProperties(boolean allowSubCommands) {
		this.allowSubCommands = Optional.of(allowSubCommands);
	}
	
	public void setAllowSubCommands(boolean allow) {
		this.allowSubCommands = Optional.of(allow);
	}
	
	public Optional<Boolean> getAllowSubCommands() {
		return allowSubCommands;
	}
	
}
