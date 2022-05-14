package me.mrletsplay.mrcore.misc.classfile.attribute;

import java.io.DataInputStream;
import java.io.IOException;

import me.mrletsplay.mrcore.misc.classfile.ClassFile;
import me.mrletsplay.mrcore.misc.classfile.pool.ConstantPool;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolUTF8Entry;

public class AttributeSignature extends DefaultAttribute {
	
	private String signature;
	
	public AttributeSignature(ClassFile classFile, int nameIndex, byte[] info) throws IOException {
		super(classFile, nameIndex, info);
		ConstantPool pool = classFile.getConstantPool();
		DataInputStream in = createInput();
		this.signature = pool.getEntry(in.readUnsignedShort()).as(ConstantPoolUTF8Entry.class).getValue();
	}
	
	@Deprecated
	public AttributeSignature(ClassFile classFile, ConstantPoolUTF8Entry name, byte[] info) throws IOException {
		this(classFile, classFile.getConstantPool().indexOf(name), info);
	}
	
	public AttributeSignature(ClassFile classFile) throws IOException {
		super(classFile, DefaultAttributeType.SIGNATURE, new byte[0]);
		this.signature = "";
	}
	
	public void setSignature(String signature) {
		this.signature = signature;
	}
	
	public String getSignature() {
		return signature;
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
