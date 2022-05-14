package me.mrletsplay.mrcore.command.completer;

import java.util.List;

import me.mrletsplay.mrcore.command.event.CommandTabCompleteEvent;

public interface CommandTabCompleter {
	
	public List<String> tabComplete(CommandTabCompleteEvent event);

}
