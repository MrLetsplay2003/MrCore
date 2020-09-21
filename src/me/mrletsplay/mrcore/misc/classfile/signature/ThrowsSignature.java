package me.mrletsplay.mrcore.misc.classfile.signature;

public interface ThrowsSignature extends Signature {

	public static class ClassTypeThrowsSignature implements ThrowsSignature {
		
		private ClassTypeSignature thrown;

		public ClassTypeThrowsSignature(ClassTypeSignature thrown) {
			this.thrown = thrown;
		}

		@Override
		public ClassTypeSignature getThrown() {
			return thrown;
		}
		
	}

	public static class TypeVariableThrowsSignature implements ThrowsSignature {
		
		private TypeVariableSignature thrown;

		public TypeVariableThrowsSignature(TypeVariableSignature thrown) {
			this.thrown = thrown;
		}
		
		@Override
		public TypeVariableSignature getThrown() {
			return thrown;
		}
		
	}
	
	public ReferenceTypeSignature getThrown();
	
}
