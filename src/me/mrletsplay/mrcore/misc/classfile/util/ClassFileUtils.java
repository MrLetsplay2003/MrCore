package me.mrletsplay.mrcore.misc.classfile.util;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.Callable;

import me.mrletsplay.mrcore.misc.EnumFlagCompound;
import me.mrletsplay.mrcore.misc.FriendlyException;
import me.mrletsplay.mrcore.misc.MiscUtils;
import me.mrletsplay.mrcore.misc.PrimitiveType;
import me.mrletsplay.mrcore.misc.classfile.ByteCode;
import me.mrletsplay.mrcore.misc.classfile.ClassAccessFlag;
import me.mrletsplay.mrcore.misc.classfile.ClassFile;
import me.mrletsplay.mrcore.misc.classfile.ClassMethod;
import me.mrletsplay.mrcore.misc.classfile.Instruction;
import me.mrletsplay.mrcore.misc.classfile.InstructionInformation;
import me.mrletsplay.mrcore.misc.classfile.MethodAccessFlag;
import me.mrletsplay.mrcore.misc.classfile.MethodDescriptor;
import me.mrletsplay.mrcore.misc.classfile.ParameterDescriptor;
import me.mrletsplay.mrcore.misc.classfile.TypeDescriptor;
import me.mrletsplay.mrcore.misc.classfile.attribute.Attribute;
import me.mrletsplay.mrcore.misc.classfile.attribute.AttributeCode;
import me.mrletsplay.mrcore.misc.classfile.attribute.DefaultAttributeType;
import me.mrletsplay.mrcore.misc.classfile.pool.ConstantPool;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolClassEntry;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolDoubleEntry;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolFieldRefEntry;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolFloatEntry;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolIntegerEntry;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolInterfaceMethodRefEntry;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolMethodRefEntry;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolNameAndTypeEntry;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolStringEntry;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolUTF8Entry;

public class ClassFileUtils {
	
	public static ClassFile createStaticClassCopy(Class<?> ofClass, String canonicalName) {
		try {
			ClassFile file = new ClassFile(canonicalName.replace('.', '/'));
			file.getAccessFlags().addFlag(ClassAccessFlag.PUBLIC);
			
			List<ClassMethod> methods = new ArrayList<>();
			
			ClassMethod constr = new ClassMethod(file, new MethodDescriptor("<init>", EnumFlagCompound.of(MethodAccessFlag.PUBLIC), "()V"));
			
			int mInd = getOrAppendMethodRef(file, getOrAppendClass(file, getOrAppendUTF8(file, "java/lang/Object")), getOrAppendNameAndType(file, getOrAppendUTF8(file, "<init>"), getOrAppendUTF8(file, "()V")));
			
			AttributeCode code = new AttributeCode(file);
			code.getCode().replace(ByteCode.of(Arrays.asList(
				new InstructionInformation(Instruction.ALOAD_0),
				new InstructionInformation(Instruction.INVOKESPECIAL, getShortBytes(mInd)),
				new InstructionInformation(Instruction.RETURN)
			)));
			code.setMaxLocals(1);
			code.setMaxStack(1);
			constr.setAttributes(new Attribute[] {code});
			methods.add(constr);
			
			for(Method m : ofClass.getMethods()) {
				if(!Modifier.isStatic(m.getModifiers())) continue;
				MethodDescriptor md = getMethodDescriptor(m);
				ClassMethod mth = new ClassMethod(file, md);
				AttributeCode mCode = new AttributeCode(file);
				mCode.setMaxLocals(m.getParameterCount() * 2);
				mth.setAttributes(new Attribute[] {mCode});
				redirectMethodExecution(file, mth, ofClass.getCanonicalName().replace('.', '/'), md.getName(), md.getRawDescriptor());
				
//				AttributeLocalVariableTable t = ClassFileUtils.createAttribute(mCode, DefaultAttributeType.LOCAL_VARIABLE_TABLE, () -> new AttributeLocalVariableTable(file)).as(AttributeLocalVariableTable.class);
//				LocalVariable[] vs = new LocalVariable[m.getParameterCount()];
//				for(int i = 0; i < vs.length; i++) {
//					ParameterDescriptor p = md.getParameterDescriptors()[i];
//					vs[i] = new LocalVariable(file, 0, mCode.getCode().getBytes().length, "arg" + i, p.getParameterType().getRawDescriptor(), i);
//				}
//				t.setEntries(vs);
				
				methods.add(mth);
			}
			
			file.setMethods(methods.toArray(new ClassMethod[methods.size()]));
			
			return file;
		}catch(IOException e) {
			throw new FriendlyException(e);
		}
	}
	
