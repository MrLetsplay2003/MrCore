package me.mrletsplay.mrcore.misc.classfile.signature;

import me.mrletsplay.mrcore.misc.CharReader;
import me.mrletsplay.mrcore.misc.PrimitiveType;
import me.mrletsplay.mrcore.misc.classfile.signature.ArrayTypeSignature.BaseTypeArraySignature;
import me.mrletsplay.mrcore.misc.classfile.signature.ArrayTypeSignature.ReferenceTypeArraySignature;

public class ReferenceTypeSignature extends Signature {
	
	public static ReferenceTypeSignature read(CharReader reader) {
		char c = reader.next();
		switch(c) {
			case 'L':
				reader.revert();
				return ClassTypeSignature.read(reader);
			case 'T':
				String identifier = reader.nextUntil(';');
				reader.next(); // Skip the ;
				return new TypeVariableSignature(identifier);
			case '[':
				char t = reader.next();
				PrimitiveType p = PrimitiveType.getBySignature(String.valueOf(t));
				if(p != null) return new BaseTypeArraySignature(p);
				reader.revert();
				return new ReferenceTypeArraySignature(read(reader));
		}
		return null;
	}
	
}
