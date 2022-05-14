package me.mrletsplay.mrcore.misc.classfile.signature;

import me.mrletsplay.mrcore.misc.CharReader;

public class TypeVariableSignature extends ReferenceTypeSignature {
	
	private String identifier;
	
	public TypeVariableSignature(String identifier) {
		this.identifier = identifier;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	
	public static TypeVariableSignature read(CharReader reader) {
		reader.next(); // Skip the T
		
		String identifier = reader.nextUntil(';');
		reader.next(); // Skip the ;
		return new TypeVariableSignature(identifier);
	}

}
