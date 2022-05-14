package me.mrletsplay.mrcore.misc.classfile;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.misc.EnumFlagCompound;

public class MethodDescriptor {

	private static final Pattern PATTERN_METHOD_SIGNATURE = Pattern.compile("\\((?<types>(.)*)\\)(?<return>(.)+)");
	
	private String name;
	private EnumFlagCompound<MethodAccessFlag> accessFlags;
	private ParameterDescriptor[] parameterDescriptors;
	private TypeDescriptor returnType;
	
	public MethodDescriptor(String name, EnumFlagCompound<MethodAccessFlag> accessFlags, String methodSignature) {
		this.name = name;
		this.accessFlags = accessFlags;
		Matcher m = PATTERN_METHOD_SIGNATURE.matcher(methodSignature);
		if(!m.matches()) throw new IllegalArgumentException("Invalid method signature");
		List<TypeDescriptor> parameterTypeDescriptors = TypeDescriptor.parseMulti(m.group("types"));
		parameterDescriptors = parameterTypeDescriptors.stream().map(t -> new ParameterDescriptor(t)).toArray(ParameterDescriptor[]::new);
		returnType = TypeDescriptor.parse(m.group("return"));
	}
	
	public MethodDescriptor(String name, EnumFlagCompound<MethodAccessFlag> accessFlags, TypeDescriptor returnType, ParameterDescriptor... parameterDescriptors) {
		this.name = name;
		this.accessFlags = accessFlags;
		this.parameterDescriptors = parameterDescriptors;
		this.returnType = returnType;
	}
	
	public MethodDescriptor(String name, EnumFlagCompound<MethodAccessFlag> accessFlags, TypeDescriptor returnType, TypeDescriptor... parameterTypeDescriptors) {
		this.name = name;
		this.parameterDescriptors = new ParameterDescriptor[parameterTypeDescriptors.length];
		this.returnType = returnType;
		this.accessFlags = accessFlags;
		
		for(int i = 0; i < parameterTypeDescriptors.length; i++) {
			parameterDescriptors[i] = new ParameterDescriptor(parameterTypeDescriptors[i]);
		}
	}
	
	public boolean isConstructor() {
		return name.equals("<init>");
	}
	
	public String getName() {
		return name;
	}
	
	public EnumFlagCompound<MethodAccessFlag> getAccessFlags() {
		return accessFlags;
	}
	
	public ParameterDescriptor[] getParameterDescriptors() {
		return parameterDescriptors;
	}
	
	public String getParameterSignature() {
		return Arrays.stream(parameterDescriptors).map(p -> p.getParameterType().getRawDescriptor()).collect(Collectors.joining());
	}
	
	public String getRawDescriptor() {
		return "(" + getParameterSignature() + ")" + getReturnType().getRawDescriptor();
	}
	
	public void setReturnType(TypeDescriptor returnType) {
		this.returnType = returnType;
	}
	
	public TypeDescriptor getReturnType() {
		return returnType;
	}
	
	@Override
	public String toString() {
		return accessFlags.getApplicable().stream().map(f -> f.getName()).collect(Collectors.joining(" ")) + " " +
				returnType.getFriendlyName() + " " +
				name + "(" + Arrays.stream(parameterDescriptors).map(p -> p.getParameterType().getFriendlyName()).collect(Collectors.joining(", ")) + ")";
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof MethodDescriptor)) return false;
		MethodDescriptor o = (MethodDescriptor) obj;
		return name.equals(o.name)
				&& Arrays.equals(parameterDescriptors, o.parameterDescriptors)
				&& returnType.equals(o.returnType)
				&& accessFlags.equals(o.accessFlags);
	}
	
}
