package me.mrletsplay.mrcore.misc.classfile.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.misc.ClassUtils;
import me.mrletsplay.mrcore.misc.EnumFlagCompound;
import me.mrletsplay.mrcore.misc.FriendlyException;
import me.mrletsplay.mrcore.misc.classfile.ClassAccessFlag;
import me.mrletsplay.mrcore.misc.classfile.ClassField;
import me.mrletsplay.mrcore.misc.classfile.ClassFile;
import me.mrletsplay.mrcore.misc.classfile.ClassMethod;
import me.mrletsplay.mrcore.misc.classfile.FieldAccessFlag;
import me.mrletsplay.mrcore.misc.classfile.MethodAccessFlag;
import me.mrletsplay.mrcore.misc.classfile.MethodDescriptor;
import me.mrletsplay.mrcore.misc.classfile.TypeDescriptor;
import me.mrletsplay.mrcore.misc.classfile.attribute.Attribute;
import me.mrletsplay.mrcore.misc.classfile.attribute.AttributeCode;
import me.mrletsplay.mrcore.misc.classfile.pool.entry.ConstantPoolClassEntry;

public class ProxyUtils {
	
	private static final String PROXY_HANDLER_FIELD = "proxyHandler";
	
	private static int proxyID;
	
	/**
	 * Internal only! Do not call this method normally
	 * @param proxyInstance
	 * @param methodName
	 * @param methodSignature
	 * @param args
	 * @return
	 */
	public static Object proxyInvoke(Object proxyInstance, String methodName, String methodSignature, Object[] args) {
		try {
			Field hdF = proxyInstance.getClass().getDeclaredField(PROXY_HANDLER_FIELD);
			hdF.setAccessible(true);
			ProxyHandler hd = (ProxyHandler) hdF.get(proxyInstance);
			if(hd == null) throw new FriendlyException("Handler is not set");
			return hd.handle(proxyInstance, methodName, methodSignature, args);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			throw new FriendlyException("Failed to invoke proxy method", e);
		}
	}

	/**
	 * Creates a proxy class file with the provided superclass that implements all of the given interfaces.
	 * This class does not have any constructors.
	 * @param className The canonical name the class should have, providing <code>null</code> will result in an automatically generated name
	 * @param overrideImplemented Whether non-abstract/default methods should be overridden and redirected to the proxyHandler
	 * @param superclass The superclass for the proxy class
	 * @param interfaces The interfaces this class should implement
	 * @return The newly created proxy class
	 * @throws IllegalArgumentException If the superclass is an interface, an interface is not an interface class, or more than 65535 interfaces are provided
	 * @throws FriendlyException If creating/loading the class fails due to some other reason
	 */
	public static synchronized ClassFile createProxyClassFile(String className, boolean overrideImplemented, Class<?> superclass, Class<?>... interfaces) throws FriendlyException {
		if(interfaces.length > 65535) throw new IllegalArgumentException("Interface limit exceeded");
		if(superclass.isInterface()) throw new IllegalArgumentException("Superclass cannot be an interface");
		if(Arrays.stream(interfaces).anyMatch(i -> !i.isInterface())) throw new IllegalArgumentException("All implemented classes need to be interfaces");
		
		if(className == null) className = "me.mrletsplay.mrcore.misc.classfile.util.Proxy$" + (proxyID++);
		
		ClassFile cf = new ClassFile(className.replace('.', '/'), superclass);
		
		cf.setFields(new ClassField[] {
			new ClassField(cf, EnumFlagCompound.of(FieldAccessFlag.PRIVATE), PROXY_HANDLER_FIELD, TypeDescriptor.of(ProxyHandler.class))	
		});
		
		ConstantPoolClassEntry[] ifCls = new ConstantPoolClassEntry[interfaces.length];
		
		for(int i = 0; i < interfaces.length; i++) {
			ifCls[i] = cf.getConstantPool().getEntry(ClassFileUtils.getOrAppendClass(cf, ClassFileUtils.getOrAppendUTF8(cf, interfaces[i].getCanonicalName().replace('.', '/')))).as(ConstantPoolClassEntry.class);
		}
		
		cf.setInterfaces(ifCls);
		
		cf.getAccessFlags().addFlag(ClassAccessFlag.FINAL);
		
		try {
			List<MethodDescriptor> ms = new ArrayList<>();
			
			for(Method m : ClassUtils.getMethods(superclass)) {
				if(Modifier.isStatic(m.getModifiers())) continue;
				if(!Modifier.isAbstract(m.getModifiers()) && !overrideImplemented) continue;
				MethodDescriptor d = ClassFileUtils.getMethodDescriptor(m);
				
				ms.add(d);
			}
			
			for(Class<?> in : interfaces) {
				for(Method m : ClassUtils.getMethods(in)) {
					if(Modifier.isStatic(m.getModifiers())) continue;
					if(!Modifier.isAbstract(m.getModifiers()) && !overrideImplemented) continue;
					MethodDescriptor d = ClassFileUtils.getMethodDescriptor(m);
					
					ms.add(d);
				}
			}
			
			ms = checkIncompatibleMethods(ms);
			
			List<ClassMethod> cMs = new ArrayList<>();
			for(MethodDescriptor d : ms) {
				ClassMethod cm = new ClassMethod(cf, d);
				cm.getAccessFlags().addFlag(MethodAccessFlag.PUBLIC);
				AttributeCode c = new AttributeCode(cf);
				c.setMaxLocals(d.getParameterDescriptors().length * 2 + 1);
				cm.setAttributes(new Attribute[] {c});
				cMs.add(cm);
			}
			
			cf.setMethods(cMs.toArray(new ClassMethod[cMs.size()]));
			
			ClassFileUtils.redirectClassMethodsNamed(cf, ProxyUtils.class, ProxyUtils.class.getMethod("proxyInvoke", Object.class, String.class, String.class, Object[].class));
			
			return cf;
		}catch(IOException | NoSuchMethodException | SecurityException e) {
			throw new FriendlyException(e);
		}
	}
	
