package me.mrletsplay.mrcore.command.event.filter;

import me.mrletsplay.mrcore.command.Command;
import me.mrletsplay.mrcore.command.CommandSender;
import me.mrletsplay.mrcore.command.option.CommandOption;

public class OptionFilterEvent extends CommandTabCompleteFilterEvent<CommandOption<?>> {
	
	private Command command;

	public OptionFilterEvent(CommandSender sender, CommandOption<?> object, Command command) {
		super(sender, object);
		this.command = command;
	}
	
	public Command getCommand() {
		return command;
	}

}
