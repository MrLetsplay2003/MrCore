package me.mrletsplay.mrcore.command.completer;

import java.util.List;

import me.mrletsplay.mrcore.command.Command;
import me.mrletsplay.mrcore.command.CommandSender;

public interface CommandTabCompleter {
	
	public List<String> tabComplete(CommandSender sender, Command command, String label, String[] args);

}
