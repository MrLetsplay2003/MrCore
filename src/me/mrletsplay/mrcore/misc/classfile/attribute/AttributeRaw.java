package me.mrletsplay.mrcore.misc.classfile.attribute;

import me.mrletsplay.mrcore.misc.classfile.ClassFile;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolUTF8Entry;
import me.mrletsplay.mrcore.misc.classfile.util.ClassFileUtils;

public class AttributeRaw implements Attribute {

	private ClassFile classFile;
	private int nameIndex;
	private byte[] info;
	
	public AttributeRaw(ClassFile classFile, int nameIndex, byte[] info) {
		this.classFile = classFile;
		this.nameIndex = nameIndex;
		this.info = info;
	}
	
	@Deprecated
	public AttributeRaw(ClassFile classFile, ConstantPoolUTF8Entry name, byte[] info) {
		this(classFile, classFile.getConstantPool().indexOf(name), info);
	}
	
	public AttributeRaw(ClassFile classFile, DefaultAttributeType type, byte[] info) {
		this(classFile, ClassFileUtils.getOrAppendUTF8(classFile, type.getName()), info);
	}
	
	@Override
	public int getNameIndex() {
		return nameIndex;
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
	public void setAttributes(Attribute[] attributes) {
		throw new UnsupportedOperationException("Can't have attributes");
	}
	
	@Override
	public Attribute[] getAttributes() {
		throw new UnsupportedOperationException();
	}
	
}
