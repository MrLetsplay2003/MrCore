package me.mrletsplay.mrcore.bukkitimpl.command;

import me.mrletsplay.mrcore.misc.MiscUtils;
import me.mrletsplay.mrcore.misc.NullableOptional;

public class DefaultCommandValueType {

	public static final CommandValueType<String> STRING = CommandValueType.of("String", s -> NullableOptional.of(s));
	public static final CommandValueType<Integer> INT = CommandValueType.of("Integer", s -> MiscUtils.callSafely(() -> Integer.parseInt(s)));
	
}
