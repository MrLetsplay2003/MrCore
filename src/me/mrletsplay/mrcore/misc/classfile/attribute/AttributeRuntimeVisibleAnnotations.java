package me.mrletsplay.mrcore.misc.classfile.attribute;

import java.io.DataInputStream;
import java.io.IOException;

import me.mrletsplay.mrcore.misc.classfile.ClassFile;
import me.mrletsplay.mrcore.misc.classfile.annotation.Annotation;
import me.mrletsplay.mrcore.misc.classfile.pool.ConstantPool;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolUTF8Entry;

public class AttributeRuntimeVisibleAnnotations extends DefaultAttribute {
	
	private Annotation[] annotations;
	
	public AttributeRuntimeVisibleAnnotations(ClassFile classFile, int nameIndex, byte[] info) throws IOException {
		super(classFile, nameIndex, info);
		ConstantPool pool = classFile.getConstantPool();
		DataInputStream in = createInput();
		this.annotations = new Annotation[in.readUnsignedShort()];
		for(int i = 0; i < annotations.length; i++) {
			annotations[i] = Annotation.readAnnotation(in, pool);
		}
	}
	
	@Deprecated
	public AttributeRuntimeVisibleAnnotations(ClassFile classFile, ConstantPoolUTF8Entry name, byte[] info) throws IOException {
		this(classFile, classFile.getConstantPool().indexOf(name), info);
	}
	
	public AttributeRuntimeVisibleAnnotations(ClassFile classFile) throws IOException {
		super(classFile, DefaultAttributeType.RUNTIME_VISIBLE_ANNOTATIONS, new byte[0]);
		this.annotations = new Annotation[0];
	}
	
	public void setAnnotations(Annotation[] annotations) {
		this.annotations = annotations;
	}
	
	public Annotation[] getAnnotations() {
		return annotations;
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
