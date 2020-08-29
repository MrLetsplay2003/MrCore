package me.mrletsplay.mrcore.misc.classfile.pool;

import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolEntry;

public class ConstantPool {

	private ConstantPoolEntry[] entries;
	
	public ConstantPool(int size) {
		this.entries = new ConstantPoolEntry[size - 1];
	}
	
	public void setEntry(int index, ConstantPoolEntry entry) {
		entries[index - 1] = entry;
	}
	
	public ConstantPoolEntry getEntry(int index) {
		if(index < 0) throw new RuntimeException("Invalid constant pool (Index out of bounds, " + index + " < 0)");
		if(index > entries.length) throw new RuntimeException("Invalid constant pool (Index out of bounds, " + index + " > " + entries.length + ")");
		return entries[index - 1];
	}
	
	public ConstantPoolEntry[] getEntries() {
		return entries;
	}
	
	public int appendEntry(ConstantPoolEntry entry) {
		ConstantPoolEntry[] nEns = new ConstantPoolEntry[entries.length + 1];
		System.arraycopy(entries, 0, nEns, 0, entries.length);
		nEns[entries.length] = entry;
		entries = nEns;
		return entries.length;
	}
	
	public int getSize() {
		return entries.length;
	}
	
	public int indexOf(ConstantPoolEntry entry) {
		for(int i = 0; i < entries.length; i++) {
			if(entries[i].equals(entry)) return i + 1;
		}
		throw new IllegalArgumentException("Entry not in constant pool");
	}
	
}
