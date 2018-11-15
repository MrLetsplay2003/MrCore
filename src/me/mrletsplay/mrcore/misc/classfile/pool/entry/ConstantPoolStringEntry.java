package me.mrletsplay.mrcore.misc.classfile.pool.entry;

import me.mrletsplay.mrcore.misc.classfile.pool.ConstantPool;
import me.mrletsplay.mrcore.misc.classfile.pool.ConstantPoolTag;

public class ConstantPoolStringEntry extends AbstractConstantPoolEntry {

	private int stringIndex;
	
	public ConstantPoolStringEntry(ConstantPool pool, int stringIndex) {
		super(pool);
		this.stringIndex = stringIndex;
	}
	
	@Override
	public ConstantPoolTag getTag() {
		return ConstantPoolTag.STRING;
	}
	
	public ConstantPoolUTF8Entry getString() {
		return getConstantPool().getEntry(stringIndex).as(ConstantPoolUTF8Entry.class);
	}
	
}
