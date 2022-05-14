package me.mrletsplay.mrcore.misc.classfile.annotation.value;

import me.mrletsplay.mrcore.misc.classfile.annotation.AnnotationElementValueTag;

public class AnnotationElementFloatValue extends AbstractAnnotationElementValue {

	private float value;
	
	public AnnotationElementFloatValue(AnnotationElementValueTag tag, float value) {
		super(tag);
		this.value = value;
	}
	
	public float getValue() {
		return value;
	}

}