	public static Attribute createAttribute(ClassMethod method, DefaultAttributeType type, Callable<? extends Attribute> initialValue) {
		Attribute a = method.getAttribute(type);
		if(a != null) return a;
		
		try {
			a = initialValue.call();
		} catch (Exception e) {
			throw new FriendlyException(e);
		}
		
		Attribute[] attrs = method.getAttributes();
		Attribute[] newAttrs = new Attribute[attrs.length + 1];
		System.arraycopy(attrs, 0, newAttrs, 0, attrs.length);
		newAttrs[newAttrs.length - 1] = a;
		method.setAttributes(newAttrs);
		
		return a;
	}
	
	public static Attribute createAttribute(Attribute attribute, DefaultAttributeType type, Callable<? extends Attribute> initialValue) {
		Attribute a = attribute.getAttribute(type);
		if(a != null) return a;
		
		try {
			a = initialValue.call();
		} catch (Exception e) {
			throw new FriendlyException(e);
		}
		
		Attribute[] attrs = attribute.getAttributes();
		Attribute[] newAttrs = new Attribute[attrs.length + 1];
		System.arraycopy(attrs, 0, newAttrs, 0, attrs.length);
		newAttrs[newAttrs.length - 1] = a;
		attribute.setAttributes(newAttrs);
		
		return a;
	}
	
	public static AttributeCode createCodeAttribute(ClassFile file, ClassMethod method) {
		return createAttribute(method, DefaultAttributeType.CODE, () -> new AttributeCode(file)).as(AttributeCode.class);
	}
	
	public static String getClassName(Class<?> clazz) {
		return clazz.getCanonicalName().replace('.', '/');
	}
	
	public static void redirectMethodExecution(ClassFile file, ClassMethod method, String toClass, String toMethodName, String toMethodSignature) {
		MethodDescriptor mDesc = method.getMethodDescriptor();
		
		AttributeCode codeAttr = (AttributeCode) method.getAttribute(DefaultAttributeType.CODE);
		List<InstructionInformation> nInstr = new ArrayList<>();
		
		int nInd = getOrAppendUTF8(file, toMethodName);
		int dInd = getOrAppendUTF8(file, toMethodSignature);
		int ntInd = getOrAppendNameAndType(file, nInd, dInd);
		int clNInd = getOrAppendUTF8(file, toClass);
		int clInd = getOrAppendClass(file, clNInd);
		int mrInd = getOrAppendMethodRef(file, clInd, ntInd);

		byte i = 0;
		boolean isStatic = method.getAccessFlags().hasFlag(MethodAccessFlag.STATIC);
		if(!isStatic) { // Only add this class instance for instance methods
			nInstr.add(new InstructionInformation(Instruction.ALOAD_0));
			i++;
		}
		
		for(ParameterDescriptor p : mDesc.getParameterDescriptors()) {
			PrimitiveType t = p.getParameterType().getPrimitiveType();
			nInstr.add(getInfo(t, i++));
			if(t.equals(PrimitiveType.DOUBLE) || t.equals(PrimitiveType.LONG)) i++;
		}
		
		nInstr.add(new InstructionInformation(Instruction.INVOKESTATIC, getShortBytes(mrInd)));
		
		PrimitiveType pt = mDesc.getReturnType().getPrimitiveType();
		
		nInstr.add(new InstructionInformation(getReturnInstruction(pt)));

		codeAttr.setMaxStack(i + 2); // #params * 2 (longs are 2x) + 2 for possible return value (can be long)
		
		codeAttr.getCode().replace(ByteCode.of(nInstr));
	}
	
