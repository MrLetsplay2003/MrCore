package me.mrletsplay.mrcore.misc.classfile.pool;

import java.util.Arrays;

public enum ConstantPoolTag {

	CLASS(7),
	FIELD_REF(9),
	METHOD_REF(10),
	INTERFACE_METHOD_REF(11),
	STRING(8),
	INTEGER(3),
	FLOAT(4),
	LONG(5),
	DOUBLE(6),
	NAME_AND_TYPE(12),
	UTF_8(1),
	METHOD_HANDLE(15),
	METHOD_TYPE(16),
	INVOKE_DYNAMIC(18);
	
	private final int value;
	
	private ConstantPoolTag(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public static ConstantPoolTag getByValue(int value) {
		return Arrays.stream(values()).filter(t -> t.value == value).findFirst().orElse(null);
	}
	
}
