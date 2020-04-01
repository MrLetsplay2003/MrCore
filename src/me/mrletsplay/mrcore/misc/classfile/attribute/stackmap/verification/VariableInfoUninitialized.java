package me.mrletsplay.mrcore.misc.classfile.attribute.stackmap.verification;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class VariableInfoUninitialized extends AbstractVerificationTypeInfo {

	private int offset;
	
	public VariableInfoUninitialized(DataInputStream dIn) throws IOException {
		this.offset = dIn.readUnsignedShort();
	}
	
	public VariableInfoUninitialized(int offset) {
		this.offset = offset;
	}
	
	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getOffset() {
		return offset;
	}
	
	@Override
	public VerificationType getType() {
		return VerificationType.UNINITIALIZED_VARIABLE;
	}
	
	@Override
	public void write(DataOutputStream dOut) throws IOException {
		dOut.write(getType().getValue());
		dOut.writeShort(offset);
	}
	
}
