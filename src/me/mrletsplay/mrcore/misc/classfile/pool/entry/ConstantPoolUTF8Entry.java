package me.mrletsplay.mrcore.misc.classfile.pool.entry;

import me.mrletsplay.mrcore.misc.classfile.pool.ConstantPool;
import me.mrletsplay.mrcore.misc.classfile.pool.ConstantPoolTag;

public class ConstantPoolUTF8Entry extends AbstractConstantPoolEntry {

	private String value;
	
	public ConstantPoolUTF8Entry(ConstantPool pool, String value) {
		super(pool);
		this.value = value;
	}
	
	@Override
	public ConstantPoolTag getTag() {
		return ConstantPoolTag.UTF_8;
	}
	
	public String getValue() {
		return value;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof String) return obj.equals(value);
		return super.equals(obj);
	}
	
}
