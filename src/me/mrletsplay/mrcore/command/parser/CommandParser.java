package me.mrletsplay.mrcore.command.parser;

import static java.util.regex.Pattern.compile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.command.Command;
import me.mrletsplay.mrcore.command.option.CommandOption;
import me.mrletsplay.mrcore.command.provider.CommandProvider;
import me.mrletsplay.mrcore.misc.NullableOptional;

public class CommandParser {

	private static final Pattern
		COMMAND_NAME_FORMAT = compile("[a-zA-Z-_0-9?]*"),
		SHORT_OPTION_FORMAT = compile("-(?<name>[a-zA-Z-_0-9]*)(?: |$)"),
		LONG_OPTION_FORMAT = compile("--(?<name>[a-zA-Z-_0-9]*)(?: |$)"),
		BASIC_ARGUMENT_FORMAT = compile("[^\n\r\t\" ]*(?: |$)"),
		ESCAPED_ARGUMENT_FORMAT = compile("\"(?<value>(?:\\\\[rnt\"\\\\]|[^\\r\\n\\t\"\\\\])*)(?<cq>\")?(?: |$)");
	
	private CommandParser() {}
	
	public static ParserToken<ParsedCommand> parse(CommandProvider provider, String commandLine, boolean tabComplete) throws CommandParsingException {
		return parseCommand(provider, new MutableString(commandLine), tabComplete);
	}
	
	public static ParsedCommand parseCommand(CommandProvider provider, String commandLine) throws CommandParsingException {
		return parseCommand(provider, new MutableString(commandLine), false).getValue();
	}
	
	public static List<String> tabComplete(CommandProvider provider, String commandLine) throws CommandParsingException {
		ParserToken<ParsedCommand> token = parseCommand(provider, new MutableString(commandLine), true);
		if(token.isComplete()) return Collections.emptyList();
		return token.getCompletions();
	}
	
	private static ParserToken<ParsedCommand> parseCommand(CommandProvider provider, MutableString commandLine, boolean tabComplete) {
		String origCommandLine = commandLine.toString();
		ParserToken<Command> cmd = readCommand(provider, commandLine, tabComplete);
		if(cmd == null) throw new CommandParsingException("Invalid command name", 0);
		
		if(!cmd.isComplete()) return new ParserToken<>(cmd.getCompletions());
		
		Command c = cmd.getValue();
		
		while(!commandLine.isEmpty()) {
			ParserToken<Command> sct = readSubCommand(c, commandLine, tabComplete);
			if(sct == null) break; // End of subcommands
			if(!sct.isComplete()) return new ParserToken<>(sct.getCompletions());
			c = sct.getValue();
		}
		
		Map<CommandOption<?>, Object> options = new HashMap<>();
		
		while(!commandLine.isEmpty()) {
			ParserToken<List<CommandOption<?>>> ops = readOption(c, commandLine, tabComplete);
			if(ops == null) break;
			if(!ops.isComplete()) return new ParserToken<>(ops.getCompletions());
			for(CommandOption<?> op : ops.getValue()) {
				if(op.getType() == null) {
					options.put(op, null);
					continue;
				}
				ParserToken<String> v = readArgument(c, op.getType().getTabCompleteValues(), commandLine, tabComplete);
				if(v == null) throw new CommandParsingException("Invalid argument format or no argument present", commandLine.getAmountCut());
				if(!v.isComplete()) return new ParserToken<>(v.getCompletions());
				NullableOptional<?> pv = op.getType().parse(v.getValue());
				if(!pv.isPresent()) throw new CommandParsingException("Invalid option value for option \"" + op.getLongName() + "\"", commandLine.getAmountCut());
				options.put(op, pv.get());
			}
		}
		
		List<String> args = new ArrayList<>();
		
		while(!commandLine.isEmpty()) {
			ParserToken<String> arg = readArgument(c, Collections.emptyList(), commandLine, tabComplete);
			if(arg == null) throw new CommandParsingException("Invalid argument format", commandLine.getAmountCut());
			if(!arg.isComplete()) return new ParserToken<>(arg.getCompletions());
			args.add(arg.getValue());
		}
		
		return new ParserToken<ParsedCommand>(new ParsedCommand(c, origCommandLine, "lol", args.toArray(new String[args.size()]), options));
	}
	
