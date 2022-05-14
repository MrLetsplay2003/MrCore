package me.mrletsplay.mrcore.misc.classfile.pool.entry;

import me.mrletsplay.mrcore.misc.classfile.pool.ConstantPool;
import me.mrletsplay.mrcore.misc.classfile.pool.ConstantPoolTag;

public class ConstantPoolLongEntry extends AbstractConstantPoolEntry {

	private long value;
	
	public ConstantPoolLongEntry(ConstantPool pool, long value) {
		super(pool);
		this.value = value;
	}
	
	@Override
	public ConstantPoolTag getTag() {
		return ConstantPoolTag.LONG;
	}
	
	@Override
	public boolean isDoubleEntry() {
		return true;
	}
	
	public long getValue() {
		return value;
	}
	
}