	public static void redirectMethodExecutionNamed(ClassFile file, ClassMethod method, String toClass, String toMethodName, String toMethodSignature) {
		MethodDescriptor mDesc = method.getMethodDescriptor();
		
		AttributeCode codeAttr = (AttributeCode) method.getAttribute(DefaultAttributeType.CODE);
		List<InstructionInformation> nInstr = new ArrayList<>();
		
		int nInd = getOrAppendUTF8(file, toMethodName);
		int dInd = getOrAppendUTF8(file, toMethodSignature);
		int ntInd = getOrAppendNameAndType(file, nInd, dInd);
		int clNInd = getOrAppendUTF8(file, toClass);
		int clInd = getOrAppendClass(file, clNInd);
		int mrInd = getOrAppendMethodRef(file, clInd, ntInd);
		int mNmInd = getOrAppendString(file, file.getConstantPool().indexOf(method.getName()));
		int mDcInd = getOrAppendString(file, file.getConstantPool().indexOf(method.getDescriptor()));
		int objClNmInd = getOrAppendUTF8(file, "java/lang/Object");
		int objClInd = file.getConstantPool().appendEntry(new ConstantPoolClassEntry(file.getConstantPool(), objClNmInd));
		
		boolean isStatic = method.getAccessFlags().hasFlag(MethodAccessFlag.STATIC);
		if(!isStatic) { // Only add this class instance for instance methods
			nInstr.add(new InstructionInformation(Instruction.ALOAD_0));
		}else {
			nInstr.add(new InstructionInformation(Instruction.ACONST_NULL));
		}
		
		nInstr.add(new InstructionInformation(Instruction.LDC_W, getShortBytes(mNmInd)));
		nInstr.add(new InstructionInformation(Instruction.LDC_W, getShortBytes(mDcInd)));
		nInstr.add(new InstructionInformation(Instruction.BIPUSH, (byte) method.getMethodDescriptor().getParameterDescriptors().length));
		nInstr.add(new InstructionInformation(Instruction.ANEWARRAY, getShortBytes(objClInd)));
		
		byte i = 0;
		byte idx = 0;
		for(ParameterDescriptor p : mDesc.getParameterDescriptors()) {
			nInstr.add(new InstructionInformation(Instruction.DUP));
			nInstr.add(new InstructionInformation(Instruction.BIPUSH, idx++)); // Push array index
			nInstr.addAll(getLoadInfo(file, p.getParameterType().getPrimitiveType(), isStatic ? i++ : ++i)); // Push value onto the stack
			nInstr.add(new InstructionInformation(Instruction.AASTORE)); // Store value into array
			PrimitiveType t = p.getParameterType().getPrimitiveType();
			if(t.equals(PrimitiveType.DOUBLE) || t.equals(PrimitiveType.LONG)) i++;
		}
		
		nInstr.add(new InstructionInformation(Instruction.INVOKESTATIC, getShortBytes(mrInd)));
		
		PrimitiveType pt = mDesc.getReturnType().getPrimitiveType();
		
		if(pt == PrimitiveType.VOID) {
			nInstr.add(new InstructionInformation(Instruction.POP));
		}
		
		if(!mDesc.getReturnType().isPrimitive()) {
			int classInd = getOrAppendClass(file, getOrAppendUTF8(file, mDesc.getReturnType().getClassName().replace('.', '/')));
			nInstr.add(new InstructionInformation(Instruction.CHECKCAST, getShortBytes(classInd)));
		}else if(!mDesc.getReturnType().isVoid()){
			int classInd = getOrAppendClass(file, getOrAppendUTF8(file, pt.getNonPrimitiveClassName().replace('.', '/')));
			nInstr.add(new InstructionInformation(Instruction.CHECKCAST, getShortBytes(classInd)));
			nInstr.add(new InstructionInformation(Instruction.INVOKEVIRTUAL, getShortBytes(getOrAppendPrimitiveValueMethod(file, pt))));
		}
		
		nInstr.add(new InstructionInformation(getReturnInstruction(pt)));
		
		codeAttr.setMaxStack(8); // 4 constant args, up to 8 total (longs are 2x) when adding params to array
		
		codeAttr.getCode().replace(ByteCode.of(nInstr));
	}
	
