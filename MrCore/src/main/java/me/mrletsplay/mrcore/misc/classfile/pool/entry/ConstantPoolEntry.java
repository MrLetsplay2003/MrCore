package me.mrletsplay.mrcore.misc.classfile.pool.entry;

import me.mrletsplay.mrcore.misc.classfile.pool.ConstantPool;
import me.mrletsplay.mrcore.misc.classfile.pool.ConstantPoolTag;

public interface ConstantPoolEntry {

	public ConstantPool getConstantPool();
	
	public ConstantPoolTag getTag();
	
	public default boolean isDoubleEntry() {
		return false;
	}
	
	public default <T extends ConstantPoolEntry> T as(Class<T> clazz) {
		if(!clazz.isInstance(this)) throw new RuntimeException("Invalid constant pool (Invalid type: " + this.getClass() + ", should be " + clazz + ")");
		return clazz.cast(this);
	}
	
}
