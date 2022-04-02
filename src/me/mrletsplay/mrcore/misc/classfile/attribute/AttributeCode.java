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
import me.mrletsplay.mrcore.misc.classfile.util.ClassFileUtils;

public class AttributeCode extends AbstractDefaultAttribute {

	private int maxStack, maxLocals;
	private ByteCode code;
	private ExceptionHandler[] exceptionTable;
	private Attribute[] attributes;
	
	public AttributeCode(ClassFile classFile, ConstantPoolUTF8Entry name, byte[] info) throws IOException {
		super(classFile, name, info);
		DataInputStream in = createInput();
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
	
	public AttributeCode(ClassFile classFile) throws IOException {
		super(classFile, classFile.getConstantPool().getEntry(ClassFileUtils.getOrAppendUTF8(classFile, DefaultAttributeType.CODE.getName())).as(ConstantPoolUTF8Entry.class), new byte[0]);
		this.maxStack = 0;
		this.maxLocals = 0;
		this.code = new ByteCode(new byte[0]);
		this.exceptionTable = new ExceptionHandler[0];
		this.attributes = new Attribute[0];
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
	
	public void setExceptionTable(ExceptionHandler[] exceptionTable) {
		this.exceptionTable = exceptionTable;
	}
	
	public ExceptionHandler[] getExceptionTable() {
		return exceptionTable;
	}
	
	@Override
	public void setAttributes(Attribute[] attributes) {
		this.attributes = attributes;
	}
	
	@Override
	public Attribute[] getAttributes() {
		return attributes;
	}
	
	public AttributeStackMapTable getStackMapTable() {
		Attribute a = getAttribute(DefaultAttributeType.STACK_MAP_TABLE);
		if(a == null) return null;
		return a.as(AttributeStackMapTable.class);
	}
	
	public AttributeLocalVariableTable getLocalVariableTable() {
		Attribute a = getAttribute(DefaultAttributeType.LOCAL_VARIABLE_TABLE);
		if(a == null) return null;
		return a.as(AttributeLocalVariableTable.class);
	}
	
}
