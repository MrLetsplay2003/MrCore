package me.mrletsplay.mrcore.misc.classfile.attribute;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import me.mrletsplay.mrcore.misc.classfile.ClassFile;

public abstract class AbstractDefaultAttribute implements DefaultAttribute {

	private ClassFile classFile;
	private byte[] info;
	private DataInputStream input;
	
	public AbstractDefaultAttribute(ClassFile classFile, byte[] info) throws IOException {
		this.classFile = classFile;
		this.info = info;
		this.input = new DataInputStream(new ByteArrayInputStream(info));
	}
	
	protected DataInputStream getInput() {
		return input;
	}
	
	@Override
	public ClassFile getClassFile() {
		return classFile;
	}
	
	@Override
	public byte[] getInfo() {
		return info;
	}
	
}
