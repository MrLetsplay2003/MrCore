package me.mrletsplay.mrcore.misc.classfile;

import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.mrcore.misc.PrimitiveType;
import me.mrletsplay.mrcore.misc.classfile.attribute.AttributeCode;
import me.mrletsplay.mrcore.misc.classfile.attribute.DefaultAttributeType;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolClassEntry;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolMethodRefEntry;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolNameAndTypeEntry;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolStringEntry;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolUTF8Entry;

public class ClassFileUtils {
	
	public static void redirectMethodExecution(ClassFile file, ClassMethod method, String toClass, String toMethodName, String toMethodSig) {
		AttributeCode codeAttr = (AttributeCode) method.getAttribute(DefaultAttributeType.CODE);
		List<InstructionInformation> nInstr = new ArrayList<>();
		
		ConstantPoolUTF8Entry nEn = new ConstantPoolUTF8Entry(file.getConstantPool(), toMethodName);
		ConstantPoolUTF8Entry dEn = new ConstantPoolUTF8Entry(file.getConstantPool(), toMethodSig);
		int nInd = file.getConstantPool().appendEntry(nEn);
		int dInd = file.getConstantPool().appendEntry(dEn);
		ConstantPoolNameAndTypeEntry ntEn = new ConstantPoolNameAndTypeEntry(file.getConstantPool(), nInd, dInd);
		int ntInd = file.getConstantPool().appendEntry(ntEn);
		ConstantPoolUTF8Entry clNEn = new ConstantPoolUTF8Entry(file.getConstantPool(), toClass);
		int clNInd = file.getConstantPool().appendEntry(clNEn);
		ConstantPoolClassEntry clEn = new ConstantPoolClassEntry(file.getConstantPool(), clNInd);
		int clInd = file.getConstantPool().appendEntry(clEn);
		ConstantPoolMethodRefEntry mrEn = new ConstantPoolMethodRefEntry(file.getConstantPool(), clInd, ntInd);
		int mrInd = file.getConstantPool().appendEntry(mrEn);
		
		boolean isStatic = method.getAccessFlags().hasFlag(MethodAccessFlag.STATIC);
		if(!isStatic) { // Only add this class instance for instance methods
			nInstr.add(new InstructionInformation(Instruction.ALOAD_0, new byte[] {}));
		}
		byte i = 0;
		for(ParameterDescriptor p : method.getMethodDescriptor().getParameterDescriptors()) {
			nInstr.add(getInfo(p.getParameterType(), ++i));
			PrimitiveType t = p.getParameterType().getPrimitiveType();
			if(t.equals(PrimitiveType.DOUBLE) || t.equals(PrimitiveType.LONG)) i++;
		}
		nInstr.add(new InstructionInformation(Instruction.INVOKESTATIC, getShortBytes(mrInd)));
		nInstr.add(new InstructionInformation(Instruction.RETURN, new byte[] {}));
		
		codeAttr.setMaxStack(i + (isStatic ? 0 : 1)); // #params + 1 for this class instance if instance method
		
		codeAttr.getCode().replace(ByteCode.of(nInstr));
		System.out.println(nInstr);
	}
	
