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
		SHORT_OPTION_FORMAT = compile("-(?<name>[a-zA-Z-_0-9]+)(?= |$)"),
		LONG_OPTION_FORMAT = compile("--(?<name>[a-zA-Z-_0-9]*)(?= |$)"),
		BASIC_ARGUMENT_FORMAT = compile("(?<arg>(?:\\\\ |[^\n\r\t\" ])*)(?= |$)"),
		ESCAPED_ARGUMENT_FORMAT = compile("\"(?<value>(?:\\\\[rnt\"\\\\]|[^\\r\\n\\t\"\\\\])*)(?<cq>\")?(?= |$)");
	
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
		if(!commandLine.isBlank()) commandLine.trim();
		if(cmd == null) throw new CommandParsingException("Invalid command name", 0);
		
		if(!cmd.isComplete()) return new ParserToken<>(cmd.getCompletions());
		
		Command c = cmd.getValue();
		String label = cmd.getRaw();
		
		while(!commandLine.isBlank()) {
			ParserToken<Command> sct = readSubCommand(c, label, commandLine, tabComplete);
			if(sct == null) break; // End of subcommands
			if(!sct.isComplete()) return new ParserToken<>(sct.getCompletions());
			c = sct.getValue();
			label = sct.getRaw();
			
			if(commandLine.isBlank()) break;
			commandLine.trim();
		}
		
		Map<CommandOption<?>, Object> options = new HashMap<>();
		
		while(!commandLine.isEmpty()) {
			if(tabComplete && commandLine.hasBlankContent()) {
				List<String> cs = new ArrayList<>();
				if(c.getTabCompleter() != null) {
					cs.addAll(c.getTabCompleter().tabComplete(c, label, new String[0]));
				}
				cs.addAll(prefixOptionCompletions(getOptionCompletions(c, "", true), true));
				return new ParserToken<>(cs);
			}
			
			ParserToken<List<CommandOption<?>>> ops = readOption(c, label, commandLine, tabComplete);
			
			if(ops == null) break;
			if(!ops.isComplete()) return new ParserToken<>(ops.getCompletions());
			
			if(!commandLine.isBlank()) commandLine.trim();
			
			for(CommandOption<?> op : ops.getValue()) {
				if(op.getType() == null) {
					options.put(op, null);
					continue;
				}
				
				if(tabComplete && commandLine.hasBlankContent()) {
					return new ParserToken<>(autoQuoteArguments(op.getType().getTabCompleteValues()));
				}
				
				ParserToken<String> v = readArgument(c, op.getType().getTabCompleteValues(), commandLine, tabComplete);
				
				if(v == null) throw new CommandParsingException("Invalid argument format or no argument present", commandLine.getAmountCut());
				if(!v.isComplete()) return new ParserToken<>(v.getCompletions());
				NullableOptional<?> pv = op.getType().parse(v.getValue());
				if(!pv.isPresent()) throw new CommandParsingException("Invalid option value for option \"" + op.getLongName() + "\"", commandLine.getAmountCut());
				options.put(op, pv.get());
				
				if(commandLine.isBlank()) continue;
				commandLine.trim();
			}
		}
		
		List<String> args = new ArrayList<>();
		
		while(!commandLine.isBlank()) {
			List<String> cs = Collections.emptyList();
			if(tabComplete && c.getTabCompleter() != null) {
				cs = c.getTabCompleter().tabComplete(c, label, args.toArray(new String[args.size()]));
			}
			ParserToken<String> arg = readArgument(c, cs, commandLine, tabComplete);
			if(arg == null) throw new CommandParsingException("Invalid argument format", commandLine.getAmountCut());
			if(!arg.isComplete()) return new ParserToken<>(arg.getCompletions());
			args.add(arg.getValue());

			if(commandLine.isBlank()) break;
			commandLine.trim();
		}
		
		if(tabComplete && commandLine.hasBlankContent() && c.getTabCompleter() != null) {
			return new ParserToken<>(autoQuoteArguments(c.getTabCompleter().tabComplete(c, label, args.toArray(new String[args.size()]))));
		}
		
		return new ParserToken<>(new ParsedCommand(c, origCommandLine, label, args.toArray(new String[args.size()]), options));
	}
	
	private static ParserToken<Command> readCommand(CommandProvider provider, MutableString commandLine, boolean tabComplete) {
		Matcher m = tryMatch(commandLine, COMMAND_NAME_FORMAT);
		if(m == null) return null;
		String cName = m.group();
		Command c = provider.getCommand(cName);
		
		if(c == null) {
			if(!tabComplete || commandLine.length() > m.group().length()) return null;
			
			commandLine.cutStart(m.group().length());
			
			return new ParserToken<>(getCommandCompletions(provider.getCommands(), cName));
		}
		
		commandLine.cutStart(m.group().length());
		
		if(commandLine.hasBlankContent() && tabComplete) {
			List<String> cs = new ArrayList<>();
			cs.addAll(getCommandCompletions(c.getSubCommands(), ""));
			c.getOptions().forEach(o -> cs.add("--" + o.getLongName()));
			return new ParserToken<>(cs);
		}
		
		return new ParserToken<>(c, cName);
	}
	
	private static List<String> getCommandCompletions(Collection<? extends Command> cmds, String cName) {
		List<String> cs = new ArrayList<>();
		for(Command cmd : cmds) {
			if(cmd.getName().toLowerCase().startsWith(cName.toLowerCase())) {
				cs.add(cmd.getName().substring(cName.length()));
			}
			cmd.getAliases().stream()
				.filter(a -> a.toLowerCase().startsWith(cName.toLowerCase()))
				.forEach(a -> cs.add(a.substring(cName.length())));
		}
		return cs;
	}
	
	private static List<String> getOptionCompletions(Command cmd, String optionName, boolean longOption) {
		if(!longOption) return cmd.getOptions().stream()
				.map(CommandOption::getShortName)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
		
		return cmd.getOptions().stream()
				.map(CommandOption::getLongName)
				.filter(c -> c.toLowerCase().startsWith(optionName.toLowerCase()))
				.map(c -> c.substring(optionName.length()))
				.collect(Collectors.toList());
	}
	
	private static List<String> prefixOptionCompletions(List<String> options, boolean longOptions) {
		return options.stream()
				.map(o -> (longOptions ? "--" : "-") + o)
				.collect(Collectors.toList());
	}
	
	private static List<String> getArgumentCompletions(Collection<String> completions, String rawArgument, boolean quoted) {
		return completions.stream()
				.map(o -> quoted ? escapeArgument(o, true) + "\"" : escapeArgument(o, false))
				.filter(c -> c.toLowerCase().startsWith(rawArgument.toLowerCase()))
				.map(c -> c.substring(rawArgument.length()))
				.collect(Collectors.toList());
	}
	
	private static ParserToken<Command> readSubCommand(Command parent, String label, MutableString commandLine, boolean tabComplete) {
		Matcher m = tryMatch(commandLine, COMMAND_NAME_FORMAT);
		if(m == null) return null;
		String cName = m.group();
		Command c = parent.getSubCommand(cName);
		
		if(c == null) {
			if(!tabComplete || commandLine.length() > m.group().length()) return null;
			
			commandLine.cutStart(m.group().length());
			
			List<String> cs = new ArrayList<>();
			cs.addAll(getCommandCompletions(parent.getSubCommands(), cName));
			
			if(parent.getTabCompleter() != null) {
				cs.addAll(getArgumentCompletions(parent.getTabCompleter().tabComplete(c, label, new String[0]), cName, false));
			}
			
			if(cName.startsWith("-")) {
				boolean isLong = cName.startsWith("--");
				cs.addAll(getOptionCompletions(parent, cName.substring(isLong ? 2 : 1), isLong));
			}
			
			return new ParserToken<>(cs);
		}
		
		commandLine.cutStart(m.group().length());
		
		if(commandLine.toString().equals(" ") && tabComplete) {
			List<String> cs = new ArrayList<>();
			cs.addAll(getCommandCompletions(c.getSubCommands(), ""));
			c.getOptions().forEach(o -> cs.add("--" + o.getLongName()));
			if(c.getTabCompleter() != null) cs.addAll(autoQuoteArguments(c.getTabCompleter().tabComplete(c, cName, new String[0])));
			return new ParserToken<>(cs);
		}
		
		return new ParserToken<>(c, cName);
	}
	
	private static ParserToken<List<CommandOption<?>>> readOption(Command c, String label, MutableString commandLine, boolean tabComplete) {
		ParserToken<CommandOption<?>> lo = readLongOption(c, label, commandLine, tabComplete);
		if(lo != null) return lo.map(Collections::singletonList);
		
		return readShortOption(c, label, commandLine, tabComplete);
	}
	
	private static ParserToken<CommandOption<?>> readLongOption(Command c, String label, MutableString commandLine, boolean tabComplete) {
		Matcher m = tryMatch(commandLine, LONG_OPTION_FORMAT);
		if(m == null) return null;
		String opName = m.group("name");
		CommandOption<?> op = c.getLongOption(opName);
		
		if(op == null) {
			if(!tabComplete || commandLine.length() > m.group().length()) return null;
			
			commandLine.cutStart(m.group().length());
			
			return new ParserToken<>(getOptionCompletions(c, opName, true));
		}
		
		commandLine.cutStart(m.group().length());
		
		if(commandLine.toString().equals(" ") && tabComplete) {
			if(op.getType() == null) {
				List<String> cs = new ArrayList<>();
				if(c.getTabCompleter() != null) {
					cs.addAll(c.getTabCompleter().tabComplete(c, label, new String[0]));
				}
				cs.addAll(prefixOptionCompletions(getOptionCompletions(c, "", true), true));
				return new ParserToken<>(cs);
			}
			return new ParserToken<>(autoQuoteArguments(op.getType().getTabCompleteValues()));
		}
		
		return new ParserToken<>(op);
	}
	
	private static ParserToken<List<CommandOption<?>>> readShortOption(Command c, String label, MutableString commandLine, boolean tabComplete) {
		Matcher m = tryMatch(commandLine, SHORT_OPTION_FORMAT);
		if(m == null) return null;
		
		String opName = m.group("name");
		List<CommandOption<?>> op = Arrays.stream(opName.split(""))
				.filter(s -> !s.isEmpty())
				.map(c::getShortOption)
				.collect(Collectors.toList());

		if(op.stream().anyMatch(Objects::isNull)) return null;
		
		if(tabComplete && commandLine.length() == m.group().length()) {
			return new ParserToken<>(c.getOptions().stream()
					.map(CommandOption::getShortName)
					.collect(Collectors.toList()));
		}
		
		commandLine.cutStart(m.group().length());
		
		if(commandLine.toString().equals(" ") && tabComplete) {
			CommandOption<?> fOp = op.stream().filter(o -> o.getType() != null).findFirst().orElse(null);
			if(fOp == null) {
				List<String> cs = new ArrayList<>();
				if(c.getTabCompleter() != null) {
					cs.addAll(c.getTabCompleter().tabComplete(c, label, new String[0]));
				}
				cs.addAll(prefixOptionCompletions(getOptionCompletions(c, "", true), true));
				return new ParserToken<>(cs);
			}
			return new ParserToken<>(autoQuoteArguments(fOp.getType().getTabCompleteValues()));
		}
		
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
		String value = m.group("arg");
		
		if(value.isEmpty() && (!tabComplete || commandLine.length() > m.group().length())) return null;
		
		if(tabComplete && commandLine.length() == m.group().length()) return new ParserToken<>(getArgumentCompletions(defaultValues, value, false));
		
		commandLine.cutStart(m.group().length());
		
		return new ParserToken<String>(value);
	}
	
	private static ParserToken<String> readEscapedArgument(Command c, Collection<String> defaultValues, MutableString commandLine, boolean tabComplete) {
		Matcher m = tryMatch(commandLine, ESCAPED_ARGUMENT_FORMAT);
		if(m == null) return null;
		
		String
			value = m.group("value"),
			uValue = unescapeArgument(value);
		
		if(m.group("cq") == null) {
			if(!tabComplete || commandLine.length() > m.group().length()) return null;
			
			commandLine.cutStart(m.group().length());
			
			List<String> tabCompletions = new ArrayList<>();
			tabCompletions.add("\"");
			tabCompletions.addAll(getArgumentCompletions(defaultValues, value, true));
			return new ParserToken<>(tabCompletions);
		}
		
		commandLine.cutStart(m.group().length());
		
		return new ParserToken<String>(uValue);
	}
	
	private static String unescapeArgument(String arg) {
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
				case ' ':
				{
					u.append(' ');
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
	
	private static String escapeArgument(String arg, boolean quoted) {
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
				case ' ':
				{
					if(!quoted) {
						u.append("\\ ");
						break;
					}
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
	
	private static List<String> autoQuoteArguments(Collection<String> arguments) {
		return arguments.stream()
				.map(a -> a.contains(" ") ? "\"" + escapeArgument(a, true) + "\"" : escapeArgument(a, false))
				.collect(Collectors.toList());
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
//			str = str.trim();
			while(str.startsWith(" ")) {
				str = str.substring(1);
			}
			amountCut += os.length() - str.length();
			return this;
		}
		
		public boolean isEmpty() {
			return str.isEmpty();
		}
		
		public boolean isBlank() {
			return str.trim().isEmpty();
		}
		
		public boolean hasBlankContent() {
			return isBlank() && !isEmpty();
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
