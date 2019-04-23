package me.mrletsplay.mrcore.misc.classfile.attribute.stackmap.verification;

import java.io.DataInputStream;
import java.io.IOException;

import me.mrletsplay.mrcore.misc.classfile.pool.ConstantPool;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolClassEntry;

public class VariableInfoObject extends AbstractVerificationTypeInfo {

	private ConstantPoolClassEntry variableType;
	
	public VariableInfoObject(ConstantPool pool, DataInputStream dIn) throws IOException {
		this.variableType = pool.getEntry(dIn.readUnsignedShort()).as(ConstantPoolClassEntry.class);
	}
	
	public ConstantPoolClassEntry getVariableType() {
		return variableType;
	}
	
	@Override
	public VerificationType getType() {
		return VerificationType.OBJECT;
	}
	
}
