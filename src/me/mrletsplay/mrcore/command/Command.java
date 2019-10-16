package me.mrletsplay.mrcore.command;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.command.option.CommandOption;
import me.mrletsplay.mrcore.command.properties.CommandProperties;
import me.mrletsplay.mrcore.misc.FriendlyException;

public interface Command {
	
	public void setParent(Command parent);
	
	public Command getParent();
	
	public String getName();
	
	public Collection<String> getAliases();
	
	public Collection<CommandOption<?>> getOptions();
	
	public default CommandOption<?> getLongOption(String name) {
		return getOptions().stream().filter(o -> o.getLongName().equals(name)).findFirst().orElse(null);
	}
	
	public default CommandOption<?> getShortOption(String name) {
		return getOptions().stream().filter(o -> o.getShortName() != null && o.getShortName().equals(name)).findFirst().orElse(null);
	}
	
	public String getDescription();
	
	public CommandProperties getProperties();
	
	public Collection<Command> getSubCommands();
	
	public default Command getSubCommand(String label) {
		return getSubCommands().stream()
				.filter(c -> c.getName().equals(label) || c.getAliases().contains(label))
				.findFirst().orElse(null);
	}
	
	public default <T extends Annotation> T getAnnotation(Class<T> type) {
		try {
			return getClass().getMethod("action", CommandInvokedEvent.class).getAnnotation(type);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new FriendlyException(e);
		}
	}
	
	public default void sendCommandInfo(CommandSender sender) {
		sender.sendMessage("Command: " + getName());
		sender.sendMessage("Description: " + getDescription());
		sender.sendMessage("");
		sender.sendMessage("Available options: " + getOptions().stream().map(o -> "--" + o.getLongName()).collect(Collectors.joining(", ")));
	}
	
	public void action(CommandInvokedEvent event);
	
}