	private static ParserToken<Command> readCommand(CommandProvider provider, MutableString commandLine, boolean tabComplete) {
		Matcher m = tryMatch(commandLine, COMMAND_NAME_FORMAT);
		if(m == null) return null;
		String cName = m.group();
		Command c = provider.getCommand(cName);
		
		if(c == null) {
			if(!tabComplete || commandLine.length() > m.group().length()) return null;
			
			commandLine.cutStart(m.group().length()).trim();
			
			return new ParserToken<>(getCommandCompletions(provider.getCommands(), cName));
		}
		
		commandLine.cutStart(m.group().length()).trim();
		return new ParserToken<>(c);
	}
	
	private static List<String> getCommandCompletions(Collection<? extends Command> cmds, String cName) {
		List<String> cs = new ArrayList<>();
		for(Command cmd : cmds) {
			if(cmd.getName().toLowerCase().startsWith(cName.toLowerCase())) {
				cs.add(cName.substring(cName.length()));
			}
			cmd.getAliases().stream()
				.filter(a -> a.toLowerCase().startsWith(cName.toLowerCase()))
				.forEach(a -> cs.add(a.substring(cName.length())));
		}
		return cs;
	}
	
	private static ParserToken<Command> readSubCommand(Command parent, MutableString commandLine, boolean tabComplete) {
		Matcher m = tryMatch(commandLine, COMMAND_NAME_FORMAT);
		if(m == null) return null;
		String cName = m.group();
		Command c = parent.getSubCommand(cName);
		
		if(c == null) {
			if(!tabComplete || commandLine.length() > m.group().length()) return null;
			
			commandLine.cutStart(m.group().length()).trim();
			
			return new ParserToken<>(getCommandCompletions(parent.getSubCommands(), cName));
		}
		
		commandLine.cutStart(m.group().length()).trim();
		return new ParserToken<>(c);
	}
	
	private static ParserToken<List<CommandOption<?>>> readOption(Command c, MutableString commandLine, boolean tabComplete) {
		ParserToken<CommandOption<?>> lo = readLongOption(c, commandLine, tabComplete);
		if(lo != null) return lo.map(Collections::singletonList);
		
		return readShortOption(c, commandLine, tabComplete);
	}
	
	private static ParserToken<CommandOption<?>> readLongOption(Command c, MutableString commandLine, boolean tabComplete) {
		Matcher m = tryMatch(commandLine, LONG_OPTION_FORMAT);
		if(m == null) return null;
		String opName = m.group("name");
		CommandOption<?> op = c.getLongOption(opName);
		
		if(op == null) {
			if(!tabComplete || commandLine.length() > m.group().length()) return null;
			
			commandLine.cutStart(m.group().length()).trim();
			
			return new ParserToken<>(c.getOptions().stream()
					.filter(o -> o.getLongName().toLowerCase().startsWith(opName.toLowerCase()))
					.map(CommandOption::getLongName)
					.map(s -> s.substring(opName.length()))
					.collect(Collectors.toList()));
		}

		commandLine.cutStart(m.group().length()).trim();
		return new ParserToken<>(op);
	}
	
	private static ParserToken<List<CommandOption<?>>> readShortOption(Command c, MutableString commandLine, boolean tabComplete) {
		Matcher m = tryMatch(commandLine, SHORT_OPTION_FORMAT);
		if(m == null) return null;
		
		String opName = m.group("name");
		List<CommandOption<?>> op = Arrays.stream(opName.split(""))
				.filter(s -> !s.isEmpty())
				.map(c::getShortOption)
				.collect(Collectors.toList());

		if(op.stream().anyMatch(Objects::isNull)) return null;
		
		if(tabComplete && commandLine.length() == m.group().length()) {
			commandLine.cutStart(m.group().length()).trim();
			
			return new ParserToken<>(c.getOptions().stream()
					.map(CommandOption::getShortName)
					.collect(Collectors.toList()));
		}
		
		commandLine.cutStart(m.group().length()).trim();
		return new ParserToken<>(op);
	}
	
	private static ParserToken<String> readArgument(Command c, Collection<String> defaultValues, MutableString commandLine, boolean tabComplete) {
		ParserToken<String> arg = readBasicArgument(c, defaultValues, commandLine, tabComplete);
		if(arg != null) return arg;
		
		return readEscapedArgument(c, defaultValues, commandLine, tabComplete);
	}
	
