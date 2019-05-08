package me.mrletsplay.mrcore.misc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ClassUtils {

	private static final List<Class<?>> PRIMITIVE_TYPE_CLASSES = Collections.unmodifiableList(Arrays.asList(
				void.class, byte.class, short.class, int.class, long.class, float.class, double.class, char.class, boolean.class
			));
	
	public static boolean isPrimitiveTypeClass(Class<?> clazz) {
		return PRIMITIVE_TYPE_CLASSES.contains(clazz);
	}
	
	public static Class<?> getArrayBaseClass(Class<?> arrayClass) {
		Class<?> cl = arrayClass;
		while(cl.isArray()) cl = cl.getComponentType();
		return cl;
	}
	
	public static int getArrayDimensions(Class<?> arrayClass) {
		int d = 0;
		while(arrayClass.isArray()) d++;
		return d;
	}
	
}
