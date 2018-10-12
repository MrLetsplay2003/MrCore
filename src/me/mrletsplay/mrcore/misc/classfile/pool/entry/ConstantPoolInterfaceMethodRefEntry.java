package me.mrletsplay.mrcore.misc.classfile.pool.entry;

import me.mrletsplay.mrcore.misc.classfile.pool.ConstantPool;
import me.mrletsplay.mrcore.misc.classfile.pool.ConstantPoolTag;

public class ConstantPoolInterfaceMethodRefEntry extends AbstractConstantPoolRefEntry {

	public ConstantPoolInterfaceMethodRefEntry(ConstantPool pool, int classIndex, int nameAndTypeIndex) {
		super(pool, classIndex, nameAndTypeIndex);
	}

	@Override
	public ConstantPoolTag getTag() {
		return ConstantPoolTag.INTERFACE_METHOD_REF;
	}
	
}
