package me.mrletsplay.mrcore.command.parser;

import java.util.Map;

import me.mrletsplay.mrcore.command.Command;
import me.mrletsplay.mrcore.command.option.CommandOption;

public class ParsedCommand {

	private Command command;
	private String raw;
	private String label;
	private String[] arguments;
	private Map<CommandOption<?>, Object> options;
	
	public ParsedCommand(Command command, String raw, String label, String[] args, Map<CommandOption<?>, Object> options) {
		this.command = command;
		this.raw = raw;
		this.label = label;
		this.arguments = args;
		this.options = options;
	}
	
	public Command getCommand() {
		return command;
	}
	
	public String getRaw() {
		return raw;
	}
	
	public String getLabel() {
		return label;
	}
	
	public String[] getArguments() {
		return arguments;
	}
	
	public Map<CommandOption<?>, Object> getOptionsMap() {
		return options;
	}
	
	public boolean isOptionPresent(CommandOption<?> option) {
		return options.containsKey(option);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getOptionValue(CommandOption<T> option) {
		return (T) options.get(option);
	}
	
}
