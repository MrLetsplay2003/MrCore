package me.mrletsplay.mrcore.misc.classfile.signature;

import me.mrletsplay.mrcore.misc.CharReader;
import me.mrletsplay.mrcore.misc.PrimitiveType;

public class ReferenceTypeSignature implements JavaTypeSignature {
	
	public static ReferenceTypeSignature read(CharReader reader) {
		char c = reader.next();
		switch(c) {
			case 'L':
				reader.revert();
				return ClassTypeSignature.read(reader);
			case 'T':
				reader.revert();
				return TypeVariableSignature.read(reader);
			case '[':
				char t = reader.next();
				PrimitiveType p = PrimitiveType.getBySignature(String.valueOf(t));
				if(p != null) return new ArrayTypeSignature(new PrimitiveJavaTypeSignature(p));
				reader.revert();
				return new ArrayTypeSignature(read(reader));
			default:
				return null;
		}
	}
	
}
