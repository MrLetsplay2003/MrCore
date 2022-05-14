package me.mrletsplay.mrcore.command.event;

import me.mrletsplay.mrcore.command.Command;
import me.mrletsplay.mrcore.command.CommandSender;

public class CommandTabCompleteEvent {
	
	private CommandSender sender;
	private Command command;
	private String label;
	private String[] args;
	
	public CommandTabCompleteEvent(CommandSender sender, Command command, String label, String[] args) {
		this.sender = sender;
		this.command = command;
		this.label = label;
		this.args = args;
	}
	
	public CommandSender getSender() {
		return sender;
	}
	
	public Command getCommand() {
		return command;
	}
	
	public String getLabel() {
		return label;
	}
	
	public String[] getArgs() {
		return args;
	}
	
}
