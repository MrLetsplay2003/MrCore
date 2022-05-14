package me.mrletsplay.mrcore.misc.classfile.annotation.value;

import me.mrletsplay.mrcore.misc.classfile.annotation.AnnotationElementValueTag;

public class AnnotationElementArrayValue extends AbstractAnnotationElementValue {

	private Object[] array;
	
	public AnnotationElementArrayValue(AnnotationElementValueTag tag, Object[] array) {
		super(tag);
		this.array = array;
	}
	
	public Object[] getArray() {
		return array;
	}
	
}
