package me.mrletsplay.mrcore.command;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.command.completer.CommandTabCompleter;
import me.mrletsplay.mrcore.command.event.CommandInvokedEvent;
import me.mrletsplay.mrcore.command.option.CommandOption;
import me.mrletsplay.mrcore.command.properties.CommandProperties;
import me.mrletsplay.mrcore.misc.FriendlyException;

public interface Command {
	
	public void setParent(Command parent);
	
	public Command getParent();
	
	public String getName();
	
	public Collection<String> getAliases();
	
	public Collection<? extends CommandOption<?>> getOptions();
	
	public default CommandOption<?> getLongOption(String name) {
		return getOptions().stream().filter(o -> o.getLongName().equals(name)).findFirst().orElse(null);
	}
	
	public default CommandOption<?> getShortOption(String name) {
		return getOptions().stream().filter(o -> o.getShortName() != null && o.getShortName().equals(name)).findFirst().orElse(null);
	}
	
	public String getDescription();
	
	public String getUsage();
	
	public CommandProperties getProperties();
	
	public CommandTabCompleter getTabCompleter();
	
	public Collection<? extends Command> getSubCommands();
	
	public default Command getSubCommand(String label, boolean caseSensitive) {
		return getSubCommands().stream()
				.filter(c -> caseSensitive ?
						c.getName().equals(label) || c.getAliases().contains(label)
						: c.getName().equalsIgnoreCase(label) || c.getAliases().stream().anyMatch(a -> a.equalsIgnoreCase(label)))
				.findFirst().orElse(null);
	}
	
	public default Command getSubCommand(String label) {
		return getSubCommand(label, false);
	}
	
	public default <T extends Annotation> T getAnnotation(Class<T> type) {
		try {
			return getClass().getMethod("action", CommandInvokedEvent.class).getAnnotation(type);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new FriendlyException(e);
		}
	}
	
	public default void sendCommandInfo(CommandSender sender) {
		sender.sendMessage("Command: " + getFullName());
		if(getDescription() != null) sender.sendMessage("Description: " + getDescription());
		if(!getOptions().isEmpty()) sender.sendMessage("Available options: " + getOptions().stream().map(o -> "--" + o.getLongName()).collect(Collectors.joining(", ")));
		if(getUsage() != null) sender.sendMessage("Usage: " + getUsage());
		if(!getSubCommands().isEmpty()) {
			sender.sendMessage("");
			sender.sendMessage("Sub commands:");
			for(Command sub : getSubCommands()) {
				sender.sendMessage((sub.getUsage() == null ? sub.getFullName() : sub.getUsage()) + (sub.getDescription() == null ? "" : " - " + sub.getDescription()));
			}
		}
	}
	
	public default String getFullName() {
		return getParent() == null ? getName() : getParent().getFullName() + " " + getName();
	}
	
	public void action(CommandInvokedEvent event);
	
}
