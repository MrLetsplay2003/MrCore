package me.mrletsplay.mrcore.misc;

import java.util.Arrays;

public enum PrimitiveType {

	BYTE("byte", byte.class, "java.lang.Byte", Byte.class, "B"),
	SHORT("short", short.class, "java.lang.Short", Short.class, "S"),
	INT("int", int.class, "java.lang.Integer", Integer.class, "I"),
	LONG("long", long.class, "java.lang.Long", Long.class, "J"),
	FLOAT("float", float.class, "java.lang.Float", Float.class, "F"),
	DOUBLE("double", double.class, "java.lang.Double", Double.class, "D"),
	CHAR("char", char.class, "java.lang.Character", Character.class, "C"),
	BOOLEAN("boolean", boolean.class, "java.lang.Boolean", Boolean.class, "Z"),
	OBJECT(null, null, null, null, null),
	VOID("void", void.class, "java.lang.Void", Void.class, "V"),
	;
	
	private final String className, nonPrimitiveClassName, signatureName;
	private final Class<?> primitiveClass, nonPrimitiveClass;
	
	private PrimitiveType(String className, Class<?> primitiveClass, String nonPrimitiveClassName, Class<?> nonPrimitiveClass, String signatureName) {
		this.className = className;
		this.primitiveClass = primitiveClass;
		this.nonPrimitiveClassName = nonPrimitiveClassName;
		this.nonPrimitiveClass = nonPrimitiveClass;
		this.signatureName = signatureName;
	}
	
	public String getClassName() {
		return className;
	}
	
	public Class<?> getPrimitiveClass() {
		return primitiveClass;
	}
	
	public String getNonPrimitiveClassName() {
		return nonPrimitiveClassName;
	}
	
	public Class<?> getNonPrimitiveClass() {
		return nonPrimitiveClass;
	}
	
	public String getSignatureName() {
		return signatureName;
	}
	
	public static PrimitiveType getByClassName(String className) {
		return Arrays.stream(values()).filter(t -> t.className != null && t.className.equals(className)).findFirst().orElse(null);
	}
	
	public static PrimitiveType getByPrimitiveClass(Class<?> primitiveClass) {
		return Arrays.stream(values()).filter(t -> t.primitiveClass != null && t.primitiveClass.equals(primitiveClass)).findFirst().orElse(null);
	}
	
	public static PrimitiveType getByNonPrimitiveClassName(String className) {
		return Arrays.stream(values()).filter(t -> t.nonPrimitiveClassName != null && t.nonPrimitiveClassName.equals(className)).findFirst().orElse(null);
	}
	
	public static PrimitiveType getByNonPrimitiveClass(Class<?> nonPrimitiveClass) {
		return Arrays.stream(values()).filter(t -> t.nonPrimitiveClass != null && t.nonPrimitiveClass.equals(nonPrimitiveClass)).findFirst().orElse(null);
	}
	
	public static PrimitiveType getBySignature(String signatureName) {
		return Arrays.stream(values()).filter(t -> t.signatureName != null && t.signatureName.equals(signatureName)).findFirst().orElse(null);
	}
	
}
