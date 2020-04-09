package me.mrletsplay.mrcore.command.option.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import me.mrletsplay.mrcore.command.CommandValueType;
import me.mrletsplay.mrcore.misc.MiscUtils;
import me.mrletsplay.mrcore.misc.NullableOptional;

public final class DefaultCommandValueType {

	public static final CommandValueType<Integer> INTEGER = integer(0, 1);
	
	public static final CommandValueType<Boolean> BOOLEAN = new Impl<>(s -> {
		if(s == null) return NullableOptional.empty();
		switch(s.toLowerCase()) {
			case "true":
				return NullableOptional.of(true);
			case "false":
				return NullableOptional.of(false);
			default:
				return NullableOptional.empty();
		}
	}, "true", "false");
	
	public static final CommandValueType<String> STRING = string("hello world");

	private DefaultCommandValueType() {}
	
	public static CommandValueType<Integer> integer(int... completions) {
		List<String> comps = new ArrayList<>();
		Arrays.stream(completions).forEach(i -> comps.add(String.valueOf(i)));
		return new Impl<>(s -> MiscUtils.callSafely(() -> Integer.parseInt(s)), comps.toArray(new String[comps.size()]));
	}
	
	public static CommandValueType<String> string(String... completions) {
		return new Impl<>(NullableOptional::of, completions);
	}
	
	private static class Impl<T> implements CommandValueType<T> {

		private Function<String, NullableOptional<T>> p;
		private List<String> tcv;
		
		public Impl(Function<String, NullableOptional<T>> p, String... tcv) {
			this.p = p;
			this.tcv = Arrays.asList(tcv);
		}
		
		@Override
		public NullableOptional<T> parse(String value) {
			return p.apply(value);
		}

		@Override
		public Collection<String> getTabCompleteValues() {
			return tcv;
		}
		
	}
	
}
