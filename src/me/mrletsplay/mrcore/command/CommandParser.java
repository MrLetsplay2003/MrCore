package me.mrletsplay.mrcore.command;

import static java.util.regex.Pattern.compile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.misc.DualNullableOptional;
import me.mrletsplay.mrcore.misc.NullableOptional;

@Deprecated
public class CommandParser {
	
	private static final Pattern
		COMMAND_NAME_FORMAT = compile("(?<name>[a-zA-Z-_0-9?]*)"),
		SHORT_OPTION_FORMAT = compile("(?<soption>-(?<name>[a-zA-Z-_0-9]*))"),
		LONG_OPTION_FORMAT = compile("(?<loption>--(?<name>[a-zA-Z-_0-9]*))"),
		ARGUMENT_FORMAT = compile("(?<arg>\"(?<avq>[^\\n\\r\\t\"]*)(?<cq>\")?|(?<av>(?:[^\\n\\r\\t\"\\\\ ]|\\\\[^\\n\\r\\t])+))");
	
	public static ParsedCommand parse(CommandProvider provider, String commandLine) throws CommandParsingException {
		return parse0(provider, commandLine, false).get();
	}
	
	public static List<String> tabComplete(CommandProvider provider, String commandLine) throws CommandParsingException {
		DualNullableOptional<ParsedCommand, List<String>> p = parse0(provider, commandLine, true);
		return p.isPresent() ? new ArrayList<>() : p.getOther();
	}
	
	private static DualNullableOptional<ParsedCommand, List<String>> parse0(CommandProvider provider, String commandLine, boolean tabComplete) throws CommandParsingException {
		MutableString tmpL = new MutableString(commandLine);
		Matcher nr = tryMatch(tmpL, COMMAND_NAME_FORMAT);
		if(nr == null) throw new CommandParsingException("Invalid command name");
		String lbl = nr.group("name");
		Command c = provider.getCommand(lbl);
		tmpL.cutStart(nr.group().length()).trim();
		if(c == null) {
			if(!tabComplete) throw new CommandParsingException("Invalid command");
			if(!tmpL.isEmpty()) return DualNullableOptional.ofDualAbsent(new ArrayList<>());
			return DualNullableOptional.ofDualAbsent(provider.getCommands().stream()
					.filter(cm -> cm.getName().startsWith(lbl) || cm.getAliases().stream().anyMatch(a -> a.startsWith(lbl)))
					.map(cm -> cm.getName().substring(lbl.length())) // FIXME: correct label: ioobexc
					.collect(Collectors.toList()));
		}
		boolean isArg = false;
		List<String> args = new ArrayList<>();
		Map<CommandOption<?>, Object> opts = new LinkedHashMap<>();
		while(tmpL.length() > 0) {
			if(!isArg) {
				DualNullableOptional<Map<CommandOption<?>, Object>, List<String>> opt = tryGetOption(c, tmpL, tabComplete);
				if(!opt.isPresent()) return DualNullableOptional.ofDualAbsent(opt.getOther());
				Map<CommandOption<?>, Object> m = opt.get();
				if(m != null) {
					opts.putAll(m);
					continue;
				}else {
					isArg = true;
				}
			}
			DualNullableOptional<String, List<String>> a = tryGetArg(c, tmpL, tabComplete);
			if(!a.isPresent()) {
				if(!tabComplete) throw new CommandParsingException("Invalid arg");
				return DualNullableOptional.ofDualAbsent(a.getOther());
			}
			args.add(a.get());
		}
		return DualNullableOptional.ofDual(new ParsedCommand(c, commandLine, lbl, args.stream().toArray(String[]::new), opts));
	}
	
