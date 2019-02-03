package me.mrletsplay.mrcore.bukkitimpl.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.mrletsplay.mrcore.bukkitimpl.command.completion.CommandArgumentElement;
import me.mrletsplay.mrcore.bukkitimpl.command.completion.CommandElement;
import me.mrletsplay.mrcore.bukkitimpl.command.completion.CommandFlagElement;
import me.mrletsplay.mrcore.bukkitimpl.command.completion.CommandFlagValueElement;
import me.mrletsplay.mrcore.misc.ErroringNullableOptional;
import me.mrletsplay.mrcore.misc.MiscUtils;
import me.mrletsplay.mrcore.misc.NullableOptional;

public class CommandParser {
	
	private static final Pattern
		COMMAND_ARGS_PATTERN = Pattern.compile("(?<result>(?:-(?<fname>[a-zA-Z]*?)(?:(?<op>[ =])(?<fvaluer>\\\"(?<fvalueq>(?:\\\\[\\\\\"]|[^\\\\\"])++)?\\\"?|(?<fvalueuq>(?:[^ \"-]|(?<! )-)[^ \"]*?)|(?<==))|))|(?<argr>\\\"(?<argq>(?:\\\\[\\\\\"]|[^\\\\\"])++)?\\\"?|(?<arguq>(?:[^ \"-][^ \"]*?)|-\\d+?(?:\\.?\\d*?|))))(?<del>$| )");

	public static ErroringNullableOptional<Map.Entry<ParsedCommand, CommandElement>, CommandParsingException> parse(EasyCommand command, String label, String rawArgs, boolean isTabComplete) {
		List<ParsedCommandFlag<?>> flags = new ArrayList<>();
		List<CommandArgument> args = new ArrayList<>();
		Matcher m = COMMAND_ARGS_PATTERN.matcher(rawArgs);
		int lastEnd = 0;
		CommandElement element = new CommandArgumentElement("", 0, false, false);
		while(m.lookingAt()) {
			lastEnd = m.end();
			String del = m.group("del");
			if(m.group("fname") != null) { // Flag
				String fName = m.group("fname");
				String fValue = m.group("fvalueq") != null ? unescapeQuoted(m.group("fvalueq")) : m.group("fvalueuq");
				String op = m.group("op");
				if(fName.isEmpty()) {
					if(op != null || !isTabComplete) return ErroringNullableOptional.ofErroring(new CommandParsingException("Flag must have a name", m.regionStart(), 1));
					if(m.start() + 1 == rawArgs.length() && !del.equals(" ")) {
						element = new CommandFlagElement("");
						break;
					}
				}
				CommandFlag<?> flag = command.getRegisteredFlag(fName);
				if(flag == null) {
					if(isTabComplete && (m.start() + fName.length() + 1 == rawArgs.length()) && !del.equals(" ")) {
						element = new CommandFlagElement(fName);
						break;
					}
					return ErroringNullableOptional.ofErroring(new CommandParsingException("A flag with the name \"" + fName + "\" doesn't exist", m.start(), fName.length() + 1));
				}
				if(flags.stream().anyMatch(f -> f.getFlag().getName().equals(fName))) return ErroringNullableOptional.ofErroring(new CommandParsingException("Flag \"" + fName + "\" already set", m.start(), fName.length() + 1));
				boolean needsClosingQuote = m.group("fvalueq") != null && !m.group("fvaluer").endsWith("\"");
				if(flag.needsValue()) {
					if(fValue == null) {
						if(isTabComplete && m.end() == rawArgs.length()) {
							if(op == null && del.equals(" ")) {
								element = new CommandFlagValueElement("", flag, " ", m.group("fvalueq") != null, needsClosingQuote);
								break;
							}else if(!del.equals(" ") && op == null) {
								element = new CommandFlagElement(fName);
								break;
							}else {
								element = new CommandFlagValueElement("", flag, op, m.group("fvalueq") != null, needsClosingQuote);
								break;
							}
						}
						return ErroringNullableOptional.ofErroring(new CommandParsingException("The flag \"" + fName + "\" needs a value", m.start(), fName.length() + 1));
					}
					if(isTabComplete && lastEnd == rawArgs.length() && !del.equals(" ")) {
						element = new CommandFlagValueElement(fValue, flag, op, m.group("fvalueq") != null, needsClosingQuote);
						break;
					}
					if(needsClosingQuote) return ErroringNullableOptional.ofErroring(new CommandParsingException("Missing end quote of string", m.start() + fName.length() + 1, m.group("fvaluer").length() + 1));
					NullableOptional<?> val = flag.getValueType().parse(fValue);
					if(!val.isPresent()) return ErroringNullableOptional.ofErroring(new CommandParsingException("Invalid value type for flag \"" + fName + "\", should be " + flag.getValueType().getFriendlyName(), m.start() + fName.length() + 2, m.group("fvaluer").length()));
					flags.add(new ParsedCommandFlag<>(flag, fValue));
				}else {
					if(op != null && op.equals("=") && fValue != null) return ErroringNullableOptional.ofErroring(new CommandParsingException("Flag \"" + fName + "\" doesn't need a value", m.start() + fName.length() + 2, m.group("fvaluer").length()));
					if(isTabComplete && m.end() == rawArgs.length()) {
						if(op == null && !del.equals(" ")) {
							element = new CommandFlagElement(fName);
							break;
						}
					}
					flags.add(new ParsedCommandFlag<>(flag, null));
				}
				if(!flag.needsValue() && fValue != null && op.equals(" ")) lastEnd -= fValue.length();
			}else { // Normal arg
				String unstripped = m.group("argr");
				String arg = m.group("argq") != null ? unescapeQuoted(m.group("argq")) : m.group("arguq");
				if(arg == null) return ErroringNullableOptional.ofErroring(new CommandParsingException("Empty argument", m.start(), m.end() - m.start()));
				boolean needsClosingQuote = m.group("argq") != null && !m.group("argr").endsWith("\"");
				if(isTabComplete && lastEnd == rawArgs.length()) {
					if(m.group("del").equals(" ")) {
						element = new CommandArgumentElement("", args.size() + 1, false, false);
						break;
					}else {
						element = new CommandArgumentElement(arg, args.size(), needsClosingQuote, m.group("argq") != null);
						break;
					}
				}
				if(needsClosingQuote) return ErroringNullableOptional.ofErroring(new CommandParsingException("Missing end quote of string", m.start(), m.group("argr").length()));
				args.add(new CommandArgument(unstripped, arg));
			}
			m = m.region(lastEnd, rawArgs.length());
		}
		if(lastEnd != rawArgs.length()) {
			return ErroringNullableOptional.ofErroring(new CommandParsingException("Invalid input format", lastEnd, rawArgs.length() - lastEnd));
		}
		return ErroringNullableOptional.ofErroring(MiscUtils.newMapEntry(new ParsedCommand(command, label + " " + rawArgs, label, args.toArray(new CommandArgument[args.size()]), flags), element));
	}
	
	private static String unescapeQuoted(String str) {
		return str.replace("\\\"", "\"").replace("\\\\", "\\");
	}
	
}
