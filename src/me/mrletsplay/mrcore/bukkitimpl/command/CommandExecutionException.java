package me.mrletsplay.mrcore.bukkitimpl.command;

public class CommandExecutionException extends CommandException {

	private static final long serialVersionUID = -8847278663930814150L;
	
	public CommandExecutionException(String reason, int index, int length) {
		super(reason, index, length);
	}
	
}
