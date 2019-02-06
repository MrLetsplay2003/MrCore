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
		returnType = new TypeDescriptor(m.group("return"));
	}
	
	public MethodDescriptor(String name, TypeDescriptor returnType, ParameterDescriptor... parameterDescriptors) {
		this.name = name;
		this.parameterDescriptors = parameterDescriptors;
		this.returnType = returnType;
	}
	
	public boolean isConstructor() {
		return name.equals("<init>");
	}
	
	public String getName() {
		return name;
	}
	
	public ParameterDescriptor[] getParameterDescriptors() {
		return parameterDescriptors;
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
	
}
