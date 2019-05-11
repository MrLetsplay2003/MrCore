package me.mrletsplay.mrcore.command;

import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.mrcore.command.impl.DefaultCommandOption;

public abstract class AbstractCommand implements Command {
	
	private String name;
	private List<String> aliases;
	private List<CommandOption<?>> options;
	private String description;

	public AbstractCommand(String name) {
		this.name = name;
		this.aliases = new ArrayList<>();
		this.options = new ArrayList<>();
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	public void addAlias(String alias) {
		aliases.add(alias);
	}

	@Override
	public List<String> getAliases() {
		return aliases;
	}
	
	public void addOption(CommandOption<?> option) {
		options.add(option);
	}

	@Override
	public List<CommandOption<?>> getOptions() {
		return options;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String getDescription() {
		return description;
	}
	
	public static CommandOption<?> createCommandOption(String shortName, String longName) {
		return DefaultCommandOption.createCommandOption(shortName, longName);
	}
	
	public static <T> CommandOption<T> createCommandOption(String shortName, String longName, CommandValueType<T> type) {
		return DefaultCommandOption.createCommandOption(shortName, longName, type);
	}

}