	public static void redirectMethodExecutionNamed(ClassFile file, ClassMethod method, String toClass, String toMethodName, String toMethodSig) {
		AttributeCode codeAttr = (AttributeCode) method.getAttribute(DefaultAttributeType.CODE);
		List<InstructionInformation> nInstr = new ArrayList<>();
		
		ConstantPoolUTF8Entry nEn = new ConstantPoolUTF8Entry(file.getConstantPool(), toMethodName);
		ConstantPoolUTF8Entry dEn = new ConstantPoolUTF8Entry(file.getConstantPool(), toMethodSig);
		int nInd = file.getConstantPool().appendEntry(nEn);
		int dInd = file.getConstantPool().appendEntry(dEn);
		ConstantPoolNameAndTypeEntry ntEn = new ConstantPoolNameAndTypeEntry(file.getConstantPool(), nInd, dInd);
		int ntInd = file.getConstantPool().appendEntry(ntEn);
		ConstantPoolUTF8Entry clNEn = new ConstantPoolUTF8Entry(file.getConstantPool(), toClass);
		int clNInd = file.getConstantPool().appendEntry(clNEn);
		ConstantPoolClassEntry clEn = new ConstantPoolClassEntry(file.getConstantPool(), clNInd);
		int clInd = file.getConstantPool().appendEntry(clEn);
		ConstantPoolMethodRefEntry mrEn = new ConstantPoolMethodRefEntry(file.getConstantPool(), clInd, ntInd);
		int mrInd = file.getConstantPool().appendEntry(mrEn);
		ConstantPoolStringEntry mNmEn = new ConstantPoolStringEntry(file.getConstantPool(), file.getConstantPool().indexOf(method.getName()));
		int mNmInd = file.getConstantPool().appendEntry(mNmEn);
		ConstantPoolStringEntry mDcEn = new ConstantPoolStringEntry(file.getConstantPool(), file.getConstantPool().indexOf(method.getDescriptor()));
		int mDcInd = file.getConstantPool().appendEntry(mDcEn);
		
		boolean isStatic = method.getAccessFlags().hasFlag(MethodAccessFlag.STATIC);
		if(!isStatic) { // Only add this class instance for instance methods
			nInstr.add(new InstructionInformation(Instruction.ALOAD_0, new byte[] {}));
		}
		nInstr.add(new InstructionInformation(Instruction.LDC, new byte[] {(byte) mNmInd}));
		nInstr.add(new InstructionInformation(Instruction.LDC, new byte[] {(byte) mDcInd}));
		byte i = 0;
		for(ParameterDescriptor p : method.getMethodDescriptor().getParameterDescriptors()) {
			nInstr.add(getInfo(p.getParameterType(), ++i));
			PrimitiveType t = p.getParameterType().getPrimitiveType();
			if(t.equals(PrimitiveType.DOUBLE) || t.equals(PrimitiveType.LONG)) i++;
		}
		nInstr.add(new InstructionInformation(Instruction.INVOKESTATIC, getShortBytes(mrInd)));
		nInstr.add(new InstructionInformation(Instruction.RETURN, new byte[] {}));
		
		codeAttr.setMaxStack(i + (isStatic ? 0 : 1)); // #params + 1 for this class instance if instance method
		
		codeAttr.getCode().replace(ByteCode.of(nInstr));
		System.out.println(nInstr);
	}
	
	private static byte[] getShortBytes(int val) {
		byte[] bt = new byte[2];
		bt[0] = (byte) ((val >> 8) & 0xFF);
		bt[1] = (byte) (val & 0xFF);
		return bt;
	}
	
	private static InstructionInformation getInfo(TypeDescriptor type, byte paramIdx) {
		PrimitiveType t = type.getPrimitiveType();
		if(t == null) throw new IllegalArgumentException("Can't have void type for parameter");
		switch(type.getPrimitiveType()) {
			case BOOLEAN:
				return new InstructionInformation(Instruction.ILOAD, new byte[] {paramIdx});
			case BYTE:
				return new InstructionInformation(Instruction.ILOAD, new byte[] {paramIdx});
			case CHAR:
				return new InstructionInformation(Instruction.ILOAD, new byte[] {paramIdx});
			case DOUBLE:
				return new InstructionInformation(Instruction.DLOAD, new byte[] {paramIdx});
			case FLOAT:
				return new InstructionInformation(Instruction.FLOAD, new byte[] {paramIdx});
			case INT:
				return new InstructionInformation(Instruction.ILOAD, new byte[] {paramIdx});
			case LONG:
				return new InstructionInformation(Instruction.LLOAD, new byte[] {paramIdx});
			case OBJECT:
				return new InstructionInformation(Instruction.ALOAD, new byte[] {paramIdx});
			case SHORT:
				return new InstructionInformation(Instruction.ILOAD, new byte[] {paramIdx});
		}
		throw new IllegalArgumentException("Invalid parameter type");
	}

}
