package me.mrletsplay.mrcore.misc.classfile.pool.entry;

import me.mrletsplay.mrcore.misc.classfile.pool.ConstantPool;
import me.mrletsplay.mrcore.misc.classfile.pool.ConstantPoolTag;

public class ConstantPoolNameAndTypeEntry extends AbstractConstantPoolEntry {

	private int nameIndex, descriptorIndex;
	
	public ConstantPoolNameAndTypeEntry(ConstantPool pool, int nameIndex, int descriptorIndex) {
		super(pool);
		this.nameIndex = nameIndex;
		this.descriptorIndex = descriptorIndex;
	}
	
	@Override
	public ConstantPoolTag getTag() {
		return ConstantPoolTag.NAME_AND_TYPE;
	}
	
	public ConstantPoolUTF8Entry getName() {
		return getConstantPool().getEntry(nameIndex).as(ConstantPoolUTF8Entry.class);
	}
	
	public ConstantPoolUTF8Entry getDescriptor() {
		return getConstantPool().getEntry(descriptorIndex).as(ConstantPoolUTF8Entry.class);
	}
	
}
