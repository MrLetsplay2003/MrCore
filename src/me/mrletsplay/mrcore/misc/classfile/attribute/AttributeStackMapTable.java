package me.mrletsplay.mrcore.misc.classfile.attribute;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import me.mrletsplay.mrcore.misc.FriendlyException;
import me.mrletsplay.mrcore.misc.classfile.ClassFile;
import me.mrletsplay.mrcore.misc.classfile.attribute.stackmap.StackMapFrame;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolUTF8Entry;

public class AttributeStackMapTable extends DefaultAttribute {

	private StackMapFrame[] entries;
	
	public AttributeStackMapTable(ClassFile classFile, int nameIndex, byte[] info) throws IOException {
		super(classFile, nameIndex, info);
		DataInputStream in = createInput();
		this.entries = new StackMapFrame[in.readUnsignedShort()];
		for(int i = 0; i < entries.length; i++) {
			entries[i] = StackMapFrame.readEntry(classFile.getConstantPool(), in);
		}
	}
	
	@Deprecated
	public AttributeStackMapTable(ClassFile classFile, ConstantPoolUTF8Entry name, byte[] info) throws IOException {
		this(classFile, classFile.getConstantPool().indexOf(name), info);
	}
	
	public AttributeStackMapTable(ClassFile classFile) throws IOException {
		super(classFile, DefaultAttributeType.STACK_MAP_TABLE, new byte[0]);
		this.entries = new StackMapFrame[0];
	}
	
	public void setEntries(StackMapFrame[] entries) {
		this.entries = entries;
	}
	
	public StackMapFrame getEntry(int index) {
		return entries[index];
	}
	
	public StackMapFrame[] getEntries() {
		return entries;
	}
	
	@Override
	public void setAttributes(Attribute[] attributes) {
		throw new UnsupportedOperationException("Can't have attributes");
	}

	@Override
	public Attribute[] getAttributes() {
		return new Attribute[0];
	}
	
	@Override
	public byte[] getInfo() {
		ByteArrayOutputStream bOut = new ByteArrayOutputStream();
		DataOutputStream dOut = new DataOutputStream(bOut);
		
		try {
			dOut.writeShort(entries.length);
			for(int i = 0; i < entries.length; i++) {
				entries[i].write(dOut);
			}
		}catch(IOException e) {
			throw new FriendlyException(e);
		}
		
		return bOut.toByteArray();
	}
	
}
