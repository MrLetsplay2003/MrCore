package me.mrletsplay.mrcore.bukkitimpl.command.completion;

@FunctionalInterface
public interface CommandTabCompleter {
	
	public static final CommandTabCompleter DEFAULT_COMPLETER = event -> new CommandTabCompletions();
	
	public CommandTabCompletions getTabCompletions(CommandTabCompleteEvent event);
	
}
