package me.mrletsplay.mrcore.command.provider.impl;

import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.mrcore.command.Command;
import me.mrletsplay.mrcore.command.provider.CommandProvider;

public class SimpleCommandProvider<T extends Command> implements CommandProvider {
	
	private List<T> commands;

	public SimpleCommandProvider() {
		this.commands = new ArrayList<>();
	}
	
	public void addCommand(T command) {
		commands.add(command);
	}
	
	@Override
	public List<T> getCommands() {
		return commands;
	}

}
