package me.mrletsplay.mrcore.misc.classfile;

import me.mrletsplay.mrcore.misc.FlagCompound.Flag;

public enum ClassAccessFlag implements Flag {

	PUBLIC("public", 0x1),
	FINAL("final", 0x10),
//	SUPER("super", 0x20),
	INTERFACE("interface", 0x200),
	ABSTRACT("abstract", 0x400),
	SYNTHETIC("synthetic", 0x1000),
	ANNOTATION("annotation", 0x2000),
	ENUM("enum", 0x4000);
	
	private final String name;
	private final long value;
	
	private ClassAccessFlag(String name, long value) {
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
