package me.mrletsplay.mrcore.command.event.filter;

import me.mrletsplay.mrcore.command.Command;
import me.mrletsplay.mrcore.command.CommandSender;
import me.mrletsplay.mrcore.command.option.CommandOption;

public class OptionArgumentFilterEvent extends CommandTabCompleteFilterEvent<String> {
	
	private Command command;
	private CommandOption<?> option;

	public OptionArgumentFilterEvent(CommandSender sender, String object, Command command, CommandOption<?> option) {
		super(sender, object);
		this.command = command;
		this.option = option;
	}
	
	public Command getCommand() {
		return command;
	}
	
	public CommandOption<?> getOption() {
		return option;
	}

}
