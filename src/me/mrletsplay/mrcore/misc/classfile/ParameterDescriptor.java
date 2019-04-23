package me.mrletsplay.mrcore.misc.classfile;

public class ParameterDescriptor {
	
	private TypeDescriptor parameterType;
	
	public ParameterDescriptor(TypeDescriptor parameterType) {
		this.parameterType = parameterType;
	}
	
	public TypeDescriptor getParameterType() {
		return parameterType;
	}

}