	/**
	 * Creates a proxy class with the provided superclass that implements all of the given interfaces.
	 * This class is not instantiable except by using {@link #instantiate(Class)}, as it does not declare any constructors.
	 * It is recommended to use {@link #createProxy(ClassLoader, ProxyHandler, boolean, Class, Class...)} instead to also set a proxyHandler
	 * @param loader The ClassLoader this class should be loaded into
	 * @param className The canonical name the class should have, providing <code>null</code> will result in an automatically generated name
	 * @param overrideImplemented Whether non-abstract/default methods should be overridden and redirected to the proxyHandler
	 * @param superclass The superclass for the proxy class
	 * @param interfaces The interfaces this class should implement
	 * @return The newly created proxy class
	 * @throws IllegalArgumentException If the superclass is an interface, an interface is not an interface class, or more than 65535 interfaces are provided
	 * @throws FriendlyException If creating/loading the class fails due to some other reason
	 */
	public static Class<?> createProxyClass(ClassLoader loader, String className, boolean overrideImplemented, Class<?> superclass, Class<?>... interfaces) throws FriendlyException {
		try {
			ByteArrayOutputStream bOut = new ByteArrayOutputStream();
			createProxyClassFile(className, overrideImplemented, superclass, interfaces).write(bOut);
			
			Method defClass = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class);
			defClass.setAccessible(true);
			
			byte[] bytes = bOut.toByteArray();
			Object cls = defClass.invoke(loader, className.substring(0, className.length()), bytes, 0, bytes.length);
			return (Class<?>) cls;
		}catch(IOException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new FriendlyException(e);
		}
	}
	
	/**
	 * Creates a proxy class as defined by {@link #createProxyClass(ClassLoader, boolean, Class, Class...)} with <code>overrideImplemented</code> set to <code>false</code>
	 * @param overrideImplemented Whether non-abstract/default methods should be overridden and redirected to the proxyHandler
	 * @param superclass The superclass for the proxy class
	 * @param interfaces The interfaces this class should implement
	 * @return The newly created proxy class
	 * @throws IllegalArgumentException If the superclass is an interface, an interface is not an interface class, or more than 65535 interfaces are provided
	 * @throws FriendlyException If creating/loading the class fails due to some other reason
	 */
	public static Class<?> createProxyClass(ClassLoader loader, Class<?> superclass, Class<?>... interfaces) throws FriendlyException {
		return createProxyClass(loader, null, false, superclass, interfaces);
	}
	
	private static List<MethodDescriptor> checkIncompatibleMethods(List<MethodDescriptor> methods) {
		List<MethodDescriptor> removedMethods = new ArrayList<>();
		List<MethodDescriptor> newMethods = new ArrayList<>();
		for(MethodDescriptor m : methods) {
			if(removedMethods.contains(m)) continue;
			List<MethodDescriptor> ms = methods.stream()
					.filter(o -> {
						if(o == m) return false;
						return m.getName().equals(o.getName()) && m.getParameterSignature().equals(o.getParameterSignature());
					})
					.collect(Collectors.toList());
			
			if(ms.isEmpty()) {
				newMethods.add(m);
				continue;
			}
			
			ms.add(m);
			
			try {
				m.setReturnType(TypeDescriptor.of(getMostSpecificCommonReturnType(ms)));
			} catch (ClassNotFoundException e) {
				throw new FriendlyException(e);
			}
			
			newMethods.add(m);
			removedMethods.addAll(ms);
		}
		return newMethods;
	}
	
	private static Class<?> getMostSpecificCommonReturnType(List<MethodDescriptor> methods) throws ClassNotFoundException {
		Class<?> type = methods.get(0).getReturnType().toClass();
		MethodDescriptor d = methods.get(0);
		for(MethodDescriptor o : methods) {
			Class<?> cl = o.getReturnType().toClass();
			if(cl.isAssignableFrom(type)) continue;
			if(type.isAssignableFrom(cl)) {
				type = cl;
				d = o;
				continue;
			}
			throw new FriendlyException("Incompatible methods: " + o + " & " + d);
		}
		return type;
	}
	
	/**
	 * Instantiates any class. Only works for Sun VMs (JRE, JDK, OpenJDK)
	 * @param <T> The type of the class to instantiate
	 * @param classToInstantiate The class to instantiate
	 * @return The instantiated object
	 * @throws Exception If instantiation fails
	 */
	public static <T> T instantiate(Class<T> classToInstantiate) throws Exception {
		if(classToInstantiate.isEnum()) throw new IllegalArgumentException("Cannot instantiate enums");
		if(classToInstantiate.isInterface()) throw new IllegalArgumentException("Cannot instantiate interfaces");
		if(classToInstantiate.isPrimitive()) throw new IllegalArgumentException("Cannot instantiate primitive types");
		if(Modifier.isAbstract(classToInstantiate.getModifiers())) throw new IllegalArgumentException("Cannot instantiate abstract classes");
		
		Constructor<?> c = Constructor.class.getDeclaredConstructor(Class.class, Class[].class, Class[].class, int.class, int.class, String.class, byte[].class, byte[].class);
		
		c.setAccessible(true);
		
		Constructor<?> cons = (Constructor<?>) c.newInstance(classToInstantiate, new Class[0], new Class[0], Modifier.PUBLIC, 0, "()V", new byte[0], new byte[0]);
		
		Class<?> mAccClass = Class.forName("sun.reflect.MethodAccessorGenerator");
		Method m = mAccClass.getDeclaredMethod("generateSerializationConstructor", Class.class, Class[].class, Class[].class, int.class, Class.class);
		m.setAccessible(true);
		
		Constructor<?> co = mAccClass.getDeclaredConstructor();
		co.setAccessible(true);
		Object mc = co.newInstance();
		
		Object acc = m.invoke(mc, classToInstantiate, new Class[0], new Class[0], Modifier.PUBLIC, Object.class);
		
		Method sA = cons.getClass().getDeclaredMethod("setConstructorAccessor", Class.forName("sun.reflect.ConstructorAccessor"));
		sA.setAccessible(true);
		
		sA.invoke(cons, acc);
		
		return classToInstantiate.cast(cons.newInstance());
	}
	
	/**
	 * Creates a proxy instance using a proxy class defined by {@link #createProxyClass(ClassLoader, boolean, Class, Class...)}
	 * @param loader The ClassLoader the proxy class should be loaded into
	 * @param proxyHandler The proxy handler for this proxy. All implemented methods will be redirected to it
	 * @param overrideImplemented Whether non-abstract/default methods should be overridden and redirected to the proxyHandler
	 * @param superclass The superclass for the proxy class
	 * @param interfaces The interfaces the proxy class should implement
	 * @return The newly created proxy instance
	 * @throws IllegalArgumentException As defined by {@link #createProxyClass(ClassLoader, boolean, Class, Class...)}
	 * @throws FriendlyException As defined by {@link #createProxyClass(ClassLoader, boolean, Class, Class...)}
	 */
	public static Object createProxy(ClassLoader loader, ProxyHandler proxyHandler, boolean overrideImplemented, Class<?> superclass, Class<?>... interfaces) {
		try {
			Class<?> proxyClass = createProxyClass(loader, null, overrideImplemented, superclass, interfaces);
			Object inst = instantiate(proxyClass);
			Field f = proxyClass.getDeclaredField(PROXY_HANDLER_FIELD);
			f.setAccessible(true);
			f.set(inst, proxyHandler);
			return inst;
		} catch(Exception e) {
			throw new FriendlyException("Failed to create proxy instance", e);
		}
	}

	/**
	 * Creates a proxy instance using a proxy class defined by {@link #createProxyClass(ClassLoader, boolean, Class, Class...)} with <code>overrideImplemented</code> set to <code>false</code>
	 * @param loader The ClassLoader the proxy class should be loaded into
	 * @param proxyHandler The proxy handler for this proxy. All implemented methods will be redirected to it
	 * @param superclass The superclass for the proxy class
	 * @param interfaces The interfaces the proxy class should implement
	 * @return The newly created proxy instance
	 * @throws IllegalArgumentException As defined by {@link #createProxyClass(ClassLoader, boolean, Class, Class...)}
	 * @throws FriendlyException As defined by {@link #createProxyClass(ClassLoader, boolean, Class, Class...)}
	 */
	public static Object createProxy(ClassLoader loader, ProxyHandler proxyHandler, Class<?> superclass, Class<?>... interfaces) {
		return createProxy(loader, proxyHandler, false, superclass, interfaces);
	}
	
}
