package me.mrletsplay.mrcore.bukkitimpl.command;

import org.bukkit.command.CommandSender;

public class CommandInvokedEvent {

	private CommandSender sender;
	private ParsedCommand parsedCommand;
	
	public CommandInvokedEvent(CommandSender sender, ParsedCommand command) {
		this.sender = sender;
		this.parsedCommand = command;
	}
	
	public EasyCommand getCommand() {
		return parsedCommand.getCommand();
	}
	
	public ParsedCommand getParsedCommand() {
		return parsedCommand;
	}
	
	public CommandSender getSender() {
		return sender;
	}
	
	public CommandArgument[] getArgs() {
		return parsedCommand.getArgs();
	}
	
	public boolean isFlagPresent(CommandFlag<?> flag) {
		return parsedCommand.isFlagPresent(flag);
	}
	
	public <T> ParsedCommandFlag<T> getFlag(CommandFlag<T> flag) {
		return parsedCommand.getFlag(flag);
	}
	
}
