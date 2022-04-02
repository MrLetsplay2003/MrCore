package me.mrletsplay.mrcore.misc.classfile.attribute;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import me.mrletsplay.mrcore.misc.classfile.ClassFile;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolUTF8Entry;
import me.mrletsplay.mrcore.misc.classfile.util.ClassFileUtils;

public abstract class DefaultAttribute implements Attribute {

	private ClassFile classFile;
	private int nameIndex;
	private byte[] info;
	
	public DefaultAttribute(ClassFile classFile, int nameIndex, byte[] info) throws IOException {
		this.classFile = classFile;
		this.nameIndex = nameIndex;
		this.info = info;
	}
	
	public DefaultAttribute(ClassFile classFile, DefaultAttributeType type, byte[] info) throws IOException {
		this(classFile, ClassFileUtils.getOrAppendUTF8(classFile, type.getName()), info);
	}
	
	@Deprecated
	public DefaultAttribute(ClassFile classFile, ConstantPoolUTF8Entry name, byte[] info) throws IOException {
		this(classFile, classFile.getConstantPool().indexOf(name), info);
	}
	
	protected DataInputStream createInput() {
		return new DataInputStream(new ByteArrayInputStream(info));
	}
	
	@Override
	public int getNameIndex() {
		return nameIndex;
	}

	public DefaultAttributeType getType() {
		return DefaultAttributeType.getByName(getNameString());
	}
	
	@Override
	public ClassFile getClassFile() {
		return classFile;
	}
	
	@Deprecated
	public void setInfo(byte[] info) {
		this.info = info;
	}
	
	@Override
	public byte[] getInfo() {
		return info;
	}
	
}
