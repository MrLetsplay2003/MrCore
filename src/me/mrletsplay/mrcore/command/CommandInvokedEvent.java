package me.mrletsplay.mrcore.command;

public class CommandInvokedEvent {

	private CommandSender sender;
	private ParsedCommand parsedCommand;
	
	public CommandInvokedEvent(CommandSender sender, ParsedCommand parsedCommand) {
		this.sender = sender;
		this.parsedCommand = parsedCommand;
	}
	
	public CommandSender getSender() {
		return sender;
	}
	
	public ParsedCommand getParsedCommand() {
		return parsedCommand;
	}
	
}
