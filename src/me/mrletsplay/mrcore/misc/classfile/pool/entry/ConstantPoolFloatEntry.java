package me.mrletsplay.mrcore.misc.classfile.pool.entry;

import me.mrletsplay.mrcore.misc.classfile.pool.ConstantPool;
import me.mrletsplay.mrcore.misc.classfile.pool.ConstantPoolTag;

public class ConstantPoolFloatEntry extends AbstractConstantPoolEntry {

	private float value;
	
	public ConstantPoolFloatEntry(ConstantPool pool, float value) {
		super(pool);
		this.value = value;
	}
	
	@Override
	public ConstantPoolTag getTag() {
		return ConstantPoolTag.FLOAT;
	}
	
	public float getValue() {
		return value;
	}
	
}
