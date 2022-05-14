package me.mrletsplay.mrcore.command.event.filter;

import me.mrletsplay.mrcore.command.Command;
import me.mrletsplay.mrcore.command.CommandSender;

public class CommandFilterEvent extends CommandTabCompleteFilterEvent<Command> {

	public CommandFilterEvent(CommandSender sender, Command object) {
		super(sender, object);
	}

}
