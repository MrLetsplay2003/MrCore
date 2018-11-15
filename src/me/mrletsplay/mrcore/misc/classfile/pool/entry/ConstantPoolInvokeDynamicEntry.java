package me.mrletsplay.mrcore.misc.classfile.pool.entry;

import me.mrletsplay.mrcore.misc.classfile.pool.ConstantPool;
import me.mrletsplay.mrcore.misc.classfile.pool.ConstantPoolTag;

public class ConstantPoolInvokeDynamicEntry extends AbstractConstantPoolEntry {

	private int bootstrapMethodAttributeIndex; // Bootstrap method
	private int nameAndTypeIndex;
	
	public ConstantPoolInvokeDynamicEntry(ConstantPool pool, int bootstrapMethodAttributeIndex, int nameAndTypeIndex) {
		super(pool);
		this.bootstrapMethodAttributeIndex = bootstrapMethodAttributeIndex;
		this.nameAndTypeIndex = nameAndTypeIndex;
	}

	@Override
	public ConstantPoolTag getTag() {
		return ConstantPoolTag.METHOD_TYPE;
	}
	
	public int getBootstrapMethodAttributeIndex() {
		return bootstrapMethodAttributeIndex;
	}
	
	public ConstantPoolNameAndTypeEntry getNameAndType() {
		return getConstantPool().getEntry(nameAndTypeIndex).as(ConstantPoolNameAndTypeEntry.class);
	}
	
}
