package me.mrletsplay.mrcore.misc.classfile.annotation.value;

import me.mrletsplay.mrcore.misc.classfile.annotation.AnnotationElementValueTag;

public class AnnotationElementLongValue extends AbstractAnnotationElementValue {

	private long value;
	
	public AnnotationElementLongValue(AnnotationElementValueTag tag, long value) {
		super(tag);
		this.value = value;
	}
	
	public long getValue() {
		return value;
	}

}
