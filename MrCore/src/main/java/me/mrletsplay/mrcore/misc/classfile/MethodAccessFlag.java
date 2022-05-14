package me.mrletsplay.mrcore.misc.classfile;

import me.mrletsplay.mrcore.misc.FlagCompound.Flag;

public enum MethodAccessFlag implements Flag {

	PUBLIC("public", 0x1),
	PRIVATE("private", 0x2),
	PROTECTED("protected", 0x4),
	STATIC("static", 0x8),
	FINAL("final", 0x10),
	SYNCHRONIZED("synchronized", 0x20),
	BRIDGE("bridge", 0x40),
	VARARGS("varargs", 0x80),
	NATIVE("native", 0x100),
	ABSTRACT("abstract", 0x400),
	STRICT("strictfp", 0x800),
	SYNTHETIC("synthetic", 0x1000);
	
	private final String name;
	private final long value;
	
	private MethodAccessFlag(String name, long value) {
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
