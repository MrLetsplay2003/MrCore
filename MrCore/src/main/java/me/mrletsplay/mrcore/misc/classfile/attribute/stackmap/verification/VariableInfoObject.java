package me.mrletsplay.mrcore.misc.classfile.attribute.stackmap.verification;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import me.mrletsplay.mrcore.misc.classfile.pool.ConstantPool;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolClassEntry;

public class VariableInfoObject extends AbstractVerificationTypeInfo {

	private ConstantPool pool;
	private ConstantPoolClassEntry variableType;
	
	public VariableInfoObject(ConstantPool pool, DataInputStream dIn) throws IOException {
		this.pool = pool;
		this.variableType = pool.getEntry(dIn.readUnsignedShort()).as(ConstantPoolClassEntry.class);
	}
	
	public VariableInfoObject(ConstantPoolClassEntry variableType) {
		this.pool = variableType.getConstantPool();
		this.variableType = variableType;
	}
	
	public void setVariableType(ConstantPoolClassEntry variableType) {
		this.variableType = variableType;
	}

	public ConstantPoolClassEntry getVariableType() {
		return variableType;
	}
	
	@Override
	public VerificationType getType() {
		return VerificationType.OBJECT;
	}
	
	@Override
	public void write(DataOutputStream dOut) throws IOException {
		dOut.write(getType().getValue());
		dOut.writeShort(pool.indexOf(variableType));
	}
	
}
