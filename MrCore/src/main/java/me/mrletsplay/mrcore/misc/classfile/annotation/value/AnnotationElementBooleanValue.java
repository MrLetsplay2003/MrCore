package me.mrletsplay.mrcore.misc.classfile.annotation.value;

import me.mrletsplay.mrcore.misc.classfile.annotation.AnnotationElementValueTag;

public class AnnotationElementBooleanValue extends AbstractAnnotationElementValue {

	private boolean value;
	
	public AnnotationElementBooleanValue(AnnotationElementValueTag tag, boolean value) {
		super(tag);
		this.value = value;
	}
	
	public boolean getValue() {
		return value;
	}

}
