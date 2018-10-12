package me.mrletsplay.mrcore.misc.classfile;

import java.util.Arrays;

import me.mrletsplay.mrcore.misc.EnumFlagCompound;
import me.mrletsplay.mrcore.misc.classfile.attribute.Attribute;
import me.mrletsplay.mrcore.misc.classfile.attribute.DefaultAttributeType;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolUTF8Entry;

public class ClassMethod {

	private ClassFile classFile;
	private EnumFlagCompound<MethodAccessFlag> accessFlags;
	private int nameIndex;
	private int descriptorIndex;
	private Attribute[] attributes;
	
	public ClassMethod(ClassFile classFile, int accessFlags, int nameIndex, int descriptorIndex, Attribute[] attributes) {
		this.classFile = classFile;
		this.accessFlags = EnumFlagCompound.of(MethodAccessFlag.class, accessFlags);
		this.nameIndex = nameIndex;
		this.descriptorIndex = descriptorIndex;
		this.attributes = attributes;
	}
	
	public EnumFlagCompound<MethodAccessFlag> getAccessFlags() {
		return accessFlags;
	}
	
	public ConstantPoolUTF8Entry getName() {
		return classFile.getConstantPool().getEntry(nameIndex).as(ConstantPoolUTF8Entry.class);
	}
	
	public ConstantPoolUTF8Entry getDescriptor() {
		return classFile.getConstantPool().getEntry(descriptorIndex).as(ConstantPoolUTF8Entry.class);
	}
	
	public MethodDescriptor getMethodDescriptor() {
		return new MethodDescriptor(getName().getValue(), accessFlags, getDescriptor().getValue());
	}
	
	public Attribute[] getAttributes() {
		return attributes;
	}
	
	public Attribute getAttribute(String name) {
		return Arrays.stream(attributes).filter(a -> a.getNameString().equals(name)).findFirst().orElse(null);
	}
	
	public Attribute getAttribute(DefaultAttributeType type) {
		return Arrays.stream(attributes).filter(a -> a.getNameString().equals(type.getName())).findFirst().orElse(null);
	}
	
	@Override
	public String toString() {
//		return new StringBuilder()
//				.append(accessFlags.getApplicable().stream().map(a -> a.name).collect(Collectors.joining(" "))).append(" ")
//				.append(getName().getValue()).append(getDescriptor().getValue()).toString();
		return getMethodDescriptor().toString();
	}
	
}
