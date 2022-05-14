package me.mrletsplay.mrcore.misc.classfile.annotation.value;

import me.mrletsplay.mrcore.misc.classfile.TypeDescriptor;
import me.mrletsplay.mrcore.misc.classfile.annotation.AnnotationElementValueTag;

public class AnnotationElementClassValue extends AbstractAnnotationElementValue {

	private TypeDescriptor classInfo;
	
	public AnnotationElementClassValue(AnnotationElementValueTag tag, TypeDescriptor type) {
		super(tag);
		this.classInfo = type;
	}
	
	public TypeDescriptor getClassInfo() {
		return classInfo;
	}
	
}
