package me.mrletsplay.mrcore.misc.classfile.attribute;

import me.mrletsplay.mrcore.misc.classfile.ClassFile;

public interface Attribute {

	public String getNameString();
	
	public byte[] getInfo();
	
	public ClassFile getClassFile();
	
	public default <T extends Attribute> T as(Class<T> clazz) {
		if(!clazz.isInstance(this)) throw new RuntimeException("Illegal attribute subtype");
		return clazz.cast(this);
	}
	
}
