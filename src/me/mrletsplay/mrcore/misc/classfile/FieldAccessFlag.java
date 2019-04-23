package me.mrletsplay.mrcore.misc.classfile;

import me.mrletsplay.mrcore.misc.FlagCompound.Flag;

public enum FieldAccessFlag implements Flag {

	PUBLIC("public", 0x1),
	PRIVATE("private", 0x2),
	PROTECTED("protected", 0x4),
	STATIC("static", 0x8),
	FINAL("final", 0x10),
	VOLATILE("volatile", 0x40),
	TRANSIENT("transient", 0x80),
	SYNTHETIC("synthetic", 0x1000),
	ENUM("enum", 0x4000);
	
	private final String name;
	private final long value;
	
	private FieldAccessFlag(String name, long value) {
		this.name = name;
		this.value = value;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public long getValue() {
		return value;
	}
	
}
