package me.mrletsplay.mrcore.misc.classfile.pool;

import java.util.Arrays;

public enum MethodHandleReferenceType {
	
	GET_FIELD(1),
	GET_STATIC(2),
	PUT_FIELD(3),
	PUT_STATIC(4),
	INVOKE_VIRTUAL(5),
	INVOKE_STATIC(6),
	INVOKE_SPECIAL(7),
	NEW_INVOKE_SPECIAL(8),
	INVOKE_INTERFACE(9);
	
	private final int kind;
	
	private MethodHandleReferenceType(int kind) {
		this.kind = kind;
	}
	
	public int getKind() {
		return kind;
	}
	
	public static MethodHandleReferenceType getByKind(int kind) {
		return Arrays.stream(values()).filter(r -> r.kind == kind).findFirst().orElse(null);
	}
	
}
