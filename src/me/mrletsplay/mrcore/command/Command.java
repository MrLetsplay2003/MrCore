package me.mrletsplay.mrcore.command;

import java.util.Collection;
import java.util.stream.Collectors;

public interface Command {
	
	public String getName();
	
	public Collection<String> getAliases();
	
	public Collection<CommandOption<?>> getOptions();
	
	public default CommandOption<?> getLongOption(String name) {
		return getOptions().stream().filter(o -> o.getLongName().equals(name)).findFirst().orElse(null);
	}
	
	public default CommandOption<?> getShortOption(String name) {
		return getOptions().stream().filter(o -> o.getShortName().equals(name)).findFirst().orElse(null);
	}
	
	public String getDescription();
	
	public default void sendCommandInfo(CommandSender sender) {
		sender.sendMessage("Command: " + getName());
		sender.sendMessage("Description: " + getDescription());
		sender.sendMessage("");
		sender.sendMessage("Available options: " + getOptions().stream().map(o -> "--" + o.getLongName()).collect(Collectors.joining(", ")));
	}
	
	public void action(CommandInvokedEvent event);
	
}
