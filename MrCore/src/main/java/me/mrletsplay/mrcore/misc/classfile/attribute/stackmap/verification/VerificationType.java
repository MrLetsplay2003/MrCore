package me.mrletsplay.mrcore.misc.classfile.attribute.stackmap.verification;

import java.util.Arrays;

public enum VerificationType {

	TOP(0),
	INTEGER(1),
	FLOAT(2),
	LONG(3),
	DOUBLE(4),
	NULL(5),
	UNINITIALIZED_THIS(6),
	OBJECT(7),
	UNINITIALIZED_VARIABLE(8),
	;
	
	private final int value;
	
	private VerificationType(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public static VerificationType getByValue(int value) {
		return Arrays.stream(values()).filter(t -> t.value == value).findFirst().orElse(null);
	}
	
}
