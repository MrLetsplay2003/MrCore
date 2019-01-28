package me.mrletsplay.mrcore.bukkitimpl.command;

import me.mrletsplay.mrcore.misc.FriendlyException;

public class CommandParsingException extends FriendlyException {

	private static final long serialVersionUID = 4134702586953223104L;

	private int index, length;
	
	public CommandParsingException(String reason, int index, int length) {
		super(reason);
		this.index = index;
		this.length = length;
	}
	
	public int getIndex() {
		return index;
	}
	
	public int getLength() {
		return length;
	}

}
