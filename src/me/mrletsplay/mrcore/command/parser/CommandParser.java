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
import me.mrletsplay.mrcore.command.CommandOption;
import me.mrletsplay.mrcore.command.CommandParsingException;
import me.mrletsplay.mrcore.command.CommandProvider;
import me.mrletsplay.mrcore.command.ParsedCommand;
import me.mrletsplay.mrcore.command.parser.token.ParserToken;
import me.mrletsplay.mrcore.command.parser.token.SimpleToken;
import me.mrletsplay.mrcore.misc.NullableOptional;

public class CommandParser {

	private static final Pattern
		COMMAND_NAME_FORMAT = compile("[a-zA-Z-_0-9?]*"),
		SHORT_OPTION_FORMAT = compile("-(?<name>[a-zA-Z-_0-9]*)"),
		LONG_OPTION_FORMAT = compile("--(?<name>[a-zA-Z-_0-9]*)"),
		BASIC_ARGUMENT_FORMAT = compile("[^\n\r\t\" ]*"),
		ESCAPED_ARGUMENT_FORMAT = compile("\"(?<value>(?:\\\\[rnt\"]|[^\\r\\n\\t\"\\\\])*)(?<cq>\")?");
	
	private CommandParser() {}
	
	public static ParserToken<ParsedCommand> parseCommand(CommandProvider provider, MutableString commandLine, boolean tabComplete) {
		String origCommandLine = commandLine.toString();
		ParserToken<Command> cmd = readCommand(provider, commandLine, tabComplete);
		if(cmd == null) throw new CommandParsingException("Invalid command name", 0);
		
		if(!cmd.isComplete()) return new SimpleToken<>(cmd.getCompletions());
		
		Command c = cmd.getValue();
		
		while(!commandLine.isEmpty()) {
			ParserToken<Command> sct = readSubCommand(c, commandLine, tabComplete);
			if(sct == null) break; // End of subcommands
			if(!sct.isComplete()) return new SimpleToken<>(sct.getCompletions());
			c = sct.getValue();
		}
		
		Map<CommandOption<?>, Object> options = new HashMap<>();
		
		while(!commandLine.isEmpty()) {
			ParserToken<List<CommandOption<?>>> ops = readOption(c, commandLine, tabComplete);
			if(ops == null) break;
			if(!ops.isComplete()) return new SimpleToken<>(ops.getCompletions());
			for(CommandOption<?> op : ops.getValue()) {
				if(op.getType() == null) {
					options.put(op, null);
					continue;
				}
				ParserToken<String> v = readArgument(c, op.getType().getTabCompleteValues(), commandLine, tabComplete);
				if(v == null) throw new CommandParsingException("Invalid argument format", commandLine.getAmountCut());
				if(!v.isComplete()) return new SimpleToken<>(v.getCompletions());
				NullableOptional<?> pv = op.getType().parse(v.getValue());
				if(!pv.isPresent()) throw new CommandParsingException("Invalid option value for option \"" + op.getLongName() + "\"", commandLine.getAmountCut());
				options.put(op, pv.get());
			}
		}
		
		List<String> args = new ArrayList<>();
		
		while(!commandLine.isEmpty()) {
			ParserToken<String> arg = readArgument(c, Collections.emptyList(), commandLine, tabComplete);
			if(arg == null) throw new CommandParsingException("Invalid argument format", commandLine.getAmountCut());
			if(!arg.isComplete()) return new SimpleToken<>(arg.getCompletions());
			args.add(arg.getValue());
		}
		
		return new SimpleToken<ParsedCommand>(new ParsedCommand(c, origCommandLine, "lol", args.toArray(new String[args.size()]), options));
	}
	
	private static ParserToken<Command> readCommand(CommandProvider provider, MutableString commandLine, boolean tabComplete) {
		Matcher m = tryMatch(commandLine, COMMAND_NAME_FORMAT);
		if(m == null) return null;
		String cName = m.group();
		Command c = provider.getCommand(cName);
		
		if(c == null) {
			if(!tabComplete || commandLine.length() > m.group().length()) return null;
			
			commandLine.cutStart(m.group().length()).trim();
			
			return new SimpleToken<>(provider.getCommands().stream()
					.filter(cm -> cm.getName().toLowerCase().startsWith(cName.toLowerCase())
							|| cm.getAliases().stream().anyMatch(a -> a.toLowerCase().startsWith(cName.toLowerCase())))
					.map(Command::getName)
					.map(s -> s.substring(cName.length()))
					.collect(Collectors.toList())); // TODO alias etc
		}
		
		commandLine.cutStart(m.group().length()).trim();
		return new SimpleToken<>(c);
	}
	
	private static ParserToken<Command> readSubCommand(Command parent, MutableString commandLine, boolean tabComplete) {
		Matcher m = tryMatch(commandLine, COMMAND_NAME_FORMAT);
		if(m == null) return null;
		String cName = m.group();
		Command c = parent.getSubCommand(cName);
		
		if(c == null) {
			if(!tabComplete || commandLine.length() > m.group().length()) return null;
			
			commandLine.cutStart(m.group().length()).trim();
			
			return new SimpleToken<>(parent.getSubCommands().stream()
					.filter(cm -> cm.getName().toLowerCase().startsWith(cName.toLowerCase())
							|| cm.getAliases().stream().anyMatch(a -> a.toLowerCase().startsWith(cName.toLowerCase())))
					.map(Command::getName)
					.map(s -> s.substring(cName.length()))
					.collect(Collectors.toList())); // TODO alias etc
		}
		
		commandLine.cutStart(m.group().length()).trim();
		return new SimpleToken<>(c);
	}
	
	private static ParserToken<List<CommandOption<?>>> readOption(Command c, MutableString commandLine, boolean tabComplete) {
		ParserToken<CommandOption<?>> lo = readLongOption(c, commandLine, tabComplete);
		if(lo != null) return lo.map(Arrays::asList);
		
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
			
			return new SimpleToken<>(c.getOptions().stream()
					.filter(o -> o.getLongName().toLowerCase().startsWith(opName.toLowerCase()))
					.map(CommandOption::getLongName)
					.map(s -> s.substring(opName.length()))
					.collect(Collectors.toList()));
		}

		commandLine.cutStart(m.group().length()).trim();
		return new SimpleToken<>(op);
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
			
			return new SimpleToken<>(c.getOptions().stream()
					.map(CommandOption::getShortName)
					.collect(Collectors.toList()));
		}
		
		commandLine.cutStart(m.group().length()).trim();
		return new SimpleToken<>(op);
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
		
		if(tabComplete && commandLine.length() == m.group().length()) return new SimpleToken<>(defaultValues.stream()
				.filter(v -> v.startsWith(value))
				.map(s -> s.substring(value.length()))
				.collect(Collectors.toList()));
		
		commandLine.cutStart(m.group().length()).trim();
		
		return new SimpleToken<String>(value);
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
			return new SimpleToken<>(tabCompletions);
		}
		
		commandLine.cutStart(m.group().length()).trim();
		
		return new SimpleToken<String>(uValue);
	}
	
	private static String unescapeArg(String arg) {
		return arg
				.replace("\\n", "\n")
				.replace("\\r", "\r")
				.replace("\\t", "\t")
				.replaceAll("\\\\(?!\\\\)", "") // TODO: \\\\
				.replace("\\\\", "\\");
	}
	
	private static String escapeArg(String arg) {
		return arg
				.replace("\n", "\\n")
				.replace("\r", "\\r")
				.replace("\t", "\\t")
				.replace("\\", "\\\\");
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
