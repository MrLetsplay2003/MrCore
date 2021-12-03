package me.mrletsplay.mrcore.misc.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.misc.EnumFlagCompound;
import me.mrletsplay.mrcore.misc.FriendlyException;
import me.mrletsplay.mrcore.misc.classfile.attribute.Attribute;
import me.mrletsplay.mrcore.misc.classfile.attribute.AttributeCode;
import me.mrletsplay.mrcore.misc.classfile.attribute.AttributeLocalVariableTable;
import me.mrletsplay.mrcore.misc.classfile.attribute.AttributeRaw;
import me.mrletsplay.mrcore.misc.classfile.attribute.AttributeRuntimeInvisibleAnnotations;
import me.mrletsplay.mrcore.misc.classfile.attribute.AttributeRuntimeInvisibleParameterAnnotations;
import me.mrletsplay.mrcore.misc.classfile.attribute.AttributeRuntimeVisibleAnnotations;
import me.mrletsplay.mrcore.misc.classfile.attribute.AttributeRuntimeVisibleParameterAnnotations;
import me.mrletsplay.mrcore.misc.classfile.attribute.AttributeSignature;
import me.mrletsplay.mrcore.misc.classfile.attribute.AttributeStackMapTable;
import me.mrletsplay.mrcore.misc.classfile.attribute.DefaultAttributeType;
import me.mrletsplay.mrcore.misc.classfile.pool.ConstantPool;
import me.mrletsplay.mrcore.misc.classfile.pool.ConstantPoolTag;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolClassEntry;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolDoubleEntry;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolEntry;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolFieldRefEntry;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolFloatEntry;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolIntegerEntry;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolInterfaceMethodRefEntry;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolInvokeDynamicEntry;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolLongEntry;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolMethodHandleEntry;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolMethodRefEntry;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolMethodTypeEntry;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolNameAndTypeEntry;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolStringEntry;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolUTF8Entry;
import me.mrletsplay.mrcore.misc.classfile.util.ClassFileUtils;

public class ClassFile {

	private int minorVersion, majorVersion;
	private ConstantPool constantPool;
	private EnumFlagCompound<ClassAccessFlag> accessFlags;
	private ConstantPoolClassEntry thisClass;
	private ConstantPoolClassEntry superClass;
	private ConstantPoolClassEntry[] interfaces;
	private ClassField[] fields;
	private ClassMethod[] methods;
	private Attribute[] attributes;
	
	public ClassFile(File fromFile) throws IOException {
		this(new FileInputStream(fromFile));
	}
	
	public ClassFile(InputStream fromInputStream) throws IOException {
		DataInputStream in = new DataInputStream(fromInputStream);
		if(in.readInt() != 0xCAFEBABE) throw new IllegalArgumentException("Not a valid .class file");
		this.minorVersion = in.readUnsignedShort();
		this.majorVersion = in.readUnsignedShort();
		int cPoolCount = in.readUnsignedShort();
		constantPool = new ConstantPool(cPoolCount);
		for(int i = 1; i < cPoolCount; i++) {
			ConstantPoolEntry en = readConstantPoolEntry(constantPool, in);
			constantPool.setEntry(i, en);
			if(en.isDoubleEntry()) constantPool.setEntry(++i, null);
		}
		this.accessFlags = EnumFlagCompound.of(ClassAccessFlag.class, in.readUnsignedShort());
		this.thisClass = constantPool.getEntry(in.readUnsignedShort()).as(ConstantPoolClassEntry.class);
		int superIdx = in.readUnsignedShort();
		this.superClass = superIdx == 0 ? null : constantPool.getEntry(superIdx).as(ConstantPoolClassEntry.class);
		this.interfaces = new ConstantPoolClassEntry[in.readUnsignedShort()];
		for(int i = 0; i < interfaces.length; i++) {
			interfaces[i] = constantPool.getEntry(in.readUnsignedShort()).as(ConstantPoolClassEntry.class);
		}
		this.fields = new ClassField[in.readUnsignedShort()];
		for(int i = 0; i < fields.length; i++) {
			int accFlags = in.readUnsignedShort();
			int nameIdx = in.readUnsignedShort();
			int descIdx = in.readUnsignedShort();
			Attribute[] attrs = new Attribute[in.readUnsignedShort()];
			for(int j = 0; j < attrs.length; j++) {
				attrs[j] = readAttribute(constantPool, in);
			}
			fields[i] = new ClassField(this, accFlags, nameIdx, descIdx, attrs);
		}
		this.methods = new ClassMethod[in.readUnsignedShort()];
		for(int i = 0; i < methods.length; i++) {
			int accFlags = in.readUnsignedShort();
			int nameIdx = in.readUnsignedShort();
			int descIdx = in.readUnsignedShort();
			Attribute[] attrs = new Attribute[in.readUnsignedShort()];
			for(int j = 0; j < attrs.length; j++) {
				attrs[j] = readAttribute(constantPool, in);
			}
			methods[i] = new ClassMethod(this, accFlags, nameIdx, descIdx, attrs);
		}
		this.attributes = new Attribute[in.readUnsignedShort()];
		for(int i = 0; i < attributes.length; i++) {
			attributes[i] = readAttribute(constantPool, in);
		}
	}
	
