package me.mrletsplay.mrcore.misc.classfile;

import java.util.Arrays;

import me.mrletsplay.mrcore.misc.EnumFlagCompound;
import me.mrletsplay.mrcore.misc.classfile.attribute.Attribute;
import me.mrletsplay.mrcore.misc.classfile.attribute.AttributeCode;
import me.mrletsplay.mrcore.misc.classfile.attribute.DefaultAttributeType;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolUTF8Entry;
import me.mrletsplay.mrcore.misc.classfile.util.ClassFileUtils;

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
	
	public ClassMethod(ClassFile classFile, EnumFlagCompound<MethodAccessFlag> accessFlags, String name, String descriptor) {
		this.classFile = classFile;
		this.accessFlags = accessFlags;
		this.nameIndex = ClassFileUtils.getOrAppendUTF8(classFile, name);
		this.descriptorIndex = ClassFileUtils.getOrAppendUTF8(classFile, descriptor);
		this.attributes = new Attribute[0];
	}
	
	public ClassMethod(ClassFile classFile, MethodDescriptor methodDescriptor) {
		this.classFile = classFile;
		this.accessFlags = methodDescriptor.getAccessFlags();
		this.nameIndex = ClassFileUtils.getOrAppendUTF8(classFile, methodDescriptor.getName());
		this.descriptorIndex = ClassFileUtils.getOrAppendUTF8(classFile, methodDescriptor.getRawDescriptor());
		this.attributes = new Attribute[0];
	}
	
	public EnumFlagCompound<MethodAccessFlag> getAccessFlags() {
		return accessFlags;
	}
	
	public boolean isConstructor() {
		return getName().getValue().equals("<init>");
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
	
	public void setAttributes(Attribute[] attributes) {
		this.attributes = attributes;
	}
	
	public Attribute[] getAttributes() {
		return attributes;
	}
	
	public AttributeCode getCodeAttribute() {
		Attribute a = getAttribute(DefaultAttributeType.CODE);
		if(a == null) return null;
		return a.as(AttributeCode.class);
	}
	
	public Attribute getAttribute(String name) {
		return Arrays.stream(attributes).filter(a -> a.getNameString().equals(name)).findFirst().orElse(null);
	}
	
	public Attribute getAttribute(DefaultAttributeType type) {
		return Arrays.stream(attributes).filter(a -> a.getNameString().equals(type.getName())).findFirst().orElse(null);
	}
	
	@Override
	public String toString() {
		return getMethodDescriptor().toString();
	}
	
}