	private static ParserToken<String> readBasicArgument(Command c, Collection<String> defaultValues, MutableString commandLine, boolean tabComplete) {
		Matcher m = tryMatch(commandLine, BASIC_ARGUMENT_FORMAT);
		if(m == null) return null;
		String value = m.group();
		
		if(value.isEmpty() && (!tabComplete || commandLine.length() > m.group().length())) return null;
		
		if(tabComplete && commandLine.length() == m.group().length()) return new ParserToken<>(defaultValues.stream()
				.filter(v -> v.startsWith(value))
				.map(s -> s.substring(value.length()))
				.collect(Collectors.toList()));
		
		commandLine.cutStart(m.group().length()).trim();
		
		return new ParserToken<String>(value);
	}
	
	private static ParserToken<String> readEscapedArgument(Command c, Collection<String> defaultValues, MutableString commandLine, boolean tabComplete) {
		Matcher m = tryMatch(commandLine, ESCAPED_ARGUMENT_FORMAT);
		if(m == null) return null;
		String
			value = m.group("value"),
			uValue = unescapeArg(value);
		
		if(m.group("cq") == null) {
			if(!tabComplete || commandLine.length() > m.group().length()) return null;
			
			commandLine.cutStart(m.group().length()).trim();
			
			List<String> tabCompletions = new ArrayList<>();
			tabCompletions.add("\"");
			tabCompletions.addAll(defaultValues.stream()
					.filter(v -> v.startsWith(uValue))
					.map(v -> escapeArg(v) + "\"")
					.map(s -> s.substring(value.length()))
					.collect(Collectors.toList()));
			return new ParserToken<>(tabCompletions);
		}
		
		commandLine.cutStart(m.group().length()).trim();
		
		return new ParserToken<String>(uValue);
	}
	
	private static String unescapeArg(String arg) {
		StringBuilder u = new StringBuilder();
		for(int i = 0; i < arg.length(); i++) {
			char c = arg.charAt(i);
			if(c != '\\') {
				u.append(c);
				continue;
			}
			if(++i == arg.length()) return null;
			char n = arg.charAt(i);
			switch(n) {
				case 'n':
				{
					u.append('\n');
					break;
				}
				case 'r':
				{
					u.append('\r');
					break;
				}
				case 't':
				{
					u.append('\t');
					break;
				}
				case '\\':
				{
					u.append('\\');
					break;
				}
				default:
				{
					u.append(n);
					break;
				}
			}
		}
		return u.toString();
	}
	
	private static String escapeArg(String arg) {
		StringBuilder u = new StringBuilder();
		for(int i = 0; i < arg.length(); i++) {
			char c = arg.charAt(i);
			switch(c) {
				case '\n':
				{
					u.append("\\n");
					break;
				}
				case '\r':
				{
					u.append("\\r");
					break;
				}
				case '\t':
				{
					u.append("\\t");
					break;
				}
				case '\\':
				{
					u.append("\\\\");
					break;
				}
				case '\"':
				{
					u.append("\\\"");
					break;
				}
				default:
				{
					u.append(c);
					break;
				}
			}
		}
		return u.toString();
	}
	
	private static Matcher tryMatch(CharSequence s, Pattern p) {
		Matcher m = p.matcher(s);
		return m.lookingAt() ? m : null;
	}
	
	public static class MutableString implements CharSequence {
		
		private String str;
		private int amountCut;
		
		private MutableString(String str, int amountCut) {
			this.str = str;
			this.amountCut = amountCut;
		}
		
		public MutableString(String str) {
			this(str, 0);
		}
		
		public MutableString cutStart(int len) {
			amountCut += len;
			this.str = str.substring(len);
			return this;
		}
		
		public MutableString copy() {
			return new MutableString(str, amountCut);
		}
		
		public MutableString trim() {
			String os = str;
			str = str.trim();
			amountCut += os.length() - str.length();
			return this;
		}
		
		public boolean isEmpty() {
			return str.isEmpty();
		}
		
		public int getAmountCut() {
			return amountCut;
		}
		
		@Override
		public boolean equals(Object obj) {
			return str.equals(obj);
		}

		@Override
		public int length() {
			return str.length();
		}

		@Override
		public char charAt(int index) {
			return str.charAt(index);
		}

		@Override
		public CharSequence subSequence(int start, int end) {
			return str.subSequence(start, end);
		}
		
		@Override
		public String toString() {
			return str;
		}
		
	}
	
}
