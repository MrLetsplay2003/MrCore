package me.mrletsplay.mrcore.command;

import java.util.Collection;

public interface CommandProvider {
	
	public Collection<? extends Command> getCommands();
	
	public default Command getCommand(String label) {
		return getCommands().stream()
				.filter(c -> c.getName().equals(label) || c.getAliases().contains(label))
				.findFirst().orElse(null);
	}

}
