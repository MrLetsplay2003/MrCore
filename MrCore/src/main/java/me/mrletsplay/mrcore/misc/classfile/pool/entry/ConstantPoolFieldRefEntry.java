package me.mrletsplay.mrcore.misc.classfile.pool.entry;

import me.mrletsplay.mrcore.misc.classfile.pool.ConstantPool;
import me.mrletsplay.mrcore.misc.classfile.pool.ConstantPoolTag;

public class ConstantPoolFieldRefEntry extends AbstractConstantPoolRefEntry {

	public ConstantPoolFieldRefEntry(ConstantPool pool, int classIndex, int nameAndTypeIndex) {
		super(pool, classIndex, nameAndTypeIndex);
	}

	@Override
	public ConstantPoolTag getTag() {
		return ConstantPoolTag.FIELD_REF;
	}
	
}