	public static Instruction getReturnInstruction(PrimitiveType primitiveType) {
		switch(primitiveType) {
			case DOUBLE:
				return Instruction.DRETURN;
			case FLOAT:
				return Instruction.FRETURN;
			case LONG:
				return Instruction.LRETURN;
			case OBJECT:
				return Instruction.ARETURN;
			case VOID:
				return Instruction.RETURN;
			case BOOLEAN:
			case BYTE:
			case SHORT:
			case CHAR:
			case INT:
				return Instruction.IRETURN;
			default:
				throw new IllegalArgumentException("Invalid primitive type");
		}
	}
	
	public static int getOrAppendUTF8(ClassFile file, String utf8) {
		int enInd = Arrays.stream(file.getConstantPool().getEntries())
				.filter(e -> e instanceof ConstantPoolUTF8Entry && ((ConstantPoolUTF8Entry) e).getValue().equals(utf8))
				.map(e -> file.getConstantPool().indexOf(e))
				.findFirst().orElse(-1);
		if(enInd == -1) {
			enInd = file.getConstantPool().appendEntry(new ConstantPoolUTF8Entry(file.getConstantPool(), utf8));
		}
		return enInd;
	}
	
	public static int getOrAppendString(ClassFile file, int stringInd) {
		int enInd = Arrays.stream(file.getConstantPool().getEntries())
				.filter(e -> e instanceof ConstantPoolStringEntry && ((ConstantPoolStringEntry) e).getStringIndex() == stringInd)
				.map(e -> file.getConstantPool().indexOf(e))
				.findFirst().orElse(-1);
		if(enInd == -1) {
			enInd = file.getConstantPool().appendEntry(new ConstantPoolStringEntry(file.getConstantPool(), stringInd));
		}
		return enInd;
	}
	
	public static int getOrAppendString(ClassFile file, String string) {
		int enInd = Arrays.stream(file.getConstantPool().getEntries())
				.filter(e -> e instanceof ConstantPoolStringEntry && ((ConstantPoolStringEntry) e).getString().getValue().equals(string))
				.map(e -> file.getConstantPool().indexOf(e))
				.findFirst().orElse(-1);
		if(enInd == -1) {
			enInd = file.getConstantPool().appendEntry(new ConstantPoolStringEntry(file.getConstantPool(), getOrAppendUTF8(file, string)));
		}
		return enInd;
	}
	
	public static int getOrAppendDouble(ClassFile file, double val) {
		int enInd = Arrays.stream(file.getConstantPool().getEntries())
				.filter(e -> e instanceof ConstantPoolDoubleEntry && ((ConstantPoolDoubleEntry) e).getValue() == val)
				.map(e -> file.getConstantPool().indexOf(e))
				.findFirst().orElse(-1);
		if(enInd == -1) {
			enInd = file.getConstantPool().appendEntry(new ConstantPoolDoubleEntry(file.getConstantPool(), val));
		}
		return enInd;
	}
	
	public static int getOrAppendFloat(ClassFile file, float val) {
		int enInd = Arrays.stream(file.getConstantPool().getEntries())
				.filter(e -> e instanceof ConstantPoolFloatEntry && ((ConstantPoolFloatEntry) e).getValue() == val)
				.map(e -> file.getConstantPool().indexOf(e))
				.findFirst().orElse(-1);
		if(enInd == -1) {
			enInd = file.getConstantPool().appendEntry(new ConstantPoolFloatEntry(file.getConstantPool(), val));
		}
		return enInd;
	}
	
