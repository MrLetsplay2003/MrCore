package me.mrletsplay.mrcore.misc.classfile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.mrletsplay.mrcore.misc.ClassUtils;
import me.mrletsplay.mrcore.misc.PrimitiveType;

public class TypeDescriptor {
	
	public static final TypeDescriptor
		VOID = of(void.class);
	
	private String rawDescriptor, className;
	private boolean isPrimitive;
	
	private TypeDescriptor(String rawDescriptor) {
		this.rawDescriptor = rawDescriptor;
		if(rawDescriptor.endsWith(";")) rawDescriptor = rawDescriptor.substring(0, rawDescriptor.length() - 1);
		rawDescriptor = rawDescriptor.replace('/', '.');
		isPrimitive = true;
		if(rawDescriptor.length() == 1) {
			switch(rawDescriptor.charAt(0)) {
				case 'Z':
					className = "boolean";
					break;
				case 'B':
					className = "byte";
					break;
				case 'C':
					className = "char";
					break;
				case 'S':
					className = "short";
					break;
				case 'I':
					className = "int";
					break;
				case 'J':
					className = "long";
					break;
				case 'F':
					className = "float";
					break;
				case 'D':
					className = "double";
					break;
				case 'V':
					className = "void";
					break;
				default:
					throw new IllegalArgumentException("Invalid descriptor \"" + rawDescriptor + "\"");
			}
		}else {
			switch(rawDescriptor.charAt(0)) {
				case 'L':
					className = rawDescriptor.substring(1);
					isPrimitive = false;
					break;
				case '[':
					int i = 0;
					while(rawDescriptor.charAt(++i) == '[') continue;
					TypeDescriptor tDsc = new TypeDescriptor(rawDescriptor.substring(i));
					if(tDsc.isPrimitive()) {
						className = rawDescriptor;
					}else {
						className = rawDescriptor + ";";
					}
					isPrimitive = false;
					break;
				default:
					className = rawDescriptor;
					isPrimitive = false;
					break;
			}
		}
	}
	
	private TypeDescriptor(Class<?> clazz) {
		this.className = clazz.getName();
		this.isPrimitive = ClassUtils.isPrimitiveTypeClass(clazz);
		if(!isPrimitive && !clazz.isArray()) {
			this.rawDescriptor = "L" + className.replace('.', '/') + ";";
		}else if(clazz.isArray()){
			this.rawDescriptor = className.replace('.', '/');
		}else {
			this.rawDescriptor = PrimitiveType.getByPrimitiveClass(clazz).getSignatureName();
		}
	}
	
	public String getClassName() {
		return className;
	}
	
	public String getRawDescriptor() {
		return rawDescriptor;
	}
	
	public boolean isArray() {
		return className.charAt(0) == '[';
	}
	
	public boolean isPrimitive() {
		return isPrimitive;
	}
	
	public boolean isVoid() {
		return className.equals("void");
	}
	
	public PrimitiveType getPrimitiveType() {
		if(!isPrimitive) return PrimitiveType.OBJECT;
		switch (className) {
			case "boolean":
				return PrimitiveType.BOOLEAN;
			case "char":
				return PrimitiveType.CHAR;
			case "byte":
				return PrimitiveType.BYTE;
			case "short":
				return PrimitiveType.SHORT;
			case "int":
				return PrimitiveType.INT;
			case "long":
				return PrimitiveType.LONG;
			case "double":
				return PrimitiveType.DOUBLE;
			case "float":
				return PrimitiveType.FLOAT;
			case "void":
				return PrimitiveType.VOID;
			default:
				throw new IllegalStateException("Invalid primitive type class");
		}
	}
	
	public TypeDescriptor getArrayType() {
		if(!isArray()) throw new RuntimeException("Not an array descriptor");
		return new TypeDescriptor(className.substring(1));
	}
	
	public Class<?> toClass() throws ClassNotFoundException {
		switch(className) {
			case "boolean":
				return boolean.class;
			case "char":
				return char.class;
			case "byte":
				return byte.class;
			case "short":
				return short.class;
			case "int":
				return int.class;
			case "long":
				return long.class;
			case "double":
				return double.class;
			case "float":
				return float.class;
			case "void":
				return void.class;
			default:
				return Class.forName(className);
		}
	}
	
	public String getFriendlyName() {
		if(isArray()) {
			return getArrayType().getFriendlyName() + "[]";
		}else {
			if(className.contains(".")) {
				return className.substring(className.lastIndexOf('.') + 1);
			}else {
				return className;
			}
		}
	}
	
	@Override
	public String toString() {
		return rawDescriptor;
	}
	
	public static TypeDescriptor parse(String rawDescriptor) {
		return new TypeDescriptor(rawDescriptor);
	}
	
	public static TypeDescriptor of(Class<?> typeClass) {
		return new TypeDescriptor(typeClass);
	}
	
	public static List<TypeDescriptor> parseMulti(String multi) {
		List<Character> cs = Arrays.asList('Z', 'B', 'C', 'S', 'I', 'J', 'F', 'D', 'V', 'L', '[');
		List<TypeDescriptor> ds = new ArrayList<>();
		int i = 0, sI = 0;
		while(i < multi.length()) {
			char c = multi.charAt(i);
			switch(c) {
				case 'L':
					while(multi.charAt(i++) != ';') continue;
					ds.add(new TypeDescriptor(multi.substring(sI, i)));
					sI = i;
					continue;
				case '[':
					i++;
					continue;
			}
			if(cs.contains(c)) {
				ds.add(new TypeDescriptor(multi.substring(sI, i + 1)));
				sI = i + 1;
				i++;
			}else {
				throw new IllegalArgumentException("Invalid multi type descriptor String \"" + multi + "\"");
			}
		}
		return ds;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof TypeDescriptor)) return false;
		TypeDescriptor o = (TypeDescriptor) obj;
		return rawDescriptor.equals(o.rawDescriptor);
	}

}
