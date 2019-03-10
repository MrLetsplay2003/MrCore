package me.mrletsplay.mrcore.misc.classfile.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import me.mrletsplay.mrcore.misc.MiscUtils;
import me.mrletsplay.mrcore.misc.PrimitiveType;
import me.mrletsplay.mrcore.misc.classfile.ByteCode;
import me.mrletsplay.mrcore.misc.classfile.ClassFile;
import me.mrletsplay.mrcore.misc.classfile.ClassMethod;
import me.mrletsplay.mrcore.misc.classfile.Instruction;
import me.mrletsplay.mrcore.misc.classfile.InstructionInformation;
import me.mrletsplay.mrcore.misc.classfile.MethodAccessFlag;
import me.mrletsplay.mrcore.misc.classfile.MethodDescriptor;
import me.mrletsplay.mrcore.misc.classfile.ParameterDescriptor;
import me.mrletsplay.mrcore.misc.classfile.TypeDescriptor;
import me.mrletsplay.mrcore.misc.classfile.attribute.AttributeCode;
import me.mrletsplay.mrcore.misc.classfile.attribute.DefaultAttributeType;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolClassEntry;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolDoubleEntry;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolFieldRefEntry;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolFloatEntry;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolIntegerEntry;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolMethodRefEntry;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolNameAndTypeEntry;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolStringEntry;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolUTF8Entry;

public class ClassFileUtils {
	
	public static void redirectMethodExecution(ClassFile file, ClassMethod method, String toClass, String toMethodName, String toMethodSignature) {
		AttributeCode codeAttr = (AttributeCode) method.getAttribute(DefaultAttributeType.CODE);
		List<InstructionInformation> nInstr = new ArrayList<>();
		
		int nInd = getOrAppendUTF8(file, toMethodName);
		int dInd = getOrAppendUTF8(file, toMethodSignature);
		int ntInd = getOrAppendNameAndType(file, nInd, dInd);
		int clNInd = getOrAppendUTF8(file, toClass);
		int clInd = getOrAppendClass(file, clNInd);
		int mrInd = getOrAppendMethodRef(file, clInd, ntInd);
		
		boolean isStatic = method.getAccessFlags().hasFlag(MethodAccessFlag.STATIC);
		if(!isStatic) { // Only add this class instance for instance methods
			nInstr.add(new InstructionInformation(Instruction.ALOAD_0));
		}
		byte i = 0;
		for(ParameterDescriptor p : method.getMethodDescriptor().getParameterDescriptors()) {
			PrimitiveType t = p.getParameterType().getPrimitiveType();
			nInstr.add(getInfo(t, ++i));
			if(t.equals(PrimitiveType.DOUBLE) || t.equals(PrimitiveType.LONG)) i++;
		}
		nInstr.add(new InstructionInformation(Instruction.INVOKESTATIC, getShortBytes(mrInd)));
		nInstr.add(new InstructionInformation(Instruction.RETURN));
		
		codeAttr.setMaxStack(i + (isStatic ? 0 : 1)); // #params + 1 for this class instance if instance method
		
		codeAttr.getCode().replace(ByteCode.of(nInstr));
	}
	
