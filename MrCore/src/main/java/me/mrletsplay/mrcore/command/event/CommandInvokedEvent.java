package me.mrletsplay.mrcore.command.event;

import me.mrletsplay.mrcore.command.CommandSender;
import me.mrletsplay.mrcore.command.option.CommandOption;
import me.mrletsplay.mrcore.command.parser.ParsedCommand;

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
	
	public String[] getArguments() {
		return parsedCommand.getArguments();
	}
	
	public boolean isOptionPresent(CommandOption<?> option) {
		return parsedCommand.isOptionPresent(option);
	}
	
	public <T> T getOptionValue(CommandOption<T> option) {
		return parsedCommand.getOptionValue(option);
	}
	
}