	public ClassFile(String name, String superClassName) {
		this.minorVersion = 0;
		this.majorVersion = 52; // Java 8
		this.constantPool = new ConstantPool(1);
		this.accessFlags = EnumFlagCompound.noneOf(ClassAccessFlag.class);
		this.thisClass = constantPool.getEntry(ClassFileUtils.getOrAppendClass(this, ClassFileUtils.getOrAppendUTF8(this, name))).as(ConstantPoolClassEntry.class);
		this.superClass = constantPool.getEntry(ClassFileUtils.getOrAppendClass(this, ClassFileUtils.getOrAppendUTF8(this, superClassName))).as(ConstantPoolClassEntry.class);
		this.interfaces = new ConstantPoolClassEntry[0];
		this.fields = new ClassField[0];
		this.methods = new ClassMethod[0];
		this.attributes = new Attribute[0];
	}
	
	public ClassFile(String name, Class<?> superClass) {
		this(name, className(superClass));
	}
	
	public ClassFile(String name) {
		this(name, Object.class);
	}
	
	private static String className(Class<?> clazz) {
		if(clazz.isInterface() || clazz.isArray() || clazz.isEnum()) throw new IllegalArgumentException("Illegal superclass");
		return clazz.getCanonicalName().replace('.', '/');
	}
	
	private ConstantPoolEntry readConstantPoolEntry(ConstantPool pool, DataInputStream in) throws IOException {
		int uByte = in.readUnsignedByte();
		ConstantPoolTag tag = ConstantPoolTag.getByValue(uByte);
		if(tag == null) throw new IllegalArgumentException("Invalid constant pool (Invalid type: 0x" + Integer.toHexString(uByte) + ")");
		switch(tag) {
			case CLASS:
				return new ConstantPoolClassEntry(constantPool, in.readUnsignedShort());
			case FIELD_REF:
				return new ConstantPoolFieldRefEntry(pool, in.readUnsignedShort(), in.readUnsignedShort());
			case METHOD_REF:
				return new ConstantPoolMethodRefEntry(pool, in.readUnsignedShort(), in.readUnsignedShort());
			case INTERFACE_METHOD_REF:
				return new ConstantPoolInterfaceMethodRefEntry(pool, in.readUnsignedShort(), in.readUnsignedShort());
			case STRING:
				return new ConstantPoolStringEntry(pool, in.readUnsignedShort());
			case INTEGER:
				return new ConstantPoolIntegerEntry(pool, in.readInt());
			case FLOAT:
				return new ConstantPoolFloatEntry(pool, in.readFloat());
			case LONG:
				return new ConstantPoolLongEntry(pool, in.readLong());
			case DOUBLE:
				return new ConstantPoolDoubleEntry(pool, in.readDouble());
			case NAME_AND_TYPE:
				return new ConstantPoolNameAndTypeEntry(pool, in.readUnsignedShort(), in.readUnsignedShort());
			case UTF_8:
				byte[] b = new byte[in.readUnsignedShort()];
				in.read(b);
				return new ConstantPoolUTF8Entry(pool, new String(b, StandardCharsets.UTF_8));
			case METHOD_HANDLE:
				return new ConstantPoolMethodHandleEntry(pool, in.readUnsignedByte(), in.readUnsignedShort());
			case METHOD_TYPE:
				return new ConstantPoolMethodTypeEntry(pool, in.readUnsignedShort());
			case INVOKE_DYNAMIC:
				return new ConstantPoolInvokeDynamicEntry(pool, in.readUnsignedShort(), in.readUnsignedShort());
			default:
				throw new FriendlyException("Unhandled constant pool tag");
		}
	}
	
