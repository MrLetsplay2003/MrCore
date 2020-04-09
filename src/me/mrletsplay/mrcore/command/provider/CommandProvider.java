package me.mrletsplay.mrcore.command.provider;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import me.mrletsplay.mrcore.command.Command;
import me.mrletsplay.mrcore.command.CommandInvokedEvent;
import me.mrletsplay.mrcore.command.CommandSender;
import me.mrletsplay.mrcore.command.parser.CommandParser;
import me.mrletsplay.mrcore.command.parser.CommandParsingException;
import me.mrletsplay.mrcore.command.parser.ParsedCommand;

public interface CommandProvider {
	
	public Collection<? extends Command> getCommands();
	
	public default Command getCommand(String label) {
		return getCommands().stream()
				.filter(c -> c.getName().equals(label) || c.getAliases().contains(label))
				.findFirst().orElse(null);
	}
	
	public default ParsedCommand parseCommand(String commandLine) throws CommandParsingException {
		return CommandParser.parseCommand(this, commandLine);
	}
	
	public default void invoke(CommandSender sender, String commandLine) throws CommandParsingException {
		ParsedCommand cmd = parseCommand(commandLine);
		cmd.getCommand().action(new CommandInvokedEvent(sender, cmd));
	}
	
	public default List<String> tabComplete(String commandLine) {
		try {
			return CommandParser.tabComplete(this, commandLine);
		}catch(CommandParsingException ex) {
			return Collections.emptyList();
		}
	}

}
