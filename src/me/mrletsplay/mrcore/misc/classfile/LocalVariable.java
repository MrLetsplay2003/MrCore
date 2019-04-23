package me.mrletsplay.mrcore.misc.classfile;

import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolUTF8Entry;

public class LocalVariable {

	private int startPC, length;
	private ConstantPoolUTF8Entry name, descriptor;
	private int index;
	
	public LocalVariable(int startPC, int length, ConstantPoolUTF8Entry name, ConstantPoolUTF8Entry descriptor, int index) {
		this.startPC = startPC;
		this.length = length;
		this.name = name;
		this.descriptor = descriptor;
		this.index = index;
	}
	
	public int getStartPC() {
		return startPC;
	}
	
	public int getLength() {
		return length;
	}
	
	public ConstantPoolUTF8Entry getName() {
		return name;
	}
	
	public TypeDescriptor getTypeDescriptor() {
		return TypeDescriptor.parse(descriptor.getValue());
	}
	
	public ConstantPoolUTF8Entry getDescriptor() {
		return descriptor;
	}
	
	public int getIndex() {
		return index;
	}
	
}
