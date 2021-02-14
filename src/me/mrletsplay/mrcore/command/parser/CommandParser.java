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
import me.mrletsplay.mrcore.command.CommandSender;
import me.mrletsplay.mrcore.command.event.CommandTabCompleteEvent;
import me.mrletsplay.mrcore.command.event.filter.ArgumentFilterEvent;
import me.mrletsplay.mrcore.command.event.filter.CommandFilterEvent;
import me.mrletsplay.mrcore.command.event.filter.OptionArgumentFilterEvent;
import me.mrletsplay.mrcore.command.event.filter.OptionFilterEvent;
import me.mrletsplay.mrcore.command.option.CommandOption;
import me.mrletsplay.mrcore.command.provider.CommandProvider;
import me.mrletsplay.mrcore.misc.NullableOptional;

public class CommandParser {

	private static final Pattern
		COMMAND_NAME_FORMAT = compile("[a-zA-Z-_0-9?:./\\\\]*"),
		SHORT_OPTION_FORMAT = compile("-(?<name>[a-zA-Z-_0-9]+)(?= |$)"),
		LONG_OPTION_FORMAT = compile("--(?<name>[a-zA-Z-_0-9]*)(?= |$)"),
		BASIC_ARGUMENT_FORMAT = compile("(?<arg>(?:\\\\ |[^\n\r\t\" ])*)(?= |$)"),
		ESCAPED_ARGUMENT_FORMAT = compile("\"(?<value>(?:\\\\[rnt\"\\\\]|[^\\r\\n\\t\"\\\\])*)(?<cq>\")?(?= |$)");
	
	private CommandProvider provider;
	private CommandParsingProperties properties;
	
	public CommandParser(CommandProvider provider) {
		this.provider = provider;
		this.properties = new CommandParsingProperties();
	}
	
	public CommandParsingProperties getParsingProperties() {
		return properties;
	}
	
	public ParserToken<ParsedCommand> parse(CommandSender sender, String commandLine, boolean tabComplete) throws CommandParsingException {
		return parseCommand(sender, new MutableString(commandLine), tabComplete);
	}
	
	public ParsedCommand parseCommand(CommandSender sender, String commandLine) throws CommandParsingException {
		return parseCommand(sender, new MutableString(commandLine), false).getValue();
	}
	
	public List<String> tabComplete(CommandSender sender, String commandLine) throws CommandParsingException {
		ParserToken<ParsedCommand> token = parseCommand(sender, new MutableString(commandLine), true);
		if(token.isComplete()) return Collections.emptyList();
		return token.getCompletions();
	}
	
	@Deprecated
	public static ParserToken<ParsedCommand> parse(CommandProvider provider, CommandSender sender, String commandLine, boolean tabComplete) throws CommandParsingException {
		return provider.getCommandParser().parse(sender, commandLine, tabComplete);
	}
	
	@Deprecated
	public static ParsedCommand parseCommand(CommandProvider provider, CommandSender sender, String commandLine) throws CommandParsingException {
		return provider.getCommandParser().parseCommand(sender, commandLine);
	}
	
	@Deprecated
	public static List<String> tabComplete(CommandProvider provider, CommandSender sender, String commandLine) throws CommandParsingException {
		return provider.getCommandParser().tabComplete(sender, commandLine);
	}
	
	private ParserToken<ParsedCommand> parseCommand(CommandSender sender, MutableString commandLine, boolean tabComplete) {
		String origCommandLine = commandLine.toString();
		ParserToken<Command> cmd = readCommand(sender, commandLine, tabComplete);
		if(!commandLine.isBlank()) commandLine.trim();
		if(cmd == null) throw new CommandParsingException("Invalid command name", 0);
		
		if(!cmd.isComplete()) return new ParserToken<>(cmd.getCompletions());
		
		Command c = cmd.getValue();
		String label = cmd.getRaw();
		
		while(!commandLine.isBlank()) {
			ParserToken<Command> sct = readSubCommand(sender, c, label, commandLine, tabComplete);
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
					cs.addAll(autoQuoteCommandArguments(sender, c, c.getTabCompleter().tabComplete(new CommandTabCompleteEvent(sender, c, label, new String[0]))));
				}
				cs.addAll(getOptionCompletions(sender, c, "", true, true));
				return new ParserToken<>(cs);
			}
			
