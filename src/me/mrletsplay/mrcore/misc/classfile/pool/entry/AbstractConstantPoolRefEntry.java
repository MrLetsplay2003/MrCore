package me.mrletsplay.mrcore.misc.classfile.pool.entry;

import me.mrletsplay.mrcore.misc.classfile.pool.ConstantPool;

public abstract class AbstractConstantPoolRefEntry extends AbstractConstantPoolEntry { // TODO: Checks

	private int classIndex;
	private int nameAndTypeIndex;
	
	protected AbstractConstantPoolRefEntry(ConstantPool pool, int classIndex, int nameAndTypeIndex) {
		super(pool);
		this.classIndex = classIndex;
		this.nameAndTypeIndex = nameAndTypeIndex;
	}
	
	public int getClassIndex() {
		return classIndex;
	}
	
	public ConstantPoolClassEntry getClassInfo() {
		return getConstantPool().getEntry(classIndex).as(ConstantPoolClassEntry.class);
	}
	
	public int getNameAndTypeIndex() {
		return nameAndTypeIndex;
	}
	
	public ConstantPoolNameAndTypeEntry getNameAndType() {
		return getConstantPool().getEntry(nameAndTypeIndex).as(ConstantPoolNameAndTypeEntry.class);
	}

}
