package me.mrletsplay.mrcore.misc.classfile.pool.entry;

import me.mrletsplay.mrcore.misc.classfile.pool.ConstantPool;
import me.mrletsplay.mrcore.misc.classfile.pool.ConstantPoolTag;

public class ConstantPoolDoubleEntry extends AbstractConstantPoolEntry {

	private double value;
	
	public ConstantPoolDoubleEntry(ConstantPool pool, double value) {
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
	
	public double getValue() {
		return value;
	}
	
}
