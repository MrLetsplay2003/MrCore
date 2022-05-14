package me.mrletsplay.mrcore.misc.classfile.annotation.value;

import me.mrletsplay.mrcore.misc.classfile.annotation.AnnotationElementValueTag;

public class AnnotationElementIntegerValue extends AbstractAnnotationElementValue {

	private int value;
	
	public AnnotationElementIntegerValue(AnnotationElementValueTag tag, int value) {
		super(tag);
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}

}
