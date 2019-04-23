package me.mrletsplay.mrcore.misc.classfile.attribute.stackmap.verification;

public class VariableInfoGeneric extends AbstractVerificationTypeInfo {

	private VerificationType type;
	
	public VariableInfoGeneric(VerificationType type) {
		this.type = type;
	}
	
	@Override
	public VerificationType getType() {
		return type;
	}
	
}
