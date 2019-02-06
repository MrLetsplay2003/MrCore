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
	
	public boolean isFlagPresent(String flag) {
		return parsedCommand.isFlagPresent(flag);
	}
	
	public <T> ParsedCommandFlag<T> getFlag(CommandFlag<T> flag) {
		return parsedCommand.getFlag(flag);
	}
	
	public ParsedCommandFlag<?> getFlag(String flag) {
		return parsedCommand.getFlag(flag);
	}
	
	public void expectArgAmount(int amount, boolean allowMore) {
		if(getArgs().length < amount) {
			throw new CommandExecutionException("Not enough arguments, need " + (allowMore ? "at least " : "") + amount + " arg(s)", 0, 0);
		}else if(getArgs().length != amount && !allowMore) {
			throw new CommandExecutionException("Too many arguments, need exactly " + amount + " arg(s)", 0, 0);
		}
	}
	
	public void expectArgTypes(boolean lastIsVarArg, CommandValueType<?>... argTypes) {
		expectArgAmount(argTypes.length, lastIsVarArg);
		for(int i = 0; i < getArgs().length; i++) {
			CommandValueType<?> valType = argTypes[Math.min(i, argTypes.length - 1)];
			if(!valType.parse(getArgs()[i].getStripped()).isPresent()) throw new CommandExecutionException("Invalid argument type, need " + valType.getFriendlyName(), parsedCommand.getRawArgStringIndex(i), getArgs()[i].getRaw().length());
		}
	}
	
}