	public Attribute readAttribute(ConstantPool pool, DataInputStream in) throws IOException {
		int aNameIdx = in.readUnsignedShort();
		byte[] info = new byte[in.readInt()];
		in.read(info);
		return parseAttribute(new AttributeRaw(this, pool.getEntry(aNameIdx).as(ConstantPoolUTF8Entry.class), info));
	}
	
	private Attribute parseAttribute(AttributeRaw attr) throws IOException {
		DefaultAttributeType defType = DefaultAttributeType.getByName(attr.getNameString());
		if(defType == null) return attr;
		switch(defType) {
			case CODE:
				return new AttributeCode(this, attr.getName(), attr.getInfo());
			case ANNOTATION_DEFAULT:
				break;
			case BOOTSTRAP_METHODS:
				break;
			case CONSTANT_VALUE:
				break;
			case DEPRECATED:
				break;
			case ENCLOSING_METHOD:
				break;
			case EXCEPTIONS:
				break;
			case INNER_CLASSES:
				break;
			case LINE_NUMBER_TABLE:
				break;
			case LOCAL_VARIABLE_TABLE:
				return new AttributeLocalVariableTable(this, attr.getName(), attr.getInfo());
			case LOCAL_VARIABLE_TYPE_TABLE:
				break;
			case RUNTIME_INVISIBLE_ANNOTATIONS:
				return new AttributeRuntimeInvisibleAnnotations(this, attr.getName(), attr.getInfo());
			case RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS:
				return new AttributeRuntimeInvisibleParameterAnnotations(this, attr.getName(), attr.getInfo());
			case RUNTIME_VISIBLE_ANNOTATIONS:
				return new AttributeRuntimeVisibleAnnotations(this, attr.getName(), attr.getInfo());
			case RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS:
				return new AttributeRuntimeVisibleParameterAnnotations(this, attr.getName(), attr.getInfo());
			case SIGNATURE:
				return new AttributeSignature(this, attr.getName(), attr.getInfo());
			case SOURCE_DEBUG_EXCEPTION:
				break;
			case SOURCE_FILE:
				break;
			case STACK_MAP_TABLE:
				return new AttributeStackMapTable(this, attr.getName(), attr.getInfo());
			case SYNTHETIC:
				break;
		}
		return attr;
	}
	
	public int getMajorVersion() {
		return majorVersion;
	}
	
	public int getMinorVersion() {
		return minorVersion;
	}
	
	public ConstantPool getConstantPool() {
		return constantPool;
	}
	
	public EnumFlagCompound<ClassAccessFlag> getAccessFlags() {
		return accessFlags;
	}
	
	public void setThisClass(ConstantPoolClassEntry thisClass) {
		this.thisClass = thisClass;
	}
	
	public void setThisClass(int thisClassIndex) {
		ConstantPoolEntry en = constantPool.getEntry(thisClassIndex);
		if(en == null || !en.getTag().equals(ConstantPoolTag.CLASS)) throw new IllegalArgumentException("Not a valid index");
		setThisClass(en.as(ConstantPoolClassEntry.class));
	}
	
	public ConstantPoolClassEntry getThisClass() {
		return thisClass;
	}
	
	public void setSuperClass(ConstantPoolClassEntry superClass) {
		this.superClass = superClass;
	}
	
