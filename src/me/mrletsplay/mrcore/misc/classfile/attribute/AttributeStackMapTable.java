package me.mrletsplay.mrcore.misc.classfile.attribute;

import java.io.DataInputStream;
import java.io.IOException;

import me.mrletsplay.mrcore.misc.classfile.ClassFile;
import me.mrletsplay.mrcore.misc.classfile.attribute.stackmap.StackMapFrame;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolUTF8Entry;

public class AttributeStackMapTable extends AbstractDefaultAttribute {

	private StackMapFrame[] entries;
	
	public AttributeStackMapTable(ClassFile classFile, ConstantPoolUTF8Entry name, byte[] info) throws IOException {
		super(classFile, name, info);
		DataInputStream in = getInput();
		this.entries = new StackMapFrame[in.readUnsignedShort()];
		for(int i = 0; i < entries.length; i++) {
			entries[i] = StackMapFrame.readEntry(classFile.getConstantPool(), in);
		}
	}
	
	public StackMapFrame[] getEntries() {
		return entries;
	}

	@Override
	public DefaultAttributeType getType() {
		return DefaultAttributeType.STACK_MAP_TABLE;
	}

	@Override
	public Attribute[] getAttributes() {
		return new Attribute[0];
	}

}
