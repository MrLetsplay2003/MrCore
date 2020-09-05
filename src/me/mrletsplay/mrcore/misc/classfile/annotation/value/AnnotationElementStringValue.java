package me.mrletsplay.mrcore.misc.classfile.annotation.value;

import me.mrletsplay.mrcore.misc.classfile.annotation.AnnotationElementValueTag;

public class AnnotationElementStringValue extends AbstractAnnotationElementValue {

	private String value;
	
	public AnnotationElementStringValue(AnnotationElementValueTag tag, String value) {
		super(tag);
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}

}
