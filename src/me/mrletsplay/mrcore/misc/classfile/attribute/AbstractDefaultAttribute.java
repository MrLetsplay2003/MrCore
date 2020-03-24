package me.mrletsplay.mrcore.misc.classfile.attribute;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import me.mrletsplay.mrcore.misc.classfile.ClassFile;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolUTF8Entry;

public abstract class AbstractDefaultAttribute implements DefaultAttribute {

	private ClassFile classFile;
	private ConstantPoolUTF8Entry name;
	private byte[] info;
	
	public AbstractDefaultAttribute(ClassFile classFile, ConstantPoolUTF8Entry name, byte[] info) throws IOException {
		this.classFile = classFile;
		this.name = name;
		this.info = info;
	}
	
	protected DataInputStream createInput() {
		return new DataInputStream(new ByteArrayInputStream(info));
	}
	
	@Override
	public ConstantPoolUTF8Entry getName() {
		return name;
	}
	
	@Override
	public ClassFile getClassFile() {
		return classFile;
	}
	
	public void setInfo(byte[] info) {
		this.info = info;
	}
	
	@Override
	public byte[] getInfo() {
		return info;
	}
	
}
