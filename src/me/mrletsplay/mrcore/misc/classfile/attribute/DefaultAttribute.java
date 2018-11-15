package me.mrletsplay.mrcore.misc.classfile.attribute;

public interface DefaultAttribute extends Attribute {

	public DefaultAttributeType getType();
	
	@Override
	public default String getNameString() {
		return getType().getName();
	}
	
}
