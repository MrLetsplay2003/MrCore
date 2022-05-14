package me.mrletsplay.mrcore.misc.classfile.attribute;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

import me.mrletsplay.mrcore.misc.FriendlyException;
import me.mrletsplay.mrcore.misc.classfile.ClassFile;
import me.mrletsplay.mrcore.misc.classfile.LocalVariable;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolUTF8Entry;

public class AttributeLocalVariableTable extends DefaultAttribute {

	private LocalVariable[] entries;

	public AttributeLocalVariableTable(ClassFile classFile, int nameIndex, byte[] info) throws IOException {
		super(classFile, nameIndex, info);
		DataInputStream in = createInput();
		this.entries = new LocalVariable[in.readUnsignedShort()];
		for(int i = 0; i < entries.length; i++) {
			int startPC = in.readUnsignedShort();
			int length = in.readUnsignedShort();
			ConstantPoolUTF8Entry lname = classFile.getConstantPool().getEntry(in.readUnsignedShort()).as(ConstantPoolUTF8Entry.class);
			ConstantPoolUTF8Entry ldescriptor = classFile.getConstantPool().getEntry(in.readUnsignedShort()).as(ConstantPoolUTF8Entry.class);
			int index = in.readUnsignedShort();
			entries[i] = new LocalVariable(startPC, length, lname, ldescriptor, index);
		}
	}
	
	@Deprecated
	public AttributeLocalVariableTable(ClassFile classFile, ConstantPoolUTF8Entry name, byte[] info) throws IOException {
		this(classFile, classFile.getConstantPool().indexOf(name), info);
	}
	
	public AttributeLocalVariableTable(ClassFile classFile) throws IOException {
		super(classFile, DefaultAttributeType.LOCAL_VARIABLE_TABLE, new byte[0]);
		this.entries = new LocalVariable[0];
	}
	
	public void setEntries(LocalVariable[] entries) {
		this.entries = entries;
	}
	
	public LocalVariable[] getEntries() {
		return entries;
	}
	
	public LocalVariable[] getEntriesAt(int pc) { // Empty entries for double/long to have correct indexes
		return Arrays.stream(entries).filter(en -> en != null && pc >= en.getStartPC() && pc < en.getStartPC() + en.getLength()).toArray(LocalVariable[]::new);
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
		ByteArrayOutputStream bO = new ByteArrayOutputStream();
		DataOutputStream dOut = new DataOutputStream(bO);
		try {
			dOut.writeShort(entries.length);
			for(LocalVariable v : entries) {
				dOut.writeShort(v.getStartPC());
				dOut.writeShort(v.getLength());
				dOut.writeShort(getClassFile().getConstantPool().indexOf(v.getName()));
				dOut.writeShort(getClassFile().getConstantPool().indexOf(v.getDescriptor()));
				dOut.writeShort(v.getIndex());
			}
		} catch (IOException e) {
			throw new FriendlyException(e);
		}
		return bO.toByteArray();
	}

}
