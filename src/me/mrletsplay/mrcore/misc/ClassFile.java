package me.mrletsplay.mrcore.misc;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.misc.FlagCompound.Flag;

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
		for(int i = 0; i < cPoolCount - 1; i++) {
			ConstantPoolEntry en = readConstantPoolEntry(constantPool, in);
			constantPool.entries[i] = en;
			if(en.isDoubleEntry()) constantPool.entries[++i] = null;
		}
		this.accessFlags = EnumFlagCompound.of(ClassAccessFlag.class, in.readUnsignedShort());
		this.thisClass = constantPool.getEntry(in.readUnsignedShort()).as(ConstantPoolClassEntry.class);
		this.superClass = constantPool.getEntry(in.readUnsignedShort()).as(ConstantPoolClassEntry.class);
		this.interfaces = new ConstantPoolClassEntry[in.readUnsignedShort()];
		for(int i = 0; i < interfaces.length; i++) {
			interfaces[i] = constantPool.getEntry(in.readUnsignedShort()).as(ConstantPoolClassEntry.class);
		}
		this.fields = new ClassField[in.readShort()];
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
		this.methods = new ClassMethod[in.readShort()];
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
	
	private ConstantPoolEntry readConstantPoolEntry(ConstantPool pool, DataInputStream in) throws IOException {
		int uByte = in.readUnsignedByte();
		ConstantPoolTag tag = ConstantPoolTag.getByValue(uByte);
		if(tag == null) {
			throw new IllegalArgumentException("Invalid constant pool (Invalid type: 0x" + Integer.toHexString(uByte) + ")");
		}
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
		}
		throw new RuntimeException();
	}
	
	private Attribute readAttribute(ConstantPool pool, DataInputStream in) throws IOException {
		int aNameIdx = in.readUnsignedShort();
		byte[] info = new byte[in.readInt()];
		in.read(info);
		return parseAttribute(new AttributeRaw(this, aNameIdx, info));
	}
	
	private Attribute parseAttribute(AttributeRaw attr) throws IOException {
		DefaultAttributeType defType = DefaultAttributeType.getByName(attr.getNameString());
		if(defType == null) return attr;
		switch(defType) {
			case ANNOTATION_DEFAULT:
				break;
			case BOOTSTRAP_METHODS:
				break;
			case CODE:
				return new AttributeCode(this, attr.getInfo());
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
			case LOCAL_VARIABLE_TYPE:
				break;
			case LOCAL_VARIABLE_TYPE_TABLE:
				break;
			case RUNTIME_INVISIBLE_ANNOTATIONS:
				break;
			case RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS:
				break;
			case RUNTIME_VISIBLE_ANNOTATIONS:
				break;
			case RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS:
				break;
			case SIGNATURE:
				break;
			case SOURCE_DEBUG_EXCEPTION:
				break;
			case SOURCE_FILE:
				break;
			case STACK_MAP_TABLE:
				break;
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
	
	public ConstantPoolClassEntry getThisClass() {
		return thisClass;
	}
	
	public ConstantPoolClassEntry getSuperClass() {
		return superClass;
	}
	
	public ConstantPoolClassEntry[] getInterfaces() {
		return interfaces;
	}
	
	public ClassField[] getFields() {
		return fields;
	}
	
	public ClassMethod[] getMethods() {
		return methods;
	}
	
	public ClassMethod[] getMethods(String name) {
		return Arrays.stream(methods).filter(m -> m.getName().getValue().equals(name)).toArray(ClassMethod[]::new);
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
		StringBuilder res = new StringBuilder();
		res.append(accessFlags.getApplicable().stream().map(a -> a.name).collect(Collectors.joining(" "))).append(" class ")
			.append(new TypeDescriptor(thisClass.getName().getValue()).getFriendlyName()).append(" extends ").append(new TypeDescriptor(superClass.getName().getValue()).getFriendlyName());
		if(interfaces.length > 0) {
			res.append(" implements ").append(Arrays.stream(interfaces).map(i -> TypeDescriptor.parse(i.getName().getValue()).getFriendlyName()).collect(Collectors.joining(", ")));
		}
		res.append("\n\n");
		for(ClassField field : fields) res.append(field.toString()).append("\n");
		res.append("\n");
		for(ClassMethod method : methods) res.append(method.toString()).append("\n");
		return res.toString();
	}
	
	public static enum ClassAccessFlag implements Flag {
		
		PUBLIC("public", 0x1),
		FINAL("final", 0x10),
//		SUPER("super", 0x20),
		INTERFACE("interface", 0x200),
		ABSTRACT("abstract", 0x400),
		SYNTHETIC("synthetic", 0x1000),
		ANNOTATION("annotation", 0x2000),
		ENUM("enum", 0x4000);
		
		private final String name;
		private final long value;
		
		private ClassAccessFlag(String name, long value) {
			this.name = name;
			this.value = value;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public long getValue() {
			return value;
		}
		
	}
	
	public static enum FieldAccessFlag implements Flag {
		
		PUBLIC("public", 0x1),
		PRIVATE("private", 0x2),
		PROTECTED("protected", 0x4),
		STATIC("static", 0x8),
		FINAL("final", 0x10),
		VOLATILE("volatile", 0x40),
		TRANSIENT("transient", 0x80),
		SYNTHETIC("synthetic", 0x1000),
		ENUM("enum", 0x4000);
		
		private final String name;
		private final long value;
		
		private FieldAccessFlag(String name, long value) {
			this.name = name;
			this.value = value;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public long getValue() {
			return value;
		}
		
	}
	
	public static class ClassField {

		private ClassFile classFile;
		private EnumFlagCompound<FieldAccessFlag> accessFlags;
		private int nameIndex;
		private int descriptorIndex;
		private Attribute[] attributes;
		
		public ClassField(ClassFile classFile, int accessFlags, int nameIndex, int descriptorIndex, Attribute[] attributes) {
			this.classFile = classFile;
			this.accessFlags = EnumFlagCompound.of(FieldAccessFlag.class, accessFlags);
			this.nameIndex = nameIndex;
			this.descriptorIndex = descriptorIndex;
			this.attributes = attributes;
		}
		
		public EnumFlagCompound<FieldAccessFlag> getAccessFlags() {
			return accessFlags;
		}
		
		public ConstantPoolUTF8Entry getName() {
			return classFile.getConstantPool().getEntry(nameIndex).as(ConstantPoolUTF8Entry.class);
		}
		
		public ConstantPoolUTF8Entry getDescriptor() {
			return classFile.getConstantPool().getEntry(descriptorIndex).as(ConstantPoolUTF8Entry.class);
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
			return new StringBuilder().append(accessFlags.getApplicable().stream().map(a -> a.name).collect(Collectors.joining(" "))).append(" ")
					.append(new TypeDescriptor(getDescriptor().getValue()).getFriendlyName()).append(" ")
					.append(getName().getValue()).toString();
		}
		
	}
	
	public static enum MethodAccessFlag implements Flag {
		
		PUBLIC("public", 0x1),
		PRIVATE("private", 0x2),
		PROTECTED("protected", 0x4),
		STATIC("static", 0x8),
		FINAL("final", 0x10),
		SYNCHRONIZED("synchronized", 0x20),
		BRIDGE("bridge", 0x40),
		VARARGS("varargs", 0x80),
		NATIVE("native", 0x100),
		ABSTRACT("abstract", 0x400),
		STRICT("strictfp", 0x800),
		SYNTHETIC("synthetic", 0x1000);
		
		private final String name;
		private final long value;
		
		private MethodAccessFlag(String name, long value) {
			this.name = name;
			this.value = value;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public long getValue() {
			return value;
		}
		
	}
	
	public static class MethodDescriptor {
		
		private static final Pattern PATTERN_METHOD_SIGNATURE = Pattern.compile("\\((?<types>(.)*)\\)(?<return>(.)+)");
		
		private String name;
		private EnumFlagCompound<MethodAccessFlag> accessFlags;
		private ParameterDescriptor[] parameterDescriptors;
		private TypeDescriptor returnType;
		
		
		public MethodDescriptor(String name, EnumFlagCompound<MethodAccessFlag> accessFlags, String methodSignature) {
			this.name = name;
			this.accessFlags = accessFlags;
			Matcher m = PATTERN_METHOD_SIGNATURE.matcher(methodSignature);
			if(!m.matches()) throw new IllegalArgumentException("Invalid method signature");
			List<TypeDescriptor> parameterTypeDescriptors = TypeDescriptor.parseMulti(m.group("types"));
			parameterDescriptors = parameterTypeDescriptors.stream().map(t -> new ParameterDescriptor(t)).toArray(ParameterDescriptor[]::new);
			returnType = new TypeDescriptor(m.group("return"));
		}
		
		public MethodDescriptor(String name, TypeDescriptor returnType, ParameterDescriptor... parameterDescriptors) {
			this.name = name;
			this.parameterDescriptors = parameterDescriptors;
			this.returnType = returnType;
		}
		
		public boolean isConstructor() {
			return name.equals("<init>");
		}
		
		public String getName() {
			return name;
		}
		
		public ParameterDescriptor[] getParameterDescriptors() {
			return parameterDescriptors;
		}
		
		public TypeDescriptor getReturnType() {
			return returnType;
		}
		
		@Override
		public String toString() {
			return accessFlags.getApplicable().stream().map(f -> f.getName()).collect(Collectors.joining(" ")) + " " +
					returnType.getFriendlyName() + " " +
					name + "(" + Arrays.stream(parameterDescriptors).map(p -> p.getParameterType().getFriendlyName()).collect(Collectors.joining(", ")) + ")";
		}
		
	}
	
	public static class ParameterDescriptor {
		
		private TypeDescriptor parameterType;
//		private String name;
		
		public ParameterDescriptor(TypeDescriptor parameterType) {
			this.parameterType = parameterType;
		}
		
		public TypeDescriptor getParameterType() {
			return parameterType;
		}
		
	}
	
	public static class TypeDescriptor {
		
		private String rawDescriptor, className;
		private boolean isPrimitive;
		
		public TypeDescriptor(String rawDescriptor) {
			this.rawDescriptor = rawDescriptor;
			if(rawDescriptor.endsWith(";")) rawDescriptor = rawDescriptor.substring(0, rawDescriptor.length() - 1);
			rawDescriptor = rawDescriptor.replace('/', '.');
			isPrimitive = true;
			switch(rawDescriptor.charAt(0)) {
				case 'Z':
					className = "boolean";
					break;
				case 'B':
					className = "byte";
					break;
				case 'C':
					className = "char";
					break;
				case 'S':
					className = "short";
					break;
				case 'I':
					className = "int";
					break;
				case 'J':
					className = "long";
					break;
				case 'F':
					className = "float";
					break;
				case 'D':
					className = "double";
					break;
				case 'V':
					className = "void";
					break;
				case 'L':
					className = rawDescriptor.substring(1);
					isPrimitive = false;
					break;
				case '[':
					int i = 0;
					while(rawDescriptor.charAt(++i) == '[') continue;
					TypeDescriptor tDsc = new TypeDescriptor(rawDescriptor.substring(i));
					if(tDsc.isPrimitive()) {
						className = rawDescriptor;
					}else {
						className = rawDescriptor + ";";
					}
					isPrimitive = false;
					break;
				default:
					className = rawDescriptor;
					isPrimitive = false;
					break;
			}
		}
		
		public String getClassName() {
			return className;
		}
		
		public String getRawDescriptor() {
			return rawDescriptor;
		}
		
		public boolean isArray() {
			return className.charAt(0) == '[';
		}
		
		public boolean isPrimitive() {
			return isPrimitive;
		}
		
		public TypeDescriptor getArrayType() {
			if(!isArray()) throw new RuntimeException("Not an array descriptor");
			return new TypeDescriptor(className.substring(1));
		}
		
		protected Class<?> toClass() throws ClassNotFoundException {
			switch(className) {
				case "boolean":
					return boolean.class;
				case "char":
					return char.class;
				case "byte":
					return byte.class;
				case "short":
					return short.class;
				case "int":
					return int.class;
				case "long":
					return long.class;
				case "double":
					return double.class;
				case "float":
					return float.class;
				case "void":
					return void.class;
				default:
					return Class.forName(className);
			}
		}
		
		public String getFriendlyName() {
			if(isArray()) {
				return getArrayType().getFriendlyName() + "[]";
			}else {
				if(className.contains(".")) {
					return className.substring(className.lastIndexOf('.') + 1);
				}else {
					return className;
				}
			}
		}
		
		@Override
		public String toString() {
			return rawDescriptor;
		}
		
		public static TypeDescriptor parse(String rawDescriptor) {
			return new TypeDescriptor(rawDescriptor);
		}
		
		public static List<TypeDescriptor> parseMulti(String multi) {
			List<Character> cs = Arrays.asList('Z', 'B', 'C', 'S', 'I', 'J', 'F', 'D', 'V', 'L', '[');
			List<TypeDescriptor> ds = new ArrayList<>();
			int i = 0, sI = 0;
			while(i < multi.length()) {
				char c = multi.charAt(i);
				switch(c) {
					case 'L':
						while(multi.charAt(i++) != ';') continue;
						ds.add(new TypeDescriptor(multi.substring(sI, i)));
						sI = i;
						continue;
					case '[':
						i++;
						continue;
				}
				if(cs.contains(c)) {
					ds.add(new TypeDescriptor(multi.substring(sI, i + 1)));
					sI = i + 1;
					i++;
				}else {
					throw new IllegalArgumentException("Invalid multi type descriptor String \"" + multi + "\"");
				}
			}
			return ds;
		}
		
	}
	
	public static class ClassMethod {

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
//			return new StringBuilder()
//					.append(accessFlags.getApplicable().stream().map(a -> a.name).collect(Collectors.joining(" "))).append(" ")
//					.append(getName().getValue()).append(getDescriptor().getValue()).toString();
			return getMethodDescriptor().toString();
		}
		
	}
	
	public static enum DefaultAttributeType {
		
		CONSTANT_VALUE("ConstantValue"),
		CODE("Code"),
		STACK_MAP_TABLE("StackMapTable"),
		EXCEPTIONS("Exceptions"),
		INNER_CLASSES("InnerClasses"),
		ENCLOSING_METHOD("EnclosingMethod"),
		SYNTHETIC("Synthetic"),
		SIGNATURE("Signature"),
		SOURCE_FILE("SourceFile"),
		SOURCE_DEBUG_EXCEPTION("SourceDebugException"),
		LINE_NUMBER_TABLE("LineNumberTable"),
		LOCAL_VARIABLE_TYPE("LocalVariableType"),
		LOCAL_VARIABLE_TYPE_TABLE("LocalVariableTypeTable"),
		DEPRECATED("Deprecated"),
		RUNTIME_VISIBLE_ANNOTATIONS("RuntimeVisibleAnnotations"),
		RUNTIME_INVISIBLE_ANNOTATIONS("RuntimeInvisibleAnnotations"),
		RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS("RuntimeVisibleParameterAnnotations"),
		RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS("RuntimeInvisibleParameterAnnotations"),
		ANNOTATION_DEFAULT("AnnotationDefault"),
		BOOTSTRAP_METHODS("BootstrapMethods");
		
		private final String name;
		
		private DefaultAttributeType(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
		
		public static DefaultAttributeType getByName(String name) {
			return Arrays.stream(values()).filter(a -> a.getName().equals(name)).findFirst().orElse(null);
		}
		
	}
	
	public static interface Attribute {
		
		public String getNameString();
		
		public byte[] getInfo();
		
		public ClassFile getClassFile();
		
		public default <T extends Attribute> T as(Class<T> clazz) {
			if(!clazz.isInstance(this)) throw new RuntimeException("Illegal attribute subtype");
			return clazz.cast(this);
		}
		
	}
	
	public static class AttributeRaw implements Attribute {
		
		private ClassFile classFile;
		private int nameIndex;
		private byte[] info;
		
		public AttributeRaw(ClassFile classFile, int nameIndex, byte[] info) {
			this.classFile = classFile;
			this.nameIndex = nameIndex;
			this.info = info;
		}
		
		public ConstantPoolUTF8Entry getName() {
			return classFile.getConstantPool().getEntry(nameIndex).as(ConstantPoolUTF8Entry.class);
		}
		
		@Override
		public String getNameString() {
			return getName().getValue();
		}
		
		@Override
		public byte[] getInfo() {
			return info;
		}

		@Override
		public ClassFile getClassFile() {
			return classFile;
		}
		
	}
	
	public static interface DefaultAttribute extends Attribute {
		
		public DefaultAttributeType getType();
		
		@Override
		public default String getNameString() {
			return getType().getName();
		}
		
	}
	
	public static abstract class AbstractDefaultAttribute implements DefaultAttribute {
		
		private ClassFile classFile;
		private byte[] info;
		private DataInputStream input;
		
		public AbstractDefaultAttribute(ClassFile classFile, byte[] info) throws IOException {
			this.classFile = classFile;
			this.info = info;
			this.input = new DataInputStream(new ByteArrayInputStream(info));
		}
		
		protected DataInputStream getInput() {
			return input;
		}
		
		@Override
		public ClassFile getClassFile() {
			return classFile;
		}
		
		@Override
		public byte[] getInfo() {
			return info;
		}
		
	}
	
	public static class AttributeCode extends AbstractDefaultAttribute {

		private int maxStack, maxLocals;
		private byte[] code;
		private ExceptionHandler[] exceptionTable;
		private Attribute[] attributes;
		
		public AttributeCode(ClassFile classFile, byte[] info) throws IOException {
			super(classFile, info);
			DataInputStream in = getInput();
			this.maxStack = in.readUnsignedShort();
			this.maxLocals = in.readUnsignedShort();
			this.code = new byte[in.readInt()];
			in.read(this.code);
			this.exceptionTable = new ExceptionHandler[in.readUnsignedShort()];
			for(int i = 0; i < exceptionTable.length; i++) {
				exceptionTable[i] = new ExceptionHandler(classFile, in.readUnsignedShort(), in.readUnsignedShort(), in.readUnsignedShort(), in.readUnsignedShort());
			}
			this.attributes = new Attribute[in.readUnsignedShort()];
			for(int i = 0; i < attributes.length; i++) {
				attributes[i] = classFile.readAttribute(classFile.getConstantPool(), in);
			}
		}

		@Override
		public DefaultAttributeType getType() {
			return DefaultAttributeType.CODE;
		}
		
		public int getMaxStack() {
			return maxStack;
		}
		
		public int getMaxLocals() {
			return maxLocals;
		}
		
		public byte[] getByteCode() {
			return code;
		}
		
		public ExceptionHandler[] getExceptionTable() {
			return exceptionTable;
		}
		
		public Attribute[] getAttributes() {
			return attributes;
		}
		
	}
	
	public static class ExceptionHandler {
		
		private ClassFile classFile;
		private int startPC, endPC, handlerPC;
		private int catchTypeIndex;
		
		public ExceptionHandler(ClassFile classFile, int startPC, int endPC, int handlerPC, int catchTypeIndex) {
			this.classFile = classFile;
			this.startPC = startPC;
			this.endPC = endPC;
			this.handlerPC = handlerPC;
			this.catchTypeIndex = catchTypeIndex;
		}
		
		public int getStartPC() {
			return startPC;
		}
		
		public int getEndPC() {
			return endPC;
		}
		
		public int getHandlerPC() {
			return handlerPC;
		}
		
		public ConstantPoolClassEntry getCatchType() {
			if(catchTypeIndex == 0) return null;
			return classFile.getConstantPool().getEntry(catchTypeIndex).as(ConstantPoolClassEntry.class);
		}
		
	}
	
	public static class ConstantPool {
		
		private ConstantPoolEntry[] entries;
		
		public ConstantPool(int size) {
			this.entries = new ConstantPoolEntry[size - 1];
		}
		
		public ConstantPoolEntry getEntry(int index) {
			if(index > entries.length) throw new RuntimeException("Invalid constant pool (Index out of bounds, " + index + " > " + entries.length + ")");
			return entries[index - 1];
		}
		
		public ConstantPoolEntry[] getEntries() {
			return entries;
		}
		
	}
	
	public static enum ConstantPoolTag {
		
		CLASS(7),
		FIELD_REF(9),
		METHOD_REF(10),
		INTERFACE_METHOD_REF(11),
		STRING(8),
		INTEGER(3),
		FLOAT(4),
		LONG(5),
		DOUBLE(6),
		NAME_AND_TYPE(12),
		UTF_8(1),
		METHOD_HANDLE(15),
		METHOD_TYPE(16),
		INVOKE_DYNAMIC(18);
		
		private final int value;
		
		private ConstantPoolTag(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
		
		public static ConstantPoolTag getByValue(int value) {
			return Arrays.stream(values()).filter(t -> t.value == value).findFirst().orElse(null);
		}
		
	}
	
	public static interface ConstantPoolEntry {
		
		public ConstantPool getConstantPool();
		
		public ConstantPoolTag getTag();
		
		public default boolean isDoubleEntry() {
			return false;
		}
		
		public default <T extends ConstantPoolEntry> T as(Class<T> clazz) {
			if(!clazz.isInstance(this)) throw new RuntimeException("Invalid constant pool (Invalid type: " + this.getClass() + ", should be " + clazz + ")");
			return clazz.cast(this);
		}
		
	}
	
	public static abstract class AbstractConstantPoolEntry implements ConstantPoolEntry {
		
		private ConstantPool pool;
		
		public AbstractConstantPoolEntry(ConstantPool pool) {
			this.pool = pool;
		}
		
		@Override
		public ConstantPool getConstantPool() {
			return pool;
		}
		
	}
	
	public static class ConstantPoolClassEntry extends AbstractConstantPoolEntry {

		private int nameIndex;
		
		private ConstantPoolClassEntry(ConstantPool pool, int nameIndex) {
			super(pool);
			this.nameIndex = nameIndex;
		}
		
		@Override
		public ConstantPoolTag getTag() {
			return ConstantPoolTag.CLASS;
		}
		
		public ConstantPoolUTF8Entry getName() {
			return getConstantPool().getEntry(nameIndex).as(ConstantPoolUTF8Entry.class);
		}

	}
	
	public static abstract class AbstractConstantPoolRefEntry extends AbstractConstantPoolEntry { // TODO: Checks

		private int classIndex;
		private int nameAndTypeIndex;
		
		private AbstractConstantPoolRefEntry(ConstantPool pool, int classIndex, int nameAndTypeIndex) {
			super(pool);
			this.classIndex = classIndex;
			this.nameAndTypeIndex = nameAndTypeIndex;
		}
		
		public ConstantPoolClassEntry getClassInfo() {
			return getConstantPool().getEntry(classIndex).as(ConstantPoolClassEntry.class);
		}
		
		public ConstantPoolNameAndTypeEntry getNameAndType() {
			return getConstantPool().getEntry(nameAndTypeIndex).as(ConstantPoolNameAndTypeEntry.class);
		}

	}
	
	public static class ConstantPoolFieldRefEntry extends AbstractConstantPoolRefEntry {
		
		private ConstantPoolFieldRefEntry(ConstantPool pool, int classIndex, int nameAndTypeIndex) {
			super(pool, classIndex, nameAndTypeIndex);
		}

		@Override
		public ConstantPoolTag getTag() {
			return ConstantPoolTag.FIELD_REF;
		}
		
	}
	
	public static class ConstantPoolMethodRefEntry extends AbstractConstantPoolRefEntry {
		
		private ConstantPoolMethodRefEntry(ConstantPool pool, int classIndex, int nameAndTypeIndex) {
			super(pool, classIndex, nameAndTypeIndex);
		}

		@Override
		public ConstantPoolTag getTag() {
			return ConstantPoolTag.METHOD_REF;
		}
		
	}
	
	public static class ConstantPoolInterfaceMethodRefEntry extends AbstractConstantPoolRefEntry {
		
		private ConstantPoolInterfaceMethodRefEntry(ConstantPool pool, int classIndex, int nameAndTypeIndex) {
			super(pool, classIndex, nameAndTypeIndex);
		}

		@Override
		public ConstantPoolTag getTag() {
			return ConstantPoolTag.INTERFACE_METHOD_REF;
		}
		
	}
	
	public static class ConstantPoolStringEntry extends AbstractConstantPoolEntry {

		private int stringIndex;
		
		private ConstantPoolStringEntry(ConstantPool pool, int stringIndex) {
			super(pool);
			this.stringIndex = stringIndex;
		}
		
		@Override
		public ConstantPoolTag getTag() {
			return ConstantPoolTag.STRING;
		}
		
		public ConstantPoolUTF8Entry getString() {
			return getConstantPool().getEntry(stringIndex).as(ConstantPoolUTF8Entry.class);
		}

	}
	
	public static class ConstantPoolIntegerEntry extends AbstractConstantPoolEntry {

		private int value;
		
		private ConstantPoolIntegerEntry(ConstantPool pool, int value) {
			super(pool);
			this.value = value;
		}
		
		@Override
		public ConstantPoolTag getTag() {
			return ConstantPoolTag.INTEGER;
		}
		
		public int getValue() {
			return value;
		}

	}
	
	public static class ConstantPoolFloatEntry extends AbstractConstantPoolEntry {

		private float value;
		
		private ConstantPoolFloatEntry(ConstantPool pool, float value) {
			super(pool);
			this.value = value;
		}
		
		@Override
		public ConstantPoolTag getTag() {
			return ConstantPoolTag.FLOAT;
		}
		
		public float getValue() {
			return value;
		}

	}
	
	public static class ConstantPoolLongEntry extends AbstractConstantPoolEntry {

		private long value;
		
		private ConstantPoolLongEntry(ConstantPool pool, long value) {
			super(pool);
			this.value = value;
		}
		
		@Override
		public ConstantPoolTag getTag() {
			return ConstantPoolTag.LONG;
		}
		
		@Override
		public boolean isDoubleEntry() {
			return true;
		}
		
		public long getValue() {
			return value;
		}

	}
	
	public static class ConstantPoolDoubleEntry extends AbstractConstantPoolEntry {

		private double value;
		
		private ConstantPoolDoubleEntry(ConstantPool pool, double value) {
			super(pool);
			this.value = value;
		}
		
		@Override
		public ConstantPoolTag getTag() {
			return ConstantPoolTag.LONG;
		}
		
		@Override
		public boolean isDoubleEntry() {
			return true;
		}
		
		public double getValue() {
			return value;
		}

	}
	
	public static class ConstantPoolNameAndTypeEntry extends AbstractConstantPoolEntry {

		private int nameIndex, descriptorIndex;
		
		private ConstantPoolNameAndTypeEntry(ConstantPool pool, int nameIndex, int descriptorIndex) {
			super(pool);
			this.nameIndex = nameIndex;
			this.descriptorIndex = descriptorIndex;
		}
		
		@Override
		public ConstantPoolTag getTag() {
			return ConstantPoolTag.LONG;
		}
		
		public ConstantPoolUTF8Entry getName() {
			return getConstantPool().getEntry(nameIndex).as(ConstantPoolUTF8Entry.class);
		}
		
		public ConstantPoolUTF8Entry getDescriptor() {
			return getConstantPool().getEntry(descriptorIndex).as(ConstantPoolUTF8Entry.class);
		}

	}
	
	public static class ConstantPoolUTF8Entry extends AbstractConstantPoolEntry {

		private String value;
		
		private ConstantPoolUTF8Entry(ConstantPool pool, String value) {
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
	
	public static enum MethodHandleReferenceType {
		
		GET_FIELD(1),
		GET_STATIC(2),
		PUT_FIELD(3),
		PUT_STATIC(4),
		INVOKE_VIRTUAL(5),
		INVOKE_STATIC(6),
		INVOKE_SPECIAL(7),
		NEW_INVOKE_SPECIAL(8),
		INVOKE_INTERFACE(9);
		
		private final int kind;
		
		private MethodHandleReferenceType(int kind) {
			this.kind = kind;
		}
		
		public int getKind() {
			return kind;
		}
		
		public static MethodHandleReferenceType getByKind(int kind) {
			return Arrays.stream(values()).filter(r -> r.kind == kind).findFirst().orElse(null);
		}
		
	}
	
	public static class ConstantPoolMethodHandleEntry extends AbstractConstantPoolEntry {

		private MethodHandleReferenceType referenceType;
		private int referenceIndex;
		
		private ConstantPoolMethodHandleEntry(ConstantPool pool, int referenceKind, int referenceIndex) {
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
	
	public static class ConstantPoolMethodTypeEntry extends AbstractConstantPoolEntry {

		private int descriptorIndex;
		
		private ConstantPoolMethodTypeEntry(ConstantPool pool, int descriptorIndex) {
			super(pool);
			this.descriptorIndex = descriptorIndex;
		}

		@Override
		public ConstantPoolTag getTag() {
			return ConstantPoolTag.METHOD_TYPE;
		}
		
		public ConstantPoolUTF8Entry getDescriptor() {
			return getConstantPool().getEntry(descriptorIndex).as(ConstantPoolUTF8Entry.class);
		}
		
	}
	
	public static class ConstantPoolInvokeDynamicEntry extends AbstractConstantPoolEntry {

		private int bootstrapMethodAttributeIndex; // Bootstrap method
		private int nameAndTypeIndex;
		
		private ConstantPoolInvokeDynamicEntry(ConstantPool pool, int bootstrapMethodAttributeIndex, int nameAndTypeIndex) {
			super(pool);
			this.bootstrapMethodAttributeIndex = bootstrapMethodAttributeIndex;
			this.nameAndTypeIndex = nameAndTypeIndex;
		}

		@Override
		public ConstantPoolTag getTag() {
			return ConstantPoolTag.METHOD_TYPE;
		}
		
		public int getBootstrapMethodAttributeIndex() {
			return bootstrapMethodAttributeIndex;
		}
		
		public ConstantPoolNameAndTypeEntry getNameAndType() {
			return getConstantPool().getEntry(nameAndTypeIndex).as(ConstantPoolNameAndTypeEntry.class);
		}
		
	}

}
