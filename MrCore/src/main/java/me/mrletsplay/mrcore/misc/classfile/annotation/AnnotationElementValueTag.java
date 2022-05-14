package me.mrletsplay.mrcore.misc.classfile.annotation;

import java.util.Arrays;

public enum AnnotationElementValueTag {
	
	BYTE('B'),
	CHARACTER('C'),
	DOUBLE('D'),
	FLOAT('F'),
	INTEGER('I'),
	LONG('J'),
	SHORT('S'),
	BOOLEAN('Z'),
	STRING('s'),
	ENUM('e'),
	CLASS('c'),
	ANNOTATION_TYPE('@'),
	ARRAY('['),
	;

	private char tag;
	
	private AnnotationElementValueTag(char tag) {
		this.tag = tag;
	}
	
	public char getTag() {
		return tag;
	}
	
	public static AnnotationElementValueTag getByTag(char tag) {
		return Arrays.stream(values())
				.filter(v -> v.tag == tag)
				.findFirst().orElse(null);
	}
	
}
