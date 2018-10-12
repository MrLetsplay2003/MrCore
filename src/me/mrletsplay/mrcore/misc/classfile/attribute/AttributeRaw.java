package me.mrletsplay.mrcore.misc.classfile.attribute;

import me.mrletsplay.mrcore.misc.classfile.ClassFile;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolUTF8Entry;

public class AttributeRaw implements Attribute {

	private ClassFile classFile;
	private int nameIndex;
	private byte[] info;
	
	public AttributeRaw(ClassFile classFile, int nameIndex, byte[] info) {
		this.classFile = classFile;
		this.nameIndex = nameIndex;
		this.info = info;
	}
	
	public ConstantPoolUTF8Entry getName() {
		return classFile.getConstantPool().getEntry(nameIndex).as(ConstantPoolUTF8Entry.class);
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
	
}
