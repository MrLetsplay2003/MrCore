package me.mrletsplay.mrcore.bukkitimpl.command;

import me.mrletsplay.mrcore.misc.FriendlyException;

public class CommandException extends FriendlyException {

	private static final long serialVersionUID = 7747598170911627164L;
	
	private int index, length;
	
	public CommandException(String reason, int index, int length) {
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
