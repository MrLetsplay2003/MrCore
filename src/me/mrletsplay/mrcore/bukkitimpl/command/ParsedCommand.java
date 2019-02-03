package me.mrletsplay.mrcore.bukkitimpl.command;

import java.util.Arrays;
import java.util.List;

public class ParsedCommand {

	private EasyCommand command;
	private String raw;
	private String label;
	private CommandArgument[] args;
	private List<ParsedCommandFlag<?>> flags;
	
	public ParsedCommand(EasyCommand command, String raw, String label, CommandArgument[] args, List<ParsedCommandFlag<?>> flags) {
		this.command = command;
		this.raw = raw;
		this.label = label;
		this.args = args;
		this.flags = flags;
	}
	
	public EasyCommand getCommand() {
		return command;
	}
	
	public String getRaw() {
		return raw;
	}
	
	public String getLabel() {
		return label;
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
	
	public int getRawArgStringIndex(int argIndex) {
		return Arrays.stream(args).limit(argIndex).mapToInt(a -> a.getRaw().length() + 1).sum();
	}
	
}
