package me.mrletsplay.mrcore.misc.classfile.signature;

import me.mrletsplay.mrcore.misc.CharReader;

public class FieldSignature implements Signature {

	private ReferenceTypeSignature type;

	public FieldSignature(ReferenceTypeSignature type) {
		this.type = type;
	}
	
	public ReferenceTypeSignature getType() {
		return type;
	}
	
	public static FieldSignature read(CharReader reader) {
		return new FieldSignature(ReferenceTypeSignature.read(reader));
	}
	
	public static FieldSignature parse(String signature) {
		return read(new CharReader(signature));
	}
	
}
