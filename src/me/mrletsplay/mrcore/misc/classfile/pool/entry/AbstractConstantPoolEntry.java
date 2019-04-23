package me.mrletsplay.mrcore.misc.classfile.pool.entry;

import me.mrletsplay.mrcore.misc.classfile.pool.ConstantPool;

public abstract class AbstractConstantPoolEntry implements ConstantPoolEntry {

	private ConstantPool pool;
	
	public AbstractConstantPoolEntry(ConstantPool pool) {
		this.pool = pool;
	}
	
	@Override
	public ConstantPool getConstantPool() {
		return pool;
	}

}
