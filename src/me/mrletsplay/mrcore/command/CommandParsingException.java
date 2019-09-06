package me.mrletsplay.mrcore.command;

import me.mrletsplay.mrcore.misc.FriendlyException;

public class CommandParsingException extends FriendlyException {

	private static final long serialVersionUID = 4301957356870587437L;
	
	private int index;
	
	public CommandParsingException(Throwable cause) {
		super(cause);
	}
	
	public CommandParsingException(String reason) {
		super(reason);
	}
	
	public CommandParsingException(String reason, Throwable cause) {
		super(reason, cause);
	}
	
	public CommandParsingException(String reason, int index) {
		super(reason);
		this.index = index;
	}
	
	public int getIndex() {
		return index;
	}
	
	public void send(CommandSender sender) {
		sender.sendMessage("Error: " + getMessage());
	}
	
}
