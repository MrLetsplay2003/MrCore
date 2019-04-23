package me.mrletsplay.mrcore.bukkitimpl.command.completion;

import org.bukkit.command.CommandSender;

import me.mrletsplay.mrcore.bukkitimpl.command.CommandArgument;
import me.mrletsplay.mrcore.bukkitimpl.command.CommandFlag;
import me.mrletsplay.mrcore.bukkitimpl.command.EasyCommand;
import me.mrletsplay.mrcore.bukkitimpl.command.ParsedCommand;
import me.mrletsplay.mrcore.bukkitimpl.command.ParsedCommandFlag;

public class CommandTabCompleteEvent {

	private CommandSender sender;
	private ParsedCommand parsedCommand;
	private CommandElement element;
	
	public CommandTabCompleteEvent(CommandSender sender, ParsedCommand command, CommandElement element) {
		this.sender = sender;
		this.parsedCommand = command;
		this.element = element;
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
	
	public CommandElement getElementToComplete() {
		return element;
	}
	
}
