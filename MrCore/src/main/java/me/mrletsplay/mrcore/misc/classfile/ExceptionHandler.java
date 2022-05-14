package me.mrletsplay.mrcore.misc.classfile;

import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolClassEntry;

public class ExceptionHandler {

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
	
	public int getCatchTypeIndex() {
		return catchTypeIndex;
	}
	
	public ConstantPoolClassEntry getCatchType() {
		if(catchTypeIndex == 0) return null;
		return classFile.getConstantPool().getEntry(catchTypeIndex).as(ConstantPoolClassEntry.class);
	}
	
}
