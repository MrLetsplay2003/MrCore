package me.mrletsplay.mrcore.misc.classfile.annotation.value;

import me.mrletsplay.mrcore.misc.classfile.TypeDescriptor;
import me.mrletsplay.mrcore.misc.classfile.annotation.AnnotationElementValueTag;

public class AnnotationElementEnumValue extends AbstractAnnotationElementValue {

	private TypeDescriptor enumClass;
	private String enumName;
	
	public AnnotationElementEnumValue(AnnotationElementValueTag tag, TypeDescriptor type, String enumName) {
		super(tag);
		this.enumClass = type;
		this.enumName = enumName;
	}
	
	public TypeDescriptor getEnumClass() {
		return enumClass;
	}
	
	public String getEnumName() {
		return enumName;
	}
	
}
