package me.mrletsplay.mrcore.misc.classfile.annotation;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import me.mrletsplay.mrcore.misc.classfile.TypeDescriptor;
import me.mrletsplay.mrcore.misc.classfile.annotation.value.AbstractAnnotationElementValue;
import me.mrletsplay.mrcore.misc.classfile.annotation.value.AnnotationElementAnnotationValue;
import me.mrletsplay.mrcore.misc.classfile.annotation.value.AnnotationElementArrayValue;
import me.mrletsplay.mrcore.misc.classfile.annotation.value.AnnotationElementBooleanValue;
import me.mrletsplay.mrcore.misc.classfile.annotation.value.AnnotationElementClassValue;
import me.mrletsplay.mrcore.misc.classfile.annotation.value.AnnotationElementDoubleValue;
import me.mrletsplay.mrcore.misc.classfile.annotation.value.AnnotationElementEnumValue;
import me.mrletsplay.mrcore.misc.classfile.annotation.value.AnnotationElementFloatValue;
import me.mrletsplay.mrcore.misc.classfile.annotation.value.AnnotationElementIntegerValue;
import me.mrletsplay.mrcore.misc.classfile.annotation.value.AnnotationElementLongValue;
import me.mrletsplay.mrcore.misc.classfile.annotation.value.AnnotationElementStringValue;
import me.mrletsplay.mrcore.misc.classfile.pool.ConstantPool;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolDoubleEntry;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolFloatEntry;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolIntegerEntry;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolLongEntry;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolUTF8Entry;

public class Annotation {
	
	private TypeDescriptor type;
	private Map<String, AbstractAnnotationElementValue> elementValuePairs;
	
	public Annotation(TypeDescriptor type, Map<String, AbstractAnnotationElementValue> elementValuePairs) {
		super();
		this.type = type;
		this.elementValuePairs = elementValuePairs;
	}
	
	public TypeDescriptor getType() {
		return type;
	}
	
	public Map<String, AbstractAnnotationElementValue> getElementValuePairs() {
		return elementValuePairs;
	}
	
	public AbstractAnnotationElementValue getElementValue(String name) {
		return elementValuePairs.get(name);
	}
	
	public static Annotation readAnnotation(DataInputStream in, ConstantPool pool) throws IOException {
		int typeIndex = in.readUnsignedShort();
		TypeDescriptor type = TypeDescriptor.parse(pool.getEntry(typeIndex).as(ConstantPoolUTF8Entry.class).getValue());
		int numEVPairs = in.readUnsignedShort();
		
		Map<String, AbstractAnnotationElementValue> values = new HashMap<>();
		for(int j = 0; j < numEVPairs; j++) {
			String elementName = pool.getEntry(in.readUnsignedShort()).as(ConstantPoolUTF8Entry.class).getValue();
			values.put(elementName, readElementValue(in, pool));
		}
		
		return new Annotation(type, values);
	}
	
	private static AbstractAnnotationElementValue readElementValue(DataInputStream in, ConstantPool pool) throws IOException {
		AnnotationElementValueTag tag = AnnotationElementValueTag.getByTag((char) in.readUnsignedByte());
		
		AbstractAnnotationElementValue v;
		switch(tag) {
			case BYTE:
			case SHORT:
			case INTEGER:
			case CHARACTER:
			{
				int valueIndex = in.readUnsignedShort();
				ConstantPoolIntegerEntry en = pool.getEntry(valueIndex).as(ConstantPoolIntegerEntry.class);
				v = new AnnotationElementIntegerValue(tag, en.getValue());
				break;
			}
			case BOOLEAN:
			{
				int valueIndex = in.readUnsignedShort();
				ConstantPoolIntegerEntry en = pool.getEntry(valueIndex).as(ConstantPoolIntegerEntry.class);
				v = new AnnotationElementBooleanValue(tag, en.getValue() == 1);
				break;
			}
			case DOUBLE:
			{
				int valueIndex = in.readUnsignedShort();
				ConstantPoolDoubleEntry en = pool.getEntry(valueIndex).as(ConstantPoolDoubleEntry.class);
				v = new AnnotationElementDoubleValue(tag, en.getValue());
				break;
			}
			case FLOAT:
			{
				int valueIndex = in.readUnsignedShort();
				ConstantPoolFloatEntry en = pool.getEntry(valueIndex).as(ConstantPoolFloatEntry.class);
				v = new AnnotationElementFloatValue(tag, en.getValue());
				break;
			}
			case LONG:
			{
				int valueIndex = in.readUnsignedShort();
				ConstantPoolLongEntry en = pool.getEntry(valueIndex).as(ConstantPoolLongEntry.class);
				v = new AnnotationElementLongValue(tag, en.getValue());
				break;
			}
			case STRING:
			{
				int valueIndex = in.readUnsignedShort();
				ConstantPoolUTF8Entry en = pool.getEntry(valueIndex).as(ConstantPoolUTF8Entry.class);
				v = new AnnotationElementStringValue(tag, en.getValue());
				break;
			}
			case ANNOTATION_TYPE:
			{
				v = new AnnotationElementAnnotationValue(tag, readAnnotation(in, pool));
				break;
			}
			case ARRAY:
			{
				int numValues = in.readUnsignedShort();
				Object[] array = new Object[numValues];
				for(int i = 0; i < array.length; i++) {
					array[i] = readElementValue(in, pool);
				}
				v = new AnnotationElementArrayValue(tag, array);
				break;
			}
			case CLASS:
			{
				TypeDescriptor d = TypeDescriptor.parse(pool.getEntry(in.readUnsignedShort()).as(ConstantPoolUTF8Entry.class).getValue());
				v = new AnnotationElementClassValue(tag, d);
				break;
			}
			case ENUM:
			{
				TypeDescriptor d = TypeDescriptor.parse(pool.getEntry(in.readUnsignedShort()).as(ConstantPoolUTF8Entry.class).getValue());
				String enumName = pool.getEntry(in.readUnsignedShort()).as(ConstantPoolUTF8Entry.class).getValue();
				v = new AnnotationElementEnumValue(tag, d, enumName);
				break;
			}
			default:
				throw new IllegalArgumentException("Unknown element value tag");
		}
		
		return v;
	}

}
