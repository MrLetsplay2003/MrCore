package me.mrletsplay.mrcore.misc.classfile.signature;

public class TypeVariableSignature extends ReferenceTypeSignature {
	
	private String identifier;
	
	public TypeVariableSignature(String identifier) {
		this.identifier = identifier;
	}
	
	public String getIdentifier() {
		return identifier;
	}

}