			ParserToken<List<CommandOption<?>>> ops = readOption(sender, c, label, commandLine, tabComplete);
			
			if(ops == null) break;
			if(!ops.isComplete()) return new ParserToken<>(ops.getCompletions());
			
			if(!commandLine.isBlank()) commandLine.trim();
			
			for(CommandOption<?> op : ops.getValue()) {
				if(op.getType() == null) {
					options.put(op, null);
					continue;
				}
				
				if(tabComplete && commandLine.hasBlankContent()) {
					return new ParserToken<>(autoQuoteOptionArguments(sender, c, op, op.getType().getTabCompleteValues()));
				}
				
				ParserToken<String> v = readArgument(sender, c, op.getType().getTabCompleteValues(), commandLine, tabComplete);
				
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
				cs = c.getTabCompleter().tabComplete(new CommandTabCompleteEvent(sender, c, label, args.toArray(new String[args.size()])));
			}
			ParserToken<String> arg = readArgument(sender, c, cs, commandLine, tabComplete);
			if(arg == null) throw new CommandParsingException("Invalid argument format", commandLine.getAmountCut());
			if(!arg.isComplete()) return new ParserToken<>(arg.getCompletions());
			args.add(arg.getValue());

			if(commandLine.isBlank()) break;
			commandLine.trim();
		}
		
		if(tabComplete && commandLine.hasBlankContent()) {
			return new ParserToken<>(c.getTabCompleter() != null ? autoQuoteCommandArguments(sender, c, c.getTabCompleter().tabComplete(new CommandTabCompleteEvent(sender, c, label, args.toArray(new String[args.size()])))) : Collections.emptyList());
		}
		
		return new ParserToken<>(new ParsedCommand(c, origCommandLine, label, args.toArray(new String[args.size()]), options));
	}
	
	private ParserToken<Command> readCommand(CommandSender sender, MutableString commandLine, boolean tabComplete) {
		Matcher m = tryMatch(commandLine, COMMAND_NAME_FORMAT);
		if(m == null) return null;
		String cName = m.group();
		Command c = provider.getCommand(cName, properties.isCaseSensitive());
		
		if(c == null) {
			if(!tabComplete || commandLine.length() > m.group().length()) return null;
			
			commandLine.cutStart(m.group().length());
			
			return new ParserToken<>(getCommandCompletions(sender, provider.getCommands(), cName));
		}
		
		commandLine.cutStart(m.group().length());
		
		if(commandLine.hasBlankContent() && tabComplete) {
			List<String> cs = new ArrayList<>();
			cs.addAll(getCommandCompletions(sender, c.getSubCommands(), ""));
			cs.addAll(getOptionCompletions(sender, c, "", true, true));
			if(c.getTabCompleter() != null) cs.addAll(autoQuoteCommandArguments(sender, c, c.getTabCompleter().tabComplete(new CommandTabCompleteEvent(sender, c, cName, new String[0]))));
			return new ParserToken<>(cs);
		}
		
		return new ParserToken<>(c, cName);
	}
	
	private List<String> getCommandCompletions(CommandSender sender, Collection<? extends Command> cmds, String cName) {
		List<String> cs = new ArrayList<>();
		for(Command cmd : cmds) {
			if(!properties.getTabCompleteCommandFilter().test(new CommandFilterEvent(sender, cmd))) continue;
			
			if(cmd.getName().toLowerCase().startsWith(cName.toLowerCase())) {
				cs.add(cmd.getName().substring(cName.length()));
			}
			
			cmd.getAliases().stream()
				.filter(a -> a.toLowerCase().startsWith(cName.toLowerCase()))
				.forEach(a -> cs.add(a.substring(cName.length())));
		}
		return cs;
	}
	
	private List<String> getOptionCompletions(CommandSender sender, Command cmd, String optionName, boolean longOptions, boolean prefix) {
		if(!longOptions) return cmd.getOptions().stream()
				.filter(o -> properties.getTabCompleteOptionFilter().test(new OptionFilterEvent(sender, o, cmd)))
				.map(CommandOption::getShortName)
				.filter(Objects::nonNull)
				.map(o -> prefix ? "-" + o : o)
				.collect(Collectors.toList());
		
		return cmd.getOptions().stream()
				.filter(o -> properties.getTabCompleteOptionFilter().test(new OptionFilterEvent(sender, o, cmd)))
				.map(CommandOption::getLongName)
				.filter(c -> c.toLowerCase().startsWith(optionName.toLowerCase()))
				.map(c -> c.substring(optionName.length()))
				.map(o -> prefix ? "--" + o : o)
				.collect(Collectors.toList());
	}
	
	private List<String> getArgumentCompletions(CommandSender sender, Command command, Collection<String> completions, String rawArgument, boolean quoted) {
		return completions.stream()
				.filter(a -> properties.getTabCompleteArgumentFilter().test(new ArgumentFilterEvent(sender, a, command)))
				.map(o -> quoted ? escapeArgument(o, true) + "\"" : escapeArgument(o, false))
				.filter(c -> c.toLowerCase().startsWith(rawArgument.toLowerCase()))
				.map(c -> c.substring(rawArgument.length()))
				.collect(Collectors.toList());
	}
	
	private ParserToken<Command> readSubCommand(CommandSender sender, Command parent, String label, MutableString commandLine, boolean tabComplete) {
		Matcher m = tryMatch(commandLine, COMMAND_NAME_FORMAT);
		if(m == null) return null;
		String cName = m.group();
		Command c = parent.getSubCommand(cName, properties.isCaseSensitive());
		
		if(c == null) {
			if(!tabComplete || commandLine.length() > m.group().length()) return null;
			
			commandLine.cutStart(m.group().length());
			
			List<String> cs = new ArrayList<>();
			cs.addAll(getCommandCompletions(sender, parent.getSubCommands(), cName));
			
			if(parent.getTabCompleter() != null) {
				cs.addAll(getArgumentCompletions(sender, parent, parent.getTabCompleter().tabComplete(new CommandTabCompleteEvent(sender, c, label, new String[0])), cName, false));
			}
			
			if(cName.startsWith("-")) {
				boolean isLong = cName.startsWith("--");
				cs.addAll(getOptionCompletions(sender, parent, cName.substring(isLong ? 2 : 1), isLong, false));
			}
			
			return new ParserToken<>(cs);
		}
		
		commandLine.cutStart(m.group().length());
		
		if(commandLine.toString().equals(" ") && tabComplete) {
			List<String> cs = new ArrayList<>();
			cs.addAll(getCommandCompletions(sender, c.getSubCommands(), ""));
			cs.addAll(getOptionCompletions(sender, c, "", true, true));
			if(c.getTabCompleter() != null) cs.addAll(autoQuoteCommandArguments(sender, c, c.getTabCompleter().tabComplete(new CommandTabCompleteEvent(sender, c, cName, new String[0]))));
			return new ParserToken<>(cs);
		}
		
		return new ParserToken<>(c, cName);
	}
	
	private ParserToken<List<CommandOption<?>>> readOption(CommandSender sender, Command c, String label, MutableString commandLine, boolean tabComplete) {
		ParserToken<CommandOption<?>> lo = readLongOption(sender, c, label, commandLine, tabComplete);
		if(lo != null) return lo.map(Collections::singletonList);
		
		return readShortOption(sender, c, label, commandLine, tabComplete);
	}
	
	private ParserToken<CommandOption<?>> readLongOption(CommandSender sender, Command c, String label, MutableString commandLine, boolean tabComplete) {
		Matcher m = tryMatch(commandLine, LONG_OPTION_FORMAT);
		if(m == null) return null;
		String opName = m.group("name");
		CommandOption<?> op = c.getLongOption(opName);
		
		if(op == null) {
			if(!tabComplete || commandLine.length() > m.group().length()) return null;
			
			commandLine.cutStart(m.group().length());
			
			return new ParserToken<>(getOptionCompletions(sender, c, opName, true, false));
		}
		
		commandLine.cutStart(m.group().length());
		
		if(commandLine.hasBlankContent() && tabComplete) {
			if(op.getType() == null) {
				List<String> cs = new ArrayList<>();
				if(c.getTabCompleter() != null) {
					cs.addAll(autoQuoteCommandArguments(sender, c, c.getTabCompleter().tabComplete(new CommandTabCompleteEvent(sender, c, label, new String[0]))));
				}
				cs.addAll(getOptionCompletions(sender, c, "", true, true));
				return new ParserToken<>(cs);
			}
			return new ParserToken<>(autoQuoteOptionArguments(sender, c, op, op.getType().getTabCompleteValues()));
		}
		
		return new ParserToken<>(op);
	}
	
	private ParserToken<List<CommandOption<?>>> readShortOption(CommandSender sender, Command c, String label, MutableString commandLine, boolean tabComplete) {
		Matcher m = tryMatch(commandLine, SHORT_OPTION_FORMAT);
		if(m == null) return null;
		
		String opName = m.group("name");
		List<CommandOption<?>> op = Arrays.stream(opName.split(""))
				.filter(s -> !s.isEmpty())
				.map(c::getShortOption)
				.collect(Collectors.toList());

		if(op.stream().anyMatch(Objects::isNull)) return null;
		
		if(tabComplete && commandLine.length() == m.group().length()) {
			return new ParserToken<>(getOptionCompletions(sender, c, "", false, true));
		}
		
		commandLine.cutStart(m.group().length());
		
		if(commandLine.hasBlankContent() && tabComplete) {
			CommandOption<?> fOp = op.stream().filter(o -> o.getType() != null).findFirst().orElse(null);
			if(fOp == null) {
				List<String> cs = new ArrayList<>();
				if(c.getTabCompleter() != null) {
					cs.addAll(autoQuoteCommandArguments(sender, c, c.getTabCompleter().tabComplete(new CommandTabCompleteEvent(sender, c, label, new String[0]))));
				}
				cs.addAll(getOptionCompletions(sender, c, "", true, true));
				return new ParserToken<>(cs);
			}
			return new ParserToken<>(autoQuoteOptionArguments(sender, c, fOp, fOp.getType().getTabCompleteValues()));
		}
		
		return new ParserToken<>(op);
	}
	
	private ParserToken<String> readArgument(CommandSender sender, Command c, Collection<String> defaultValues, MutableString commandLine, boolean tabComplete) {
		ParserToken<String> arg = readBasicArgument(sender, c, defaultValues, commandLine, tabComplete);
		if(arg != null) return arg;
		
		return readEscapedArgument(sender, c, defaultValues, commandLine, tabComplete);
	}
	
	private ParserToken<String> readBasicArgument(CommandSender sender, Command c, Collection<String> defaultValues, MutableString commandLine, boolean tabComplete) {
		Matcher m = tryMatch(commandLine, BASIC_ARGUMENT_FORMAT);
		if(m == null) return null;
		String value = m.group("arg");
		
		if(value.isEmpty() && (!tabComplete || commandLine.length() > m.group().length())) return null;
		
		if(tabComplete && commandLine.length() == m.group().length()) return new ParserToken<>(getArgumentCompletions(sender, c, defaultValues, value, false));
		
		commandLine.cutStart(m.group().length());
		
		return new ParserToken<String>(value);
	}
	
	private ParserToken<String> readEscapedArgument(CommandSender sender, Command c, Collection<String> defaultValues, MutableString commandLine, boolean tabComplete) {
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
			tabCompletions.addAll(getArgumentCompletions(sender, c, defaultValues, value, true));
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
	
	private List<String> autoQuoteCommandArguments(CommandSender sender, Command command, Collection<String> arguments) {
		return arguments.stream()
				.filter(a -> properties.getTabCompleteArgumentFilter().test(new ArgumentFilterEvent(sender, a, command)))
				.map(a -> a.contains(" ") ? "\"" + escapeArgument(a, true) + "\"" : escapeArgument(a, false))
				.collect(Collectors.toList());
	}
	
	private List<String> autoQuoteOptionArguments(CommandSender sender, Command command, CommandOption<?> option, Collection<String> arguments) {
		return arguments.stream()
				.filter(a -> properties.getTabCompleteOptionArgumentFilter().test(new OptionArgumentFilterEvent(sender, a, command, option)))
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
