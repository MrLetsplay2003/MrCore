package me.mrletsplay.mrcore.misc.classfile.attribute;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import me.mrletsplay.mrcore.misc.FriendlyException;
import me.mrletsplay.mrcore.misc.classfile.ByteCode;
import me.mrletsplay.mrcore.misc.classfile.ClassFile;
import me.mrletsplay.mrcore.misc.classfile.ExceptionHandler;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolUTF8Entry;

public class AttributeCode extends AbstractDefaultAttribute {

	private int maxStack, maxLocals;
	private ByteCode code;
	private ExceptionHandler[] exceptionTable;
	private Attribute[] attributes;
	
	public AttributeCode(ClassFile classFile, ConstantPoolUTF8Entry name, byte[] info) throws IOException {
		super(classFile, name, info);
		DataInputStream in = getInput();
		this.maxStack = in.readUnsignedShort();
		this.maxLocals = in.readUnsignedShort();
		this.code = new ByteCode(new byte[in.readInt()]);
		in.read(this.code.getBytes());
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
	
	public void setMaxStack(int maxStack) {
		this.maxStack = maxStack;
	}
	
	public int getMaxStack() {
		return maxStack;
	}
	
	public void setMaxLocals(int maxLocals) {
		this.maxLocals = maxLocals;
	}
	
	public int getMaxLocals() {
		return maxLocals;
	}
	
	public ByteCode getCode() {
		return code;
	}
	
	@Override
	public byte[] getInfo() {
		ByteArrayOutputStream bO = new ByteArrayOutputStream();
		DataOutputStream dOut = new DataOutputStream(bO);
		try {
			dOut.writeShort(maxStack);
			dOut.writeShort(maxLocals);
			dOut.writeInt(code.getBytes().length);
			dOut.write(code.getBytes());
			dOut.writeShort(exceptionTable.length);
			for(ExceptionHandler exH : exceptionTable) {
				dOut.writeShort(exH.getStartPC());
				dOut.writeShort(exH.getEndPC());
				dOut.writeShort(exH.getHandlerPC());
				dOut.writeShort(getClassFile().getConstantPool().indexOf(exH.getCatchType()));
			}
			dOut.writeShort(attributes.length);
			for(Attribute a : attributes) {
				getClassFile().writeAttribute(dOut, getClassFile().getConstantPool(), a);
			}
		} catch (IOException e) {
			throw new FriendlyException(e);
		}
		return bO.toByteArray();
	}
	
	public ExceptionHandler[] getExceptionTable() {
		return exceptionTable;
	}
	
	public Attribute[] getAttributes() {
		return attributes;
	}
	
}