	public static int getOrAppendInteger(ClassFile file, int val) {
		int enInd = Arrays.stream(file.getConstantPool().getEntries())
				.filter(e -> e instanceof ConstantPoolIntegerEntry && ((ConstantPoolIntegerEntry) e).getValue() == val)
				.map(e -> file.getConstantPool().indexOf(e))
				.findFirst().orElse(-1);
		if(enInd == -1) {
			enInd = file.getConstantPool().appendEntry(new ConstantPoolIntegerEntry(file.getConstantPool(), val));
		}
		return enInd;
	}
	
	public static int getOrAppendClass(ClassFile file, int nameIndex) {
		int enInd = Arrays.stream(file.getConstantPool().getEntries())
				.filter(e -> e instanceof ConstantPoolClassEntry && ((ConstantPoolClassEntry) e).getNameIndex() == nameIndex)
				.map(e -> file.getConstantPool().indexOf(e))
				.findFirst().orElse(-1);
		if(enInd == -1) {
			enInd = file.getConstantPool().appendEntry(new ConstantPoolClassEntry(file.getConstantPool(), nameIndex));
		}
		return enInd;
	}
	
	public static int getOrAppendFieldRef(ClassFile file, int clInd, int ntIndex) {
		int enInd = Arrays.stream(file.getConstantPool().getEntries())
				.filter(e -> e instanceof ConstantPoolFieldRefEntry && ((ConstantPoolFieldRefEntry) e).getClassIndex() == clInd && ((ConstantPoolFieldRefEntry) e).getNameAndTypeIndex() == ntIndex)
				.map(e -> file.getConstantPool().indexOf(e))
				.findFirst().orElse(-1);
		if(enInd == -1) {
			enInd = file.getConstantPool().appendEntry(new ConstantPoolFieldRefEntry(file.getConstantPool(), clInd, ntIndex));
		}
		return enInd;
	}
	
	public static int getOrAppendNameAndType(ClassFile file, int nameIndex, int descIndex) {
		int enInd = Arrays.stream(file.getConstantPool().getEntries())
				.filter(e -> e instanceof ConstantPoolNameAndTypeEntry && ((ConstantPoolNameAndTypeEntry) e).getNameIndex() == nameIndex && ((ConstantPoolNameAndTypeEntry) e).getDescriptorIndex() == descIndex)
				.map(e -> file.getConstantPool().indexOf(e))
				.findFirst().orElse(-1);
		if(enInd == -1) {
			enInd = file.getConstantPool().appendEntry(new ConstantPoolNameAndTypeEntry(file.getConstantPool(), nameIndex, descIndex));
		}
		return enInd;
	}
	
	public static int getOrAppendMethodRef(ClassFile file, int clInd, int ntInd) {
		int enInd = Arrays.stream(file.getConstantPool().getEntries())
				.filter(e -> e instanceof ConstantPoolMethodRefEntry && ((ConstantPoolMethodRefEntry) e).getClassIndex() == clInd && ((ConstantPoolMethodRefEntry) e).getNameAndTypeIndex() == ntInd)
				.map(e -> file.getConstantPool().indexOf(e))
				.findFirst().orElse(-1);
		if(enInd == -1) {
			enInd = file.getConstantPool().appendEntry(new ConstantPoolMethodRefEntry(file.getConstantPool(), clInd, ntInd));
		}
		return enInd;
	}
	
