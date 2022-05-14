package me.mrletsplay.mrcore.misc.classfile.pool.entry;

import me.mrletsplay.mrcore.misc.classfile.pool.ConstantPool;
import me.mrletsplay.mrcore.misc.classfile.pool.ConstantPoolTag;

public class ConstantPoolIntegerEntry extends AbstractConstantPoolEntry {

	private int value;
	
	public ConstantPoolIntegerEntry(ConstantPool pool, int value) {
		super(pool);
		this.value = value;
	}
	
	@Override
	public ConstantPoolTag getTag() {
		return ConstantPoolTag.INTEGER;
	}
	
	public int getValue() {
		return value;
	}
	
}
