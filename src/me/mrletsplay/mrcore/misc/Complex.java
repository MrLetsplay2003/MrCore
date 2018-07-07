package me.mrletsplay.mrcore.misc;

import java.util.List;
import java.util.Map;

public interface Complex<T> {
	
	public static <T> Complex<T> value(Class<T> clazz) {
		return new ComplexValue<>(clazz);
	}
	
	public static <T> ComplexList<T> list(Complex<T> clazz) {
		return new ComplexList<>(clazz);
	}
	
	public static <T> ComplexList<T> list(Class<T> clazz) {
		return new ComplexList<>(value(clazz));
	}
	
	public static <K, V> ComplexMap<K, V> map(Complex<K> keyClass, Complex<V> valueClass) {
		return new ComplexMap<>(keyClass, valueClass);
	}
	
	public static <K, V> ComplexMap<K, V> map(Class<K> keyClass, Complex<V> valueClass) {
		return new ComplexMap<>(value(keyClass), valueClass);
	}
	
	public static <K, V> ComplexMap<K, V> map(Complex<K> keyClass, Class<V> valueClass) {
		return new ComplexMap<>(keyClass, value(valueClass));
	}
	
	public static <K, V> ComplexMap<K, V> map(Class<K> keyClass, Class<V> valueClass) {
		return new ComplexMap<>(value(keyClass), value(valueClass));
	}
	
	public static class ComplexValue<T> implements Complex<T> {
		
		private Class<T> typeClass;
		
		private ComplexValue(Class<T> typeClass) {
			this.typeClass = typeClass;
		}
		
		public Class<T> getTypeClass() {
			return typeClass;
		}
		
	}
	
	public static class ComplexList<T> implements Complex<List<T>> {
		
		private Complex<T> typeClass;
		
		private ComplexList(Complex<T> typeClass) {
			this.typeClass = typeClass;
		}
		
		public Complex<T> getType() {
			return typeClass;
		}
		
	}
	
	public static class ComplexMap<K, V> implements Complex<Map<K, V>> {
		
		private Complex<K> keyClass;
		private Complex<V> valueClass;
		
		private ComplexMap(Complex<K> keyClass, Complex<V> valueClass) {
			this.keyClass = keyClass;
			this.valueClass = valueClass;
		}
		
		public Complex<K> getKeyType() {
			return keyClass;
		}
		
		public Complex<V> getValueType() {
			return valueClass;
		}
		
	}
	
}