	public static int getOrAppendInterfaceMethodRef(ClassFile file, int clInd, int ntInd) {
		int enInd = Arrays.stream(file.getConstantPool().getEntries())
				.filter(e -> e instanceof ConstantPoolInterfaceMethodRefEntry && ((ConstantPoolInterfaceMethodRefEntry) e).getClassIndex() == clInd && ((ConstantPoolInterfaceMethodRefEntry) e).getNameAndTypeIndex() == ntInd)
				.map(e -> file.getConstantPool().indexOf(e))
				.findFirst().orElse(-1);
		if(enInd == -1) {
			enInd = file.getConstantPool().appendEntry(new ConstantPoolInterfaceMethodRefEntry(file.getConstantPool(), clInd, ntInd));
		}
		return enInd;
	}
	
	public static int getOrAppendValueOfMethod(ClassFile file, String className, String primitiveName) {
		int intClNmInd = getOrAppendUTF8(file, className);
		int intClInd = getOrAppendClass(file, intClNmInd);
		int intVONmInd = getOrAppendUTF8(file, "valueOf");
		int intVOSigInd = getOrAppendUTF8(file, "(" + primitiveName + ")L" + className + ";");
		int intVOntInd = getOrAppendNameAndType(file, intVONmInd, intVOSigInd);
		return getOrAppendMethodRef(file, intClInd, intVOntInd);
	}
	
	public static int getOrAppendValueOfMethod(ClassFile file, PrimitiveType primitiveType) {
		return getOrAppendValueOfMethod(file, primitiveType.getNonPrimitiveClassName().replace('.', '/'), primitiveType.getSignatureName());
	}
	
	public static int getOrAppendPrimitiveValueMethod(ClassFile file, String className, String primitiveName, String methodName) {
		int intClNmInd = getOrAppendUTF8(file, className);
		int intClInd = getOrAppendClass(file, intClNmInd);
		int intVONmInd = getOrAppendUTF8(file, methodName);
		int intVOSigInd = getOrAppendUTF8(file, "()" + primitiveName);
		int intVOntInd = getOrAppendNameAndType(file, intVONmInd, intVOSigInd);
		return getOrAppendMethodRef(file, intClInd, intVOntInd);
	}
	
	public static int getOrAppendPrimitiveValueMethod(ClassFile file, PrimitiveType type) {
		switch(type) {
			case DOUBLE:
				return getOrAppendPrimitiveValueMethod(file, "java/lang/Double", "D", "doubleValue");
			case FLOAT:
				return getOrAppendPrimitiveValueMethod(file, "java/lang/Float", "F", "floatValue");
			case LONG:
				return getOrAppendPrimitiveValueMethod(file, "java/lang/Long", "J", "longValue");
			case BOOLEAN:
				return getOrAppendPrimitiveValueMethod(file, "java/lang/Boolean", "Z", "booleanValue");
			case BYTE:
				return getOrAppendPrimitiveValueMethod(file, "java/lang/Byte", "B", "byteValue");
			case SHORT:
				return getOrAppendPrimitiveValueMethod(file, "java/lang/Short", "S", "shortValue");
			case CHAR:
				return getOrAppendPrimitiveValueMethod(file, "java/lang/Character", "C", "charValue");
			case INT:
				return getOrAppendPrimitiveValueMethod(file, "java/lang/Integer", "I", "intValue");
			default:
				throw new IllegalArgumentException("Invalid primitive type");
		}
	}
	
	public static void redirectClassMethodsNamed(ClassFile file, Class<?> toClass, Method handlerMethod) {
		if(handlerMethod.getParameterCount() != 4
				|| !Arrays.equals(handlerMethod.getParameterTypes(), new Class<?>[] {Object.class, String.class, String.class, Object[].class})
				|| !Modifier.isPublic(handlerMethod.getModifiers())
				|| !Modifier.isStatic(handlerMethod.getModifiers()))
			throw new IllegalArgumentException("Method must be of format: Object method(Object classInstance, String methodName, String methodSignature, Object[] args)");
		
		for(ClassMethod m : file.getMethods()) {
			if(m.isConstructor()) continue;
//			if(m.getMethodDescriptor().getReturnType().isVoid()) continue;
			String sig = "(" + getMethodDescriptor(handlerMethod).getParameterSignature() + ")Ljava/lang/Object;";
			redirectMethodExecutionNamed(file, m, toClass.getName().replace('.', '/'), handlerMethod.getName(), sig);
		}
	}
	
