package me.mrletsplay.mrcore.misc.classfile.attribute;

import java.io.DataInputStream;
import java.io.IOException;

import me.mrletsplay.mrcore.misc.classfile.ClassFile;
import me.mrletsplay.mrcore.misc.classfile.ExceptionHandler;

public class AttributeCode extends AbstractDefaultAttribute {

	private int maxStack, maxLocals;
	private byte[] code;
	private ExceptionHandler[] exceptionTable;
	private Attribute[] attributes;
	
	public AttributeCode(ClassFile classFile, byte[] info) throws IOException {
		super(classFile, info);
		DataInputStream in = getInput();
		this.maxStack = in.readUnsignedShort();
		this.maxLocals = in.readUnsignedShort();
		this.code = new byte[in.readInt()];
		in.read(this.code);
		this.exceptionTable = new ExceptionHandler[in.readUnsignedShort()];
		for(int i = 0; i < exceptionTable.length; i++) {
			exceptionTable[i] = new ExceptionHandler(classFile, in.readUnsignedShort(), in.readUnsignedShort(), in.readUnsignedShort(), in.readUnsignedShort());
		}
		this.attributes = new Attribute[in.readUnsignedShort()];
		for(int i = 0; i < attributes.length; i++) {
			attributes[i] = classFile.readAttribute(classFile.getConstantPool(), in);
		}
	}

	@Override
	public DefaultAttributeType getType() {
		return DefaultAttributeType.CODE;
	}
	
	public int getMaxStack() {
		return maxStack;
	}
	
	public int getMaxLocals() {
		return maxLocals;
	}
	
	public byte[] getByteCode() {
		return code;
	}
	
	public ExceptionHandler[] getExceptionTable() {
		return exceptionTable;
	}
	
	public Attribute[] getAttributes() {
		return attributes;
	}
	
}
