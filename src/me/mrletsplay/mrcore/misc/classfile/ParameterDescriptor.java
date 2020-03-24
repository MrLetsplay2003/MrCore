package me.mrletsplay.mrcore.misc.classfile;

public class ParameterDescriptor {
	
	private TypeDescriptor parameterType;
	
	public ParameterDescriptor(TypeDescriptor parameterType) {
		this.parameterType = parameterType;
	}
	
	public TypeDescriptor getParameterType() {
		return parameterType;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof ParameterDescriptor)) return false;
		ParameterDescriptor o = (ParameterDescriptor) obj;
		return parameterType.equals(o.parameterType);
	}

}
