package me.mrletsplay.mrcore.misc.classfile.attribute;

import java.io.DataInputStream;
import java.io.IOException;

import me.mrletsplay.mrcore.misc.classfile.ClassFile;
import me.mrletsplay.mrcore.misc.classfile.pool.ConstantPool;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolUTF8Entry;

public class AttributeSignature extends AbstractDefaultAttribute {
	
	private String signature;
	
	public AttributeSignature(ClassFile classFile, ConstantPoolUTF8Entry name, byte[] info) throws IOException {
		super(classFile, name, info);
		ConstantPool pool = classFile.getConstantPool();
		DataInputStream in = createInput();
		this.signature = pool.getEntry(in.readUnsignedShort()).as(ConstantPoolUTF8Entry.class).getValue();
	}
	
	public String getSignature() {
		return signature;
	}
	
	@Override
	public DefaultAttributeType getType() {
		return DefaultAttributeType.SIGNATURE;
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
