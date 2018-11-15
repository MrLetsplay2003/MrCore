package me.mrletsplay.mrcore.misc.classfile.pool.entry;

import me.mrletsplay.mrcore.misc.classfile.pool.ConstantPool;
import me.mrletsplay.mrcore.misc.classfile.pool.ConstantPoolTag;

public class ConstantPoolMethodTypeEntry extends AbstractConstantPoolEntry {

	private int descriptorIndex;
	
	public ConstantPoolMethodTypeEntry(ConstantPool pool, int descriptorIndex) {
		super(pool);
		this.descriptorIndex = descriptorIndex;
	}

	@Override
	public ConstantPoolTag getTag() {
		return ConstantPoolTag.METHOD_TYPE;
	}
	
	public ConstantPoolUTF8Entry getDescriptor() {
		return getConstantPool().getEntry(descriptorIndex).as(ConstantPoolUTF8Entry.class);
	}
	
}
