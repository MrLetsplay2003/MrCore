package me.mrletsplay.mrcore.bukkitimpl.command;

import java.util.List;

public class ParsedCommand {

	private EasyCommand command;
	private String raw;
	private CommandArgument[] args;
	private List<ParsedCommandFlag<?>> flags;
	
	public ParsedCommand(EasyCommand command, String raw, CommandArgument[] args, List<ParsedCommandFlag<?>> flags) {
		this.command = command;
		this.raw = raw;
		this.args = args;
		this.flags = flags;
	}
	
	public EasyCommand getCommand() {
		return command;
	}
	
	public String getRaw() {
		return raw;
	}
	
	public CommandArgument[] getArgs() {
		return args;
	}
	
	public boolean isFlagPresent(CommandFlag<?> flag) {
		return getFlag(flag) != null;
	}
	
	@SuppressWarnings("unchecked")
	public <T> ParsedCommandFlag<T> getFlag(CommandFlag<T> flag) {
		ParsedCommandFlag<?> flg = flags.stream().filter(p -> p.getFlag().equals(flag)).findFirst().orElse(null);
		return (ParsedCommandFlag<T>) flg;
	}
	
	public List<ParsedCommandFlag<?>> getFlags() {
		return flags;
	}
	
}
