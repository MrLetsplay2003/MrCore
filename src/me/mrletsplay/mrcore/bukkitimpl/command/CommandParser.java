package me.mrletsplay.mrcore.bukkitimpl.command;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.mrletsplay.mrcore.misc.ErroringNullableOptional;
import me.mrletsplay.mrcore.misc.NullableOptional;

public class CommandParser {
	
	private static final Pattern
		COMMAND_ARGS_PATTERN = Pattern.compile("(?<result>(?:-(?<fname>[a-zA-Z]+?)(?:(?<op>[ =])(?<fvaluer>\\\"(?<fvalueq>(?:\\\\[\\\\\"]|[^\\\\])+?)\\\"|(?<fvalueuq>(?:[^ \"-]|(?<! )-)[^ \"]*?))|))|(?<argr>\\\"(?<argq>(?:\\\\[\\\\\"]|[^\\\\])+?)\\\"|(?<arguq>(?:[^ \"-][^ \"]*?)|-\\d+?)))(?:$| )");

	public static ErroringNullableOptional<ParsedCommand, CommandParsingException> parse(EasyCommand command, String label, String rawArgs) {
		List<ParsedCommandFlag<?>> flags = new ArrayList<>();
		List<CommandArgument> args = new ArrayList<>();
		Matcher m = COMMAND_ARGS_PATTERN.matcher(rawArgs);
		int lastEnd = 0;
		while(m.lookingAt()) {
			lastEnd = m.end();
			if(m.group("fname") != null) { // Flag
				String fName = m.group("fname");
				String fValue = m.group("fvalueq") != null ? unescapeQuoted(m.group("fvalueq")) : m.group("fvalueuq");
				String op = m.group("op");
				CommandFlag<?> flag = command.getRegisteredFlag(fName);
				ErroringNullableOptional<ParsedCommandFlag<?>, CommandParsingException> r = createParsedFlag(command, flags, flag, m.regionStart(), fName, fValue, m.group("fvaluer"), op);
				if(!r.isPresent()) return ErroringNullableOptional.ofErroring(r.getException());
				if(!flag.needsValue() && fValue != null && op.equals(" ")) lastEnd -= fValue.length() + 1;
				flags.add(r.get());
			}else { // Normal arg
				String arg = m.group("argq") != null ? unescapeQuoted(m.group("argq")) : m.group("arguq");
				args.add(new CommandArgument(arg));
			}
			m = m.region(lastEnd, rawArgs.length());
		}
		if(lastEnd != rawArgs.length()) {
			return ErroringNullableOptional.ofErroring(new CommandParsingException("Invalid input format", lastEnd, rawArgs.length() - lastEnd));
		}
		return ErroringNullableOptional.ofErroring(new ParsedCommand(command, label, args.toArray(new CommandArgument[args.size()]), flags));
	}
	
	private static String unescapeQuoted(String str) {
		return str.replace("\\\"", "\"").replace("\\\\", "\\");
	}
	
	private static <T> ErroringNullableOptional<ParsedCommandFlag<?>, CommandParsingException> createParsedFlag(EasyCommand command, List<ParsedCommandFlag<?>> setFlags, CommandFlag<T> flag, int flagIndex, String flagName, String flagValue, String rFlagValue, String op) {
		if(flag == null) return ErroringNullableOptional.ofErroring(new CommandParsingException("A flag with the name \"" + flagName + "\" doesn't exist", flagIndex, flagName.length() + 1));
		if(flag.needsValue()) {
			if(flagValue == null) return ErroringNullableOptional.ofErroring(new CommandParsingException("The flag \"" + flagName + "\" needs a value", flagIndex, flagName.length() + 1));
			if(setFlags.stream().anyMatch(f -> f.getFlag().getName().equals(flagName))) return ErroringNullableOptional.ofErroring(new CommandParsingException("Flag \"" + flagName + "\" already set", flagIndex, flagName.length() + 1));
			NullableOptional<T> val = flag.getValueType().parse(flagValue);
			if(!val.isPresent()) return ErroringNullableOptional.ofErroring(new CommandParsingException("Invalid value type for flag \"" + flagName + "\", should be " + flag.getValueType().getFriendlyName(), flagIndex, flagName.length() + rFlagValue.length() + 2));
			return ErroringNullableOptional.ofErroring(new ParsedCommandFlag<>(flag, val.get()));
		}else {
			if(op != null && op.equals("=") && flagValue != null) return ErroringNullableOptional.ofErroring(new CommandParsingException("Flag \"" + flagName + "\" doesn't need a value", flagIndex, flagName.length() + rFlagValue.length() + 2));
			return ErroringNullableOptional.ofErroring(new ParsedCommandFlag<>(flag, null));
		}
	}
	
}
