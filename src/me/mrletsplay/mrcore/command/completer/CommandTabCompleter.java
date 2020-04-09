package me.mrletsplay.mrcore.command.completer;

import java.util.List;

import me.mrletsplay.mrcore.command.Command;

public interface CommandTabCompleter {
	
	public List<String> tabComplete(Command command, String label, String[] args);

}