	public static void redirectClassMethods(ClassFile file, Class<?> toClass) {
		for(ClassMethod m : file.getMethods()) {
			if(m.isConstructor()) continue;
			Method handlerMethod;
			try {
				List<ParameterDescriptor> ds = new ArrayList<>(Arrays.asList(m.getMethodDescriptor().getParameterDescriptors()));
				if(!m.getAccessFlags().hasFlag(MethodAccessFlag.STATIC)) ds.add(0, new ParameterDescriptor(TypeDescriptor.of(Object.class)));
				handlerMethod = toClass.getDeclaredMethod(m.getName().getValue(), ds.stream().map(d -> MiscUtils.callSafely(() -> d.getParameterType().toClass()).get()).toArray(Class<?>[]::new));
				if(!handlerMethod.isAnnotationPresent(MethodRedirectTarget.class)) continue;
				handlerMethod.setAccessible(true);
			} catch (NoSuchMethodException | SecurityException | NoSuchElementException e) {
				continue;
			}
			MethodDescriptor dc = getMethodDescriptor(handlerMethod);
			String sig = dc.getRawDescriptor();
			redirectMethodExecution(file, m, toClass.getName().replace('.', '/'), handlerMethod.getName(), sig);
		}
	}
	
	public static MethodDescriptor getMethodDescriptor(Method m) {
		return new MethodDescriptor(
				m.getName(),
				EnumFlagCompound.of(MethodAccessFlag.class, m.getModifiers()),
				TypeDescriptor.of(m.getReturnType()),
				Arrays.stream(m.getParameterTypes()).map(t -> new ParameterDescriptor(TypeDescriptor.of(t))).toArray(ParameterDescriptor[]::new));
	}
	
	public static byte[] getShortBytes(int val) {
		byte[] bt = new byte[2];
		bt[0] = (byte) ((val >> 8) & 0xFF);
		bt[1] = (byte) (val & 0xFF);
		return bt;
	}
	
	public static InstructionInformation getInfo(PrimitiveType type, byte paramIdx) {
		return new InstructionInformation(getLoadInstruction(type), new byte[] {paramIdx});
	}
	
	public static List<InstructionInformation> getLoadInfo(ClassFile file, PrimitiveType type, byte paramIdx) {
		List<InstructionInformation> inf = new ArrayList<>();
		inf.add(new InstructionInformation(getLoadInstruction(type), new byte[] {paramIdx}));
		if(type != PrimitiveType.OBJECT) inf.add(new InstructionInformation(Instruction.INVOKESTATIC, getShortBytes(getOrAppendValueOfMethod(file, type))));
		return inf;
	}
	
	public static Instruction getLoadInstruction(PrimitiveType type) {
		switch(type) {
			case BOOLEAN:
			case BYTE:
			case CHAR:
			case INT:
			case SHORT:
				return Instruction.ILOAD;
			case DOUBLE:
				return Instruction.DLOAD;
			case FLOAT:
				return Instruction.FLOAD;
			case LONG:
				return Instruction.LLOAD;
			case OBJECT:
				return Instruction.ALOAD;
			default:
				throw new IllegalArgumentException("Invalid parameter type");
		}
	}
	
	public static boolean isEntryUsed(ClassFile cf, int poolEntryIndex) {
		ConstantPool constantPool = cf.getConstantPool();
		
		if(constantPool.indexOf(cf.getThisClass()) == poolEntryIndex) return true;
		if(cf.getThisClass().getNameIndex() == poolEntryIndex) return true;
		if(constantPool.indexOf(cf.getThisClass()) == poolEntryIndex) return true;
		if(cf.getThisClass().getNameIndex() == poolEntryIndex) return true;
		
		System.out.println("Entry @ " + poolEntryIndex + " is not used");
		return false;
	}

}
