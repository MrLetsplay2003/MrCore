package me.mrletsplay.mrcore.misc.classfile.attribute;

import me.mrletsplay.mrcore.misc.classfile.ClassFile;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolUTF8Entry;

public class AttributeRaw implements Attribute {

	private ClassFile classFile;
	private ConstantPoolUTF8Entry name;
	private byte[] info;
	
	public AttributeRaw(ClassFile classFile, ConstantPoolUTF8Entry name, byte[] info) {
		this.classFile = classFile;
		this.name = name;
		this.info = info;
	}
	
	@Override
	public ConstantPoolUTF8Entry getName() {
		return name;
	}
	
	@Override
	public String getNameString() {
		return getName().getValue();
	}
	
	@Override
	public byte[] getInfo() {
		return info;
	}

	@Override
	public ClassFile getClassFile() {
		return classFile;
	}
	
	@Override
	public Attribute[] getAttributes() {
		throw new UnsupportedOperationException();
	}
	
}
