package me.mrletsplay.mrcore.misc.classfile.annotation.value;

import me.mrletsplay.mrcore.misc.classfile.annotation.AnnotationElementValueTag;

public class AnnotationElementDoubleValue extends AbstractAnnotationElementValue {

	private double value;
	
	public AnnotationElementDoubleValue(AnnotationElementValueTag tag, double value) {
		super(tag);
		this.value = value;
	}
	
	public double getValue() {
		return value;
	}

}
