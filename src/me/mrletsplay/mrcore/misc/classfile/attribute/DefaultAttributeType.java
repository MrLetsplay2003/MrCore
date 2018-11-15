package me.mrletsplay.mrcore.misc.classfile.attribute;

import java.util.Arrays;

public enum DefaultAttributeType {

	CONSTANT_VALUE("ConstantValue"),
	CODE("Code"),
	STACK_MAP_TABLE("StackMapTable"),
	EXCEPTIONS("Exceptions"),
	INNER_CLASSES("InnerClasses"),
	ENCLOSING_METHOD("EnclosingMethod"),
	SYNTHETIC("Synthetic"),
	SIGNATURE("Signature"),
	SOURCE_FILE("SourceFile"),
	SOURCE_DEBUG_EXCEPTION("SourceDebugException"),
	LINE_NUMBER_TABLE("LineNumberTable"),
	LOCAL_VARIABLE_TYPE("LocalVariableType"),
	LOCAL_VARIABLE_TYPE_TABLE("LocalVariableTypeTable"),
	DEPRECATED("Deprecated"),
	RUNTIME_VISIBLE_ANNOTATIONS("RuntimeVisibleAnnotations"),
	RUNTIME_INVISIBLE_ANNOTATIONS("RuntimeInvisibleAnnotations"),
	RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS("RuntimeVisibleParameterAnnotations"),
	RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS("RuntimeInvisibleParameterAnnotations"),
	ANNOTATION_DEFAULT("AnnotationDefault"),
	BOOTSTRAP_METHODS("BootstrapMethods");
	
	private final String name;
	
	private DefaultAttributeType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public static DefaultAttributeType getByName(String name) {
		return Arrays.stream(values()).filter(a -> a.getName().equals(name)).findFirst().orElse(null);
	}
	
}
