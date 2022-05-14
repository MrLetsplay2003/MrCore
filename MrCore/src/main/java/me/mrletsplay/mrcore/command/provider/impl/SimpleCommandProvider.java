package me.mrletsplay.mrcore.command.provider.impl;

import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.mrcore.command.Command;
import me.mrletsplay.mrcore.command.parser.CommandParser;
import me.mrletsplay.mrcore.command.provider.CommandProvider;

public class SimpleCommandProvider<T extends Command> implements CommandProvider {
	
	private List<T> commands;
	private CommandParser parser;

	public SimpleCommandProvider() {
		this.commands = new ArrayList<>();
		this.parser = new CommandParser(this);
	}
	
	public void addCommand(T command) {
		commands.add(command);
	}
	
	@Override
	public List<T> getCommands() {
		return commands;
	}
	
	@Override
	public CommandParser getCommandParser() {
		return parser;
	}

}
