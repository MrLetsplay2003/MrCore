package me.mrletsplay.mrcore.command;

import java.util.Collection;

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
	
	public void action(CommandInvokedEvent event);
	
}
