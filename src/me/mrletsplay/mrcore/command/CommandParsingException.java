package me.mrletsplay.mrcore.command;

import me.mrletsplay.mrcore.misc.FriendlyException;

public class CommandParsingException extends FriendlyException {

	private static final long serialVersionUID = 4301957356870587437L;

	public CommandParsingException(Throwable cause) {
		super(cause);
	}
	
	public CommandParsingException(String reason) {
		super(reason);
	}
	
	public CommandParsingException(String reason, Throwable cause) {
		super(reason, cause);
	}
	
	public void send(CommandSender sender) {
		sender.sendMessage("Exception: " + getMessage());
		printStackTrace();
	}
	
}
