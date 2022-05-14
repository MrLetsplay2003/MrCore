package me.mrletsplay.mrcore.misc.classfile.util;

public interface ProxyHandler {
	
	public Object handle(Object proxyInstance, String methodName, String methodSignature, Object[] args);

}
