package me.mrletsplay.mrcore.misc.classfile.pool;

import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolEntry;

public class ConstantPool {

	private ConstantPoolEntry[] entries;
	
	public ConstantPool(int size) {
		this.entries = new ConstantPoolEntry[size - 1];
	}
	
	public void setEntry(int index, ConstantPoolEntry entry) {
		entries[index] = entry;
	}
	
	public ConstantPoolEntry getEntry(int index) {
		if(index > entries.length) throw new RuntimeException("Invalid constant pool (Index out of bounds, " + index + " > " + entries.length + ")");
		return entries[index - 1];
	}
	
	public ConstantPoolEntry[] getEntries() {
		return entries;
	}
	
}
