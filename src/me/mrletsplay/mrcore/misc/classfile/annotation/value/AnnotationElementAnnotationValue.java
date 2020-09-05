package me.mrletsplay.mrcore.misc.classfile.annotation.value;

import me.mrletsplay.mrcore.misc.classfile.annotation.Annotation;
import me.mrletsplay.mrcore.misc.classfile.annotation.AnnotationElementValueTag;

public class AnnotationElementAnnotationValue extends AbstractAnnotationElementValue {

	private Annotation annotation;
	
	public AnnotationElementAnnotationValue(AnnotationElementValueTag tag, Annotation annotation) {
		super(tag);
		this.annotation = annotation;
	}
	
	public Annotation getAnnotation() {
		return annotation;
	}
	
}
