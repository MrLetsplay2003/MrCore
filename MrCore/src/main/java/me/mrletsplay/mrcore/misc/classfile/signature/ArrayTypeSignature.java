package me.mrletsplay.mrcore.misc.classfile.signature;

public class ArrayTypeSignature extends ReferenceTypeSignature {
	
	private JavaTypeSignature javaType;
	
	public ArrayTypeSignature(JavaTypeSignature javaType) {
		this.javaType = javaType;
	}

	public JavaTypeSignature getJavaType() {
		return javaType;
	}
	
}
