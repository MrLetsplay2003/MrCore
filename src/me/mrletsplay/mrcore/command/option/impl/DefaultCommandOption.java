package me.mrletsplay.mrcore.command.option.impl;

import me.mrletsplay.mrcore.command.CommandValueType;
import me.mrletsplay.mrcore.command.option.CommandOption;

public final class DefaultCommandOption {

	public static final CommandOption<?> HELP = createCommandOption("h", "help");
	
	private DefaultCommandOption() {}
	
	public static CommandOption<?> createCommandOption(String shortName, String longName) throws IllegalArgumentException {
		if(shortName.length() != 1) throw new IllegalArgumentException("shortName needs to be 1 character long");
		return new Impl<>(shortName, longName, false, null);
	}
	
	public static <T> CommandOption<T> createCommandOption(String shortName, String longName, CommandValueType<T> type) throws IllegalArgumentException {
		if(shortName.length() != 1) throw new IllegalArgumentException("shortName needs to be 1 character long");
		return new Impl<>(shortName, longName, true, type);
	}
	
	public static final class Impl<T> implements CommandOption<T>{

		private String sn, ln;
		private boolean v;
		private CommandValueType<T> t;
		
		public Impl(String sn, String ln, boolean v, CommandValueType<T> t) {
			this.sn = sn;
			this.ln = ln;
			this.v = v;
			this.t = t;
		}
		
		@Override
		public String getShortName() {
			return sn;
		}

		@Override
		public String getLongName() {
			return ln;
		}

		@Override
		public boolean needsValue() {
			return v;
		}

		@Override
		public CommandValueType<T> getType() {
			return t;
		}
		
		@Override
		public String toString() {
			return "--" + ln;
		}
		
	}
	
}
