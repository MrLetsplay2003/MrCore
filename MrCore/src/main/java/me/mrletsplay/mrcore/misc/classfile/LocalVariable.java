package me.mrletsplay.mrcore.misc.classfile;

import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolUTF8Entry;
import me.mrletsplay.mrcore.misc.classfile.util.ClassFileUtils;

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
	
	public LocalVariable(ClassFile file, int startPC, int length, String name, String descriptor, int index) {
		this.startPC = startPC;
		this.length = length;
		this.name = file.getConstantPool().getEntry(ClassFileUtils.getOrAppendUTF8(file, name)).as(ConstantPoolUTF8Entry.class);
		this.descriptor = file.getConstantPool().getEntry(ClassFileUtils.getOrAppendUTF8(file, descriptor)).as(ConstantPoolUTF8Entry.class);
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
