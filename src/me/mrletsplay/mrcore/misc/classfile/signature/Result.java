package me.mrletsplay.mrcore.misc.classfile.signature;

public interface Result {
	
	public static class JavaTypeResult implements Result {
		
		private JavaTypeSignature resultType;

		public JavaTypeResult(JavaTypeSignature resultType) {
			this.resultType = resultType;
		}
		
		public JavaTypeSignature getResultType() {
			return resultType;
		}
		
	}
	
	public static class VoidResult implements Result {
		
	}

}