	public static void redirectMethodExecutionNamed(ClassFile file, ClassMethod method, String toClass, String toMethodName, String toMethodSignature) {
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
		
		int intVOMr = appendVOMethod(file, "java/lang/Integer", "I");
		int dbVOMr = appendVOMethod(file, "java/lang/Double", "D");
		int flVOMr = appendVOMethod(file, "java/lang/Float", "F");
		int loVOMr = appendVOMethod(file, "java/lang/Long", "J");
		int byVOMr = appendVOMethod(file, "java/lang/Byte", "B");
		int boVOMr = appendVOMethod(file, "java/lang/Boolean", "Z");
		int chloVOMr = appendVOMethod(file, "java/lang/Character", "C");
		int shloVOMr = appendVOMethod(file, "java/lang/Short", "S");
		
		boolean isStatic = method.getAccessFlags().hasFlag(MethodAccessFlag.STATIC);
		if(!isStatic) { // Only add this class instance for instance methods
			nInstr.add(new InstructionInformation(Instruction.ALOAD_0));
		}else {
			nInstr.add(new InstructionInformation(Instruction.ACONST_NULL));
		}
		
		nInstr.add(new InstructionInformation(Instruction.LDC, (byte) mNmInd));
		nInstr.add(new InstructionInformation(Instruction.LDC, (byte) mDcInd));
		nInstr.add(new InstructionInformation(Instruction.BIPUSH, (byte) method.getMethodDescriptor().getParameterDescriptors().length));
		nInstr.add(new InstructionInformation(Instruction.ANEWARRAY, getShortBytes(objClInd)));
		
		byte i = 0, idx = 0;
		for(ParameterDescriptor p : method.getMethodDescriptor().getParameterDescriptors()) {
			nInstr.add(new InstructionInformation(Instruction.DUP));
			nInstr.add(new InstructionInformation(Instruction.BIPUSH, idx++));
			nInstr.addAll(getLoadInfo(p.getParameterType().getPrimitiveType(), isStatic ? i++ : ++i, intVOMr, dbVOMr, flVOMr, loVOMr, byVOMr, boVOMr, chloVOMr, shloVOMr));
			nInstr.add(new InstructionInformation(Instruction.AASTORE));
			PrimitiveType t = p.getParameterType().getPrimitiveType();
			if(t.equals(PrimitiveType.DOUBLE) || t.equals(PrimitiveType.LONG)) i++;
		}
		
		nInstr.add(new InstructionInformation(Instruction.INVOKESTATIC, getShortBytes(mrInd)));
		nInstr.add(new InstructionInformation(Instruction.RETURN));
		
		codeAttr.setMaxStack(i + 3);
		
		codeAttr.getCode().replace(ByteCode.of(nInstr));
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
	
//	private static int getOrAppendString(ClassFile file, String string) {
//		int enInd = Arrays.stream(file.getConstantPool().getEntries())
//				.filter(e -> e instanceof ConstantPoolStringEntry && ((ConstantPoolStringEntry) e).getString().getValue().equals(string))
//				.map(e -> file.getConstantPool().indexOf(e))
//				.findFirst().orElse(-1);
//		if(enInd == -1) {
//			enInd = file.getConstantPool().appendEntry(new ConstantPoolStringEntry(file.getConstantPool(), getOrAppendUTF8(file, string)));
//		}
//		return enInd;
//	}
	
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
	
	public static int appendVOMethod(ClassFile file, String clName, String pNm) {
		int intClNmInd = getOrAppendUTF8(file, clName);
		int intClInd = getOrAppendClass(file, intClNmInd);
		int intVONmInd = getOrAppendUTF8(file, "valueOf");
		int intVOSigInd = getOrAppendUTF8(file, "(" + pNm + ")L" + clName + ";");
		int intVOntInd = getOrAppendNameAndType(file, intVONmInd, intVOSigInd);
		return getOrAppendMethodRef(file, intClInd, intVOntInd);
	}
	
	public static void redirectClassMethodsNamed(ClassFile file, Class<?> toClass, Method handlerMethod) {
		if(handlerMethod.getParameterCount() != 4 || !Arrays.equals(handlerMethod.getParameterTypes(), new Class<?>[] {Object.class, String.class, String.class, Object[].class}) || !handlerMethod.isVarArgs())
			throw new IllegalArgumentException("Method must be of format method(Object classInst, String methodName, String methodSignature, Object... args)");
		
		for(ClassMethod m : file.getMethods()) {
			if(m.isConstructor()) continue;
			if(m.getMethodDescriptor().getReturnType().isVoid()) continue;
			String sig = "(" + getMethodDescriptor(handlerMethod).getParameterSignature() + ")V";
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
			String sig = "(" + getMethodDescriptor(handlerMethod).getParameterSignature() + ")V";
			redirectMethodExecution(file, m, toClass.getName().replace('.', '/'), handlerMethod.getName(), sig);
		}
	}
	
	public static MethodDescriptor getMethodDescriptor(Method m) {
		MethodDescriptor dc = new MethodDescriptor(m.getName(), TypeDescriptor.of(m.getReturnType()), Arrays.stream(m.getParameterTypes()).map(t -> new ParameterDescriptor(TypeDescriptor.of(t))).toArray(ParameterDescriptor[]::new));
		return dc;
	}
	
	public static byte[] getShortBytes(int val) {
		byte[] bt = new byte[2];
		bt[0] = (byte) ((val >> 8) & 0xFF);
		bt[1] = (byte) (val & 0xFF);
		return bt;
	}
	
	private static InstructionInformation getInfo(PrimitiveType type, byte paramIdx) {
		return new InstructionInformation(getLoadInstruction(type), new byte[] {paramIdx});
	}
	
	public static List<InstructionInformation> getLoadInfo(PrimitiveType type, byte paramIdx, int iInd, int dInd, int fInd, int lInd, int byInd, int boInd, int cInd, int sInd) {
		List<InstructionInformation> inf = new ArrayList<>();
		inf.add(new InstructionInformation(getLoadInstruction(type), new byte[] {paramIdx}));
		switch(type) {
			case INT:
				inf.add(new InstructionInformation(Instruction.INVOKESTATIC, getShortBytes(iInd)));
				break;
			case DOUBLE:
				inf.add(new InstructionInformation(Instruction.INVOKESTATIC, getShortBytes(dInd)));
				break;
			case FLOAT:
				inf.add(new InstructionInformation(Instruction.INVOKESTATIC, getShortBytes(fInd)));
				break;
			case LONG:
				inf.add(new InstructionInformation(Instruction.INVOKESTATIC, getShortBytes(lInd)));
				break;
			case BOOLEAN:
				inf.add(new InstructionInformation(Instruction.INVOKESTATIC, getShortBytes(boInd)));
				break;
			case BYTE:
				inf.add(new InstructionInformation(Instruction.INVOKESTATIC, getShortBytes(byInd)));
				break;
			case CHAR:
				inf.add(new InstructionInformation(Instruction.INVOKESTATIC, getShortBytes(cInd)));
				break;
			case SHORT:
				inf.add(new InstructionInformation(Instruction.INVOKESTATIC, getShortBytes(sInd)));
				break;
			default:
				throw new IllegalArgumentException("Invalid parameter type");
		}
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

}
