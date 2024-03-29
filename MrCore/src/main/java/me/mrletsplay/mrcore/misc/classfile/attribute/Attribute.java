package me.mrletsplay.mrcore.misc.classfile.attribute;

import java.util.Arrays;

import me.mrletsplay.mrcore.misc.classfile.ClassFile;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolUTF8Entry;

public interface Attribute {

	public int getNameIndex();

	public default ConstantPoolUTF8Entry getName() {
		return getClassFile().getConstantPool().getEntry(getNameIndex()).as(ConstantPoolUTF8Entry.class);
	}

	public default String getNameString() {
		return getName().getValue();
	}
	
	public byte[] getInfo();
	
	public ClassFile getClassFile();
	
	public default <T extends Attribute> T as(Class<T> clazz) {
		if(!clazz.isInstance(this)) throw new RuntimeException("Illegal attribute subtype");
		return clazz.cast(this);
	}
	
	public void setAttributes(Attribute[] attributes);
	
	public Attribute[] getAttributes();
	
	public default Attribute getAttribute(String name) {
		return Arrays.stream(getAttributes()).filter(a -> a.getNameString().equals(name)).findFirst().orElse(null);
	}
	
	public default Attribute getAttribute(DefaultAttributeType type) {
		return Arrays.stream(getAttributes()).filter(a -> a.getNameString().equals(type.getName())).findFirst().orElse(null);
	}
	
}
