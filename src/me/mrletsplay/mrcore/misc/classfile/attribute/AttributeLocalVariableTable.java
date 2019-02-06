package me.mrletsplay.mrcore.misc.classfile.attribute;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;

import me.mrletsplay.mrcore.misc.classfile.ClassFile;
import me.mrletsplay.mrcore.misc.classfile.LocalVariable;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolUTF8Entry;

public class AttributeLocalVariableTable extends AbstractDefaultAttribute {

	private LocalVariable[] entries;
	
	public AttributeLocalVariableTable(ClassFile classFile, ConstantPoolUTF8Entry name, byte[] info) throws IOException {
		super(classFile, name, info);
		DataInputStream in = getInput();
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

	@Override
	public DefaultAttributeType getType() {
		return DefaultAttributeType.LOCAL_VARIABLE_TABLE;
	}
	
	public LocalVariable[] getEntries() {
		return entries;
	}
	
	public LocalVariable[] getEntriesAt(int pc) { // Empty entries for double/long to have correct indexes
		return Arrays.stream(entries).filter(en -> pc >= en.getStartPC() && pc < en.getStartPC() + en.getLength()).toArray(LocalVariable[]::new);
	}

	@Override
	public Attribute[] getAttributes() {
		return new Attribute[0];
	}

}