	public void setSuperClass(int superClassIndex) {
		if(superClassIndex == 0) {
			setSuperClass(null);
			return;
		}
		
		ConstantPoolEntry en = constantPool.getEntry(superClassIndex);
		if(en == null || !en.getTag().equals(ConstantPoolTag.CLASS)) throw new IllegalArgumentException("Not a valid index");
		setSuperClass(en.as(ConstantPoolClassEntry.class));
	}
	
	public ConstantPoolClassEntry getSuperClass() {
		return superClass;
	}
	
	public void setInterfaces(ConstantPoolClassEntry[] interfaces) {
		this.interfaces = interfaces;
	}
	
	public ConstantPoolClassEntry[] getInterfaces() {
		return interfaces;
	}
	
	public void setFields(ClassField[] fields) {
		this.fields = fields;
	}
	
	public ClassField[] getFields() {
		return fields;
	}
	
	public void setMethods(ClassMethod[] methods) {
		this.methods = methods;
	}
	
	public ClassMethod[] getMethods() {
		return methods;
	}
	
	public ClassMethod[] getMethods(String name) {
		return Arrays.stream(methods).filter(m -> m.getName().getValue().equals(name)).toArray(ClassMethod[]::new);
	}
	
	public ClassMethod[] getConstructors() {
		return getMethods("<init>");
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
	
	public void write(OutputStream out) throws IOException {
		DataOutputStream dOut = new DataOutputStream(out);
		dOut.writeInt(0xCAFEBABE);
		dOut.writeShort(minorVersion);
		dOut.writeShort(majorVersion);
		dOut.writeShort(constantPool.getSize() + 1);
		for(ConstantPoolEntry en : constantPool.getEntries()) {
			if(en == null) continue;
			writeConstantPoolEntry(dOut, constantPool, en);
		}
		dOut.writeShort((int) accessFlags.getCompound());
		dOut.writeShort(constantPool.indexOf(thisClass));
		dOut.writeShort(superClass == null ? 0 : constantPool.indexOf(superClass));
		dOut.writeShort(interfaces.length);
		for(ConstantPoolClassEntry en : interfaces) {
			dOut.writeShort(constantPool.indexOf(en));
		}
		dOut.writeShort(fields.length);
		for(ClassField f : fields) {
			dOut.writeShort((int) f.getAccessFlags().getCompound());
			dOut.writeShort(constantPool.indexOf(f.getName()));
			dOut.writeShort(constantPool.indexOf(f.getDescriptor()));
			dOut.writeShort(f.getAttributes().length);
			for(Attribute a : f.getAttributes()) {
				writeAttribute(dOut, constantPool, a);
			}
		}
		dOut.writeShort(methods.length);
		for(ClassMethod m : methods) {
			dOut.writeShort((int) m.getAccessFlags().getCompound());
			dOut.writeShort(constantPool.indexOf(m.getName()));
			dOut.writeShort(constantPool.indexOf(m.getDescriptor()));
			dOut.writeShort(m.getAttributes().length);
			for(Attribute a : m.getAttributes()) {
				writeAttribute(dOut, constantPool, a);
			}
		}
		dOut.writeShort(attributes.length);
		for(Attribute a : attributes) {
			writeAttribute(dOut, constantPool, a);
		}
	}
	
	public void writeConstantPoolEntry(DataOutputStream dOut, ConstantPool pool, ConstantPoolEntry entry) throws IOException {
		dOut.writeByte(entry.getTag().getValue());
		switch(entry.getTag()) {
			case CLASS:
			{
				ConstantPoolClassEntry en = (ConstantPoolClassEntry) entry;
				dOut.writeShort(pool.indexOf(en.getName()));
				break;
			}
			case FIELD_REF:
			{
				ConstantPoolFieldRefEntry en = (ConstantPoolFieldRefEntry) entry;
				dOut.writeShort(pool.indexOf(en.getClassInfo()));
				dOut.writeShort(pool.indexOf(en.getNameAndType()));
				break;
			}
			case METHOD_REF:
			{
				ConstantPoolMethodRefEntry en = (ConstantPoolMethodRefEntry) entry;
				dOut.writeShort(pool.indexOf(en.getClassInfo()));
				dOut.writeShort(pool.indexOf(en.getNameAndType()));
				break;
			}
			case INTERFACE_METHOD_REF:
			{
				ConstantPoolInterfaceMethodRefEntry en = (ConstantPoolInterfaceMethodRefEntry) entry;
				dOut.writeShort(pool.indexOf(en.getClassInfo()));
				dOut.writeShort(pool.indexOf(en.getNameAndType()));
				break;
			}
			case STRING:
			{
				ConstantPoolStringEntry en = (ConstantPoolStringEntry) entry;
				dOut.writeShort(pool.indexOf(en.getString()));
				break;
			}
			case INTEGER:
			{
				ConstantPoolIntegerEntry en = (ConstantPoolIntegerEntry) entry;
				dOut.writeInt(en.getValue());
				break;
			}
			case FLOAT:
			{
				ConstantPoolFloatEntry en = (ConstantPoolFloatEntry) entry;
				dOut.writeFloat(en.getValue());
				break;
			}
			case LONG:
			{
				ConstantPoolLongEntry en = (ConstantPoolLongEntry) entry;
				dOut.writeLong(en.getValue());
				break;
			}
			case DOUBLE:
			{
				ConstantPoolDoubleEntry en = (ConstantPoolDoubleEntry) entry;
				dOut.writeDouble(en.getValue());
				break;
			}
			case NAME_AND_TYPE:
			{
				ConstantPoolNameAndTypeEntry en = (ConstantPoolNameAndTypeEntry) entry;
				dOut.writeShort(pool.indexOf(en.getName()));
				dOut.writeShort(pool.indexOf(en.getDescriptor()));
				break;
			}
			case UTF_8:
			{
				ConstantPoolUTF8Entry en = (ConstantPoolUTF8Entry) entry;
				byte[] bs = en.getValue().getBytes(StandardCharsets.UTF_8);
				dOut.writeShort(bs.length);
				dOut.write(bs);
				break;
			}
			case METHOD_HANDLE:
			{
				ConstantPoolMethodHandleEntry en = (ConstantPoolMethodHandleEntry) entry;
				dOut.writeByte(en.getReferenceType().getKind());
				dOut.writeShort(pool.indexOf(en.getReference()));
				break;
			}
			case METHOD_TYPE:
			{
				ConstantPoolMethodTypeEntry en = (ConstantPoolMethodTypeEntry) entry;
				dOut.writeShort(pool.indexOf(en.getDescriptor()));
				break;
			}
			case INVOKE_DYNAMIC:
			{
				ConstantPoolInvokeDynamicEntry en = (ConstantPoolInvokeDynamicEntry) entry;
				dOut.writeShort(en.getBootstrapMethodAttributeIndex());
				dOut.writeShort(pool.indexOf(en.getNameAndType()));
				break;
			}
			default:
				throw new RuntimeException();
		}
	}
	
	public void writeAttribute(DataOutputStream dOut, ConstantPool pool, Attribute attr) throws IOException {
		dOut.writeShort(pool.indexOf(attr.getName()));
		dOut.writeInt(attr.getInfo().length);
		dOut.write(attr.getInfo());
	}
	
	@Override
	public String toString() {
		StringBuilder res = new StringBuilder();
		res.append(accessFlags.getApplicable().stream().map(a -> a.getName()).collect(Collectors.joining(" "))).append(" class ")
			.append(thisClass.getName().getValue().replace('/', '.')).append(" extends ").append(TypeDescriptor.parse(superClass.getName().getValue()).getFriendlyName());
		if(interfaces.length > 0) {
			res.append(" implements ").append(Arrays.stream(interfaces).map(i -> TypeDescriptor.parse(i.getName().getValue()).getFriendlyName()).collect(Collectors.joining(", ")));
		}
		res.append("\n\n");
		for(ClassField field : fields) res.append(field.toString()).append("\n");
		res.append("\n");
		for(ClassMethod method : methods) res.append(method.toString()).append("\n");
		return res.toString();
	}

}