	private static DualNullableOptional<Map<CommandOption<?>, Object>, List<String>> tryGetOption(Command c, MutableString tmpL, boolean tabComplete) {
		Map<CommandOption<?>, Object> om = new LinkedHashMap<>();
		MutableString tmpStr = tmpL.copy();
		Matcher lom = tryMatch(tmpStr.toString(), LONG_OPTION_FORMAT);
		if(lom != null) {
			tmpStr.cutStart(lom.group().length()).trim();
			String nm = lom.group("name");
			CommandOption<?> o = c.getLongOption(nm);
			if(o == null) {
				if(!tabComplete || !tmpStr.isEmpty()) return DualNullableOptional.ofDual(null);
				return DualNullableOptional.ofDualAbsent(c.getOptions().stream()
						.filter(op -> op.getLongName().startsWith(nm))
						.map(CommandOption::getLongName)
						.map(n -> n.substring(nm.length()))
						.collect(Collectors.toList()));
			}
			if(o.needsValue()) {
				DualNullableOptional<String, List<String>> v = tryGetArg(c, tmpStr, tabComplete);
				if(!v.isPresent()) return DualNullableOptional.ofDualAbsent(v.getOther());
				String val = v.get();
				if(val == null) return DualNullableOptional.ofDualAbsent(new ArrayList<>());
				NullableOptional<?> vl = o.getType().parse(val);
				if(!vl.isPresent()) {
					if(!tabComplete || !tmpStr.isEmpty()) return DualNullableOptional.ofDual(null);
					return DualNullableOptional.ofDualAbsent(o.getType().getTabCompleteValues().stream()
							.filter(t -> t.startsWith(val))
							.map(t -> t.substring(val.length()))
							.collect(Collectors.toList()));
				}
				om.put(o, vl.get());
			}else {
				om.put(o, null);
			}
			tmpL.setString(tmpStr.toString());
			return DualNullableOptional.ofDual(om);
		}

		Matcher som = tryMatch(tmpStr.toString(), SHORT_OPTION_FORMAT);
		if(som != null) {
			tmpStr.cutStart(som.group().length()).trim();
			String[] ops = som.group("name").split("");
			for(String opt : ops) {
				CommandOption<?> o = c.getShortOption(opt);
				if(o == null) {
					if(!tabComplete || !tmpStr.isEmpty()) return DualNullableOptional.ofDual(null);
					return DualNullableOptional.ofDualAbsent(c.getOptions().stream()
							.map(CommandOption::getShortName)
							.collect(Collectors.toList()));
				}
				if(o.needsValue()) {
					DualNullableOptional<String, List<String>> v = tryGetArg(c, tmpStr, tabComplete);
					if(!v.isPresent()) return DualNullableOptional.ofDualAbsent(v.getOther());
					String val = v.get();
					NullableOptional<?> vl = o.getType().parse(val);
					if(!vl.isPresent()) {
						if(!tabComplete || !tmpStr.isEmpty()) return DualNullableOptional.ofDual(null);
						return DualNullableOptional.ofDualAbsent(o.getType().getTabCompleteValues().stream()
								.filter(t -> t.startsWith(val))
								.map(t -> t.substring(val.length()))
								.collect(Collectors.toList()));
					}
					om.put(o, vl.get());
				}else {
					om.put(o, null);
				}
			}
			tmpL.setString(tmpStr.toString());
			return DualNullableOptional.ofDual(om);
		}
		
		return DualNullableOptional.ofDual(null);
	}
	
	private static DualNullableOptional<String, List<String>> tryGetArg(Command c, MutableString tmpL, boolean tabComplete) {
		Matcher am = tryMatch(tmpL.toString(), ARGUMENT_FORMAT);
		if(am != null) {
			tmpL.cutStart(am.group().length()).trim();
			if(am.group("avq") != null && am.group("cq") == null) {
				if(!tabComplete) throw new CommandParsingException("Missing end quote");
				if(!tmpL.isEmpty()) return DualNullableOptional.ofDualAbsent(new ArrayList<>());
				return DualNullableOptional.ofDualAbsent(Arrays.asList("\""));
			}
			return DualNullableOptional.ofDual(unescapeArg(am.group("avq") != null ? am.group("avq") : am.group("av")));
		}
//		if(tabComplete && tmpL.trim().isEmpty()) return DualNullableOptional.ofDual("");
		return DualNullableOptional.ofDual(null);
	}
	
	private static String unescapeArg(String arg) {
		return arg
				.replace("\\n", "\n")
				.replace("\\r", "\\r")
				.replace("\\t", "\t")
				.replaceAll("\\\\(?!\\\\)", "") // TODO: \\\\
				.replace("\\\\", "\\");
	}
	
	private static Matcher tryMatch(CharSequence s, Pattern p) {
		Matcher m = p.matcher(s);
		return m.lookingAt() ? m : null;
	}
	
	private static class MutableString implements CharSequence {
		
		private String str;
		
		public MutableString(String str) {
			this.str = str;
		}
		
		public MutableString cutStart(int len) {
			this.str = str.substring(len);
			return this;
		}
		
		public void setString(String str) {
			this.str = str;
		}
		
		public MutableString copy() {
			return new MutableString(str);
		}
		
		public MutableString trim() {
			str = str.trim();
			return this;
		}
		
		public boolean isEmpty() {
			return str.isEmpty();
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
