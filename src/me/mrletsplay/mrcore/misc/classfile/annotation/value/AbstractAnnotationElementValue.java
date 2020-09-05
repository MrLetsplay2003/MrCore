package me.mrletsplay.mrcore.misc.classfile.annotation.value;

import me.mrletsplay.mrcore.misc.classfile.annotation.AnnotationElementValueTag;

public abstract class AbstractAnnotationElementValue {
	
	private AnnotationElementValueTag tag;
	
	public AbstractAnnotationElementValue(AnnotationElementValueTag tag) {
		this.tag = tag;
	}

	public AnnotationElementValueTag getTag() {
		return tag;
	}
	
}
