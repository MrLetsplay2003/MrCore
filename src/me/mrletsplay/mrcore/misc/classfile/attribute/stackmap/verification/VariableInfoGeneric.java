package me.mrletsplay.mrcore.misc.classfile.attribute.stackmap.verification;

import java.io.DataOutputStream;
import java.io.IOException;

public class VariableInfoGeneric extends AbstractVerificationTypeInfo {

	private VerificationType type;
	
	public VariableInfoGeneric(VerificationType type) {
		this.type = type;
	}
	
	@Override
	public VerificationType getType() {
		return type;
	}
	
	@Override
	public void write(DataOutputStream dOut) throws IOException {
		dOut.write(type.getValue());
	}
	
}
