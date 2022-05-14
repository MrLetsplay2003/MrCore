package me.mrletsplay.mrcore.command.event.filter;

import me.mrletsplay.mrcore.command.Command;
import me.mrletsplay.mrcore.command.CommandSender;

public class ArgumentFilterEvent extends CommandTabCompleteFilterEvent<String> {
	
	private Command command;

	public ArgumentFilterEvent(CommandSender sender, String object, Command command) {
		super(sender, object);
		this.command = command;
	}
	
	public Command getCommand() {
		return command;
	}

}
