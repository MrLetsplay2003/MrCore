package me.mrletsplay.mrcore.command.event.filter;

import me.mrletsplay.mrcore.command.CommandSender;

public class CommandTabCompleteFilterEvent<T> {
	
	private CommandSender sender;
	private T object;
	
	public CommandTabCompleteFilterEvent(CommandSender sender, T object) {
		this.sender = sender;
		this.object = object;
	}
	
	public CommandSender getSender() {
		return sender;
	}
	
	public T getObject() {
		return object;
	}
	
}
