package me.mrletsplay.mrcore.bukkitimpl.command;

public class CommandParsingException extends CommandException {

	private static final long serialVersionUID = 4134702586953223104L;

	public CommandParsingException(String reason, int index, int length) {
		super(reason, index, length);
	}

}
