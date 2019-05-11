package me.mrletsplay.mrcore.command.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import me.mrletsplay.mrcore.command.CommandValueType;
import me.mrletsplay.mrcore.misc.MiscUtils;
import me.mrletsplay.mrcore.misc.NullableOptional;

public final class DefaultCommandValueType {

	public static final CommandValueType<Integer> INTEGER = new Impl<Integer>(s -> MiscUtils.callSafely(() -> Integer.parseInt(s)), "-1");

	private DefaultCommandValueType() {}
	
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
