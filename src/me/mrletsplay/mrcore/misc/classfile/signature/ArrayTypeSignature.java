package me.mrletsplay.mrcore.misc.classfile.signature;

import me.mrletsplay.mrcore.misc.PrimitiveType;

public class ArrayTypeSignature extends ReferenceTypeSignature {
	
	public static class BaseTypeArraySignature extends ArrayTypeSignature {
		
		private PrimitiveType arrayType;
		
		public BaseTypeArraySignature(PrimitiveType arrayType) {
			this.arrayType = arrayType;
		}

		public PrimitiveType getArrayType() {
			return arrayType;
		}
		
	}
	
	public static class ReferenceTypeArraySignature extends ArrayTypeSignature {
		
		private ReferenceTypeSignature referenceType;

		public ReferenceTypeArraySignature(ReferenceTypeSignature referenceType) {
			this.referenceType = referenceType;
		}
		
		public ReferenceTypeSignature getArrayType() {
			return referenceType;
		}
		
	}

}
