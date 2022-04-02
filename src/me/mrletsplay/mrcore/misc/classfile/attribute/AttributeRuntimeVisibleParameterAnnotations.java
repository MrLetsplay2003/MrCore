package me.mrletsplay.mrcore.misc.classfile.attribute;

import java.io.DataInputStream;
import java.io.IOException;

import me.mrletsplay.mrcore.misc.classfile.ClassFile;
import me.mrletsplay.mrcore.misc.classfile.annotation.Annotation;
import me.mrletsplay.mrcore.misc.classfile.pool.ConstantPool;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolUTF8Entry;

public class AttributeRuntimeVisibleParameterAnnotations extends DefaultAttribute {
	
	private Annotation[][] annotations;
	
	public AttributeRuntimeVisibleParameterAnnotations(ClassFile classFile, int nameIndex, byte[] info) throws IOException {
		super(classFile, nameIndex, info);
		ConstantPool pool = classFile.getConstantPool();
		DataInputStream in = createInput();
		this.annotations = new Annotation[in.readUnsignedByte()][];
		for(int i = 0; i < annotations.length; i++) {
			annotations[i] = new Annotation[in.readUnsignedShort()];
			for(int j = 0; j < annotations[i].length; j++) {
				annotations[i][j] = Annotation.readAnnotation(in, pool);
			}
		}
	}
	
	@Deprecated
	public AttributeRuntimeVisibleParameterAnnotations(ClassFile classFile, ConstantPoolUTF8Entry name, byte[] info) throws IOException {
		this(classFile, classFile.getConstantPool().indexOf(name), info);
	}
	
	public AttributeRuntimeVisibleParameterAnnotations(ClassFile classFile) throws IOException {
		super(classFile, DefaultAttributeType.RUNTIME_VISIBLE_ANNOTATIONS, new byte[0]);
		this.annotations = new Annotation[0][];
	}
	
	public void setAnnotations(int index, Annotation[] annotations) {
		this.annotations[index] = annotations;
	}
	
	public Annotation[] getAnnotations(int index) {
		return annotations[index];
	}

	@Override
	public void setAttributes(Attribute[] attributes) {
		throw new UnsupportedOperationException("Can't have attributes");
	}

	@Override
	public Attribute[] getAttributes() {
		return new Attribute[0];
	}
	
	// TODO: getInfo()

}
