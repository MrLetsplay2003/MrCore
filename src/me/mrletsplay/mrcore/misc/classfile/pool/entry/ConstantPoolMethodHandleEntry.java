package me.mrletsplay.mrcore.misc.classfile.pool.entry;

import me.mrletsplay.mrcore.misc.classfile.pool.ConstantPool;
import me.mrletsplay.mrcore.misc.classfile.pool.ConstantPoolTag;
import me.mrletsplay.mrcore.misc.classfile.pool.MethodHandleReferenceType;

public class ConstantPoolMethodHandleEntry extends AbstractConstantPoolEntry {

	private MethodHandleReferenceType referenceType;
	private int referenceIndex;
	
	public ConstantPoolMethodHandleEntry(ConstantPool pool, int referenceKind, int referenceIndex) {
		super(pool);
		this.referenceType = MethodHandleReferenceType.getByKind(referenceKind);
		this.referenceIndex = referenceIndex;
	}

	@Override
	public ConstantPoolTag getTag() {
		return ConstantPoolTag.METHOD_HANDLE;
	}
	
	public MethodHandleReferenceType getReferenceType() {
		return referenceType;
	}
	
	public ConstantPoolEntry getReference() {
		return getConstantPool().getEntry(referenceIndex);
	}
	
}
