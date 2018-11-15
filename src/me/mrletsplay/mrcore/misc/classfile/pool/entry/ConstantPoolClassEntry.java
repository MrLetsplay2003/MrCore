package me.mrletsplay.mrcore.misc.classfile.pool.entry;

import me.mrletsplay.mrcore.misc.classfile.pool.ConstantPool;
import me.mrletsplay.mrcore.misc.classfile.pool.ConstantPoolTag;

public class ConstantPoolClassEntry extends AbstractConstantPoolEntry {

	private int nameIndex;
	
	public ConstantPoolClassEntry(ConstantPool pool, int nameIndex) {
		super(pool);
		this.nameIndex = nameIndex;
	}
	
	@Override
	public ConstantPoolTag getTag() {
		return ConstantPoolTag.CLASS;
	}
	
	public ConstantPoolUTF8Entry getName() {
		return getConstantPool().getEntry(nameIndex).as(ConstantPoolUTF8Entry.class);
	}
	
}
