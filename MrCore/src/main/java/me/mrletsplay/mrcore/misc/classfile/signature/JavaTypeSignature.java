package me.mrletsplay.mrcore.misc.classfile.signature;

import me.mrletsplay.mrcore.misc.CharReader;
import me.mrletsplay.mrcore.misc.PrimitiveType;

public interface JavaTypeSignature extends Signature {
	
	public static class PrimitiveJavaTypeSignature implements JavaTypeSignature {
		
		private PrimitiveType primitiveType;

		public PrimitiveJavaTypeSignature(PrimitiveType primitiveType) {
			this.primitiveType = primitiveType;
		}
		
		public PrimitiveType getPrimitiveType() {
			return primitiveType;
		}
		
	}
	
	public static JavaTypeSignature read(CharReader reader) {
		char t = reader.next();
		PrimitiveType p = PrimitiveType.getBySignature(String.valueOf(t));
		if(p != null) return new PrimitiveJavaTypeSignature(p);
		reader.revert();
		return ReferenceTypeSignature.read(reader);
	}

}
