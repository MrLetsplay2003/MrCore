package me.mrletsplay.mrcore.misc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface Complex<T> {
	
	public static <T> NullableOptional<T> defaultCast(Object o, Class<T> typeClass) {
		if(o == null) return NullableOptional.of(null);
		if(!typeClass.isInstance(o)) return NullableOptional.empty();
		return NullableOptional.of(typeClass.cast(o));
	}
	
	public default NullableOptional<T> cast(Object o) {
		return cast(o, Complex::defaultCast);
	}
	
	public default boolean isInstance(Object o) {
		return cast(o).isPresent();
	}
	
	public default boolean isAssignableFrom(Class<?> clazz) {
		return isAssignableFrom(value(clazz));
	}
	
	public boolean isAssignableFrom(Complex<?> clazz);
	
	public NullableOptional<T> cast(Object o, CastingFunction castingFunction);
	
	public default boolean isInstance(Object o, CastingFunction castingFunction) {
		return cast(o, castingFunction).isPresent();
	}
	
	public Complex<?> getCommonClass(Complex<?> other);
	
	public Class<?> getBaseClass();
	
	public String getFriendlyClassName();
	
	public static <T> Complex<T> value(Class<T> clazz) {
		if(ClassUtils.isPrimitiveTypeClass(clazz)) throw new IllegalArgumentException("Primitive types are not allowed");
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
	
	public static <E> NullableOptional<List<E>> castList(List<?> list, Class<E> toClass) {
		return Complex.list(toClass).cast(list);
	}
	
	public static <E> NullableOptional<List<E>> castList(List<?> list, Class<E> toClass, CastingFunction castingFunction) {
		return Complex.list(toClass).cast(list, castingFunction);
	}
	
	public static <K, V> NullableOptional<Map<K, V>> castMap(Map<?, ?> map, Class<K> keyClass, Class<V> valueClass) {
		return Complex.map(keyClass, valueClass).cast(map);
	}
	
	public static <K, V> NullableOptional<Map<K, V>> castMap(Map<?, ?> map, Class<K> keyClass, Class<V> valueClass, CastingFunction castingFunction) {
		return Complex.map(keyClass, valueClass).cast(map, castingFunction);
	}
	
	public static Complex<?> getCommonClass(Complex<?>... types) {
		if(types.length == 0) throw new IllegalArgumentException("Can't get common type of no classes");
		Complex<?> commonType = types[0];
		for(Complex<?> o : types) {
			commonType = o.getCommonClass(commonType);
		}
		return commonType;
	}
	
	public static Complex<?> typeOf(Object o) {
		if(o == null) return Complex.value(Object.class); // TODO
		if(o instanceof List) {
			return ComplexList.typeOf((List<?>) o);
		}else if(o instanceof Map) {
			return ComplexMap.typeOf((Map<?, ?>) o);
		}else {
			return Complex.value(o.getClass());
		}
	}
	
	public static class ComplexValue<T> implements Complex<T> {
		
		private Class<T> typeClass;
		
		private ComplexValue(Class<T> typeClass) {
			this.typeClass = typeClass;
		}

		@Override
		public Class<?> getBaseClass() {
			return typeClass;
		}
		
		public Class<T> getTypeClass() {
			return typeClass;
		}
		
		@Override
		public NullableOptional<T> cast(Object o, CastingFunction castingFunction) {
			return castingFunction.cast(o, typeClass);
		}

		@Override
		public String getFriendlyClassName() {
			return typeClass.getName();
		}
		
		@Override
		public boolean isAssignableFrom(Complex<?> clazz) {
			return typeClass.isAssignableFrom(clazz.getBaseClass());
		}
		
		@Override
		public boolean equals(Object obj) {
			if(!(obj instanceof ComplexValue<?>)) return false;
			return ((ComplexValue<?>) obj).getBaseClass().equals(typeClass);
		}
		
		@Override
		public Complex<?> getCommonClass(Complex<?> other) {
			Class<?> commonType = typeClass;
			while(!commonType.isAssignableFrom(other.getBaseClass())) {
				commonType = commonType.getSuperclass();
				if(commonType == null) return Complex.value(Object.class); // type is an interface
			}
			return Complex.value(commonType);
		}
		
		@Override
		public String toString() {
			return "Complex[" + getFriendlyClassName() + "]";
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

		@Override
		public NullableOptional<List<T>> cast(Object o, CastingFunction castingFunction) {
			return cast(o, castingFunction, new ArrayList<>());
		}

		public NullableOptional<List<T>> cast(Object o, CastingFunction castingFunction, List<T> toList) {
			if(o == null) return NullableOptional.of(null);
			NullableOptional<?> list = castingFunction.cast(o, List.class);
			if(!list.isPresent()) {
				return NullableOptional.empty();
			}
			
			List<?> oList = (List<?>) list.get();
			for(Object e : oList) {
				NullableOptional<T> c = typeClass.cast(e, castingFunction);
				if(!c.isPresent()) return NullableOptional.empty();
				toList.add(c.get());
			}
			return NullableOptional.of(toList);
		}

		@Override
		public Class<?> getBaseClass() {
			return List.class;
		}

		@Override
		public String getFriendlyClassName() {
			return "List<" + typeClass.getFriendlyClassName() + ">";
		}

		@Override
		public boolean isAssignableFrom(Complex<?> clazz) {
			if(!(List.class.isAssignableFrom(clazz.getBaseClass()))) return false;
			if(clazz instanceof ComplexList<?>) {
				ComplexList<?> l = (ComplexList<?>) clazz;
				if(!typeClass.isAssignableFrom(l.getType())) return false;
			}
			return true;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(!(obj instanceof ComplexList<?>)) return false;
			return ((ComplexList<?>) obj).getType().equals(typeClass);
		}
		
		public static ComplexList<?> typeOf(List<?> list) {
			if(list.isEmpty()) return Complex.list(Object.class); // TODO
			Complex<?>[] c = list.stream().map(Complex::typeOf).toArray(Complex[]::new);
			return Complex.list(Complex.getCommonClass(c));
		}
		
		@Override
		public Complex<?> getCommonClass(Complex<?> other) {
			if(other instanceof ComplexList<?>) {
				ComplexList<?> o = (ComplexList<?>) other;
				return Complex.list(typeClass.getCommonClass(o.getType()));
			}else {
				return Complex.value(List.class).getCommonClass(other);
			}
		}
		
		@Override
		public String toString() {
			return "Complex[" + getFriendlyClassName() + "]";
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

		@Override
		public NullableOptional<Map<K, V>> cast(Object o, CastingFunction castingFunction) {
			return cast(o, castingFunction, new HashMap<>());
		}

		public NullableOptional<Map<K, V>> cast(Object o, CastingFunction castingFunction, Map<K, V> toMap) {
			if(o == null) return NullableOptional.of(null);
			NullableOptional<?> map = castingFunction.cast(o, Map.class);
			if(!map.isPresent()) {
				return NullableOptional.empty();
			}
			
			Map<?, ?> oMap = (Map<?, ?>) map.get();
			for(Map.Entry<?, ?> e : oMap.entrySet()) {
				NullableOptional<K> k = keyClass.cast(e.getKey(), castingFunction);
				if(!k.isPresent()) return NullableOptional.empty();
				NullableOptional<V> v = valueClass.cast(e.getValue(), castingFunction);
				if(!v.isPresent()) return NullableOptional.empty();
				toMap.put(k.get(), v.get());
			}
			return NullableOptional.of(toMap);
		}

		@Override
		public Class<?> getBaseClass() {
			return Map.class;
		}

		@Override
		public String getFriendlyClassName() {
			return "Map<" + keyClass.getFriendlyClassName() + ", " + valueClass.getFriendlyClassName() + ">";
		}

		@Override
		public boolean isAssignableFrom(Complex<?> clazz) {
			if(!(Map.class.isAssignableFrom(clazz.getBaseClass()))) return false;
			if(clazz instanceof ComplexList<?>) {
				ComplexMap<?, ?> l = (ComplexMap<?, ?>) clazz;
				if(!keyClass.isAssignableFrom(l.getKeyType())) return false;
				if(!valueClass.isAssignableFrom(l.getValueType())) return false;
			}
			return true;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(!(obj instanceof ComplexMap<?, ?>)) return false;
			return ((ComplexMap<?, ?>) obj).getKeyType().equals(keyClass) &&
					((ComplexMap<?, ?>) obj).getValueType().equals(valueClass);
		}
		
		public static ComplexMap<?, ?> typeOf(Map<?, ?> map) {
			if(map.isEmpty()) return Complex.map(Object.class, Object.class); // TODO
			Complex<?> keyType = Complex.getCommonClass(map.keySet().stream().map(Complex::typeOf).toArray(Complex[]::new));
			Complex<?> valueType = Complex.getCommonClass(map.values().stream().map(Complex::typeOf).toArray(Complex[]::new));
			return Complex.map(keyType, valueType);
		}
		
		@Override
		public Complex<?> getCommonClass(Complex<?> other) {
			if(other instanceof ComplexMap) {
				ComplexMap<?, ?> o = (ComplexMap<?, ?>) other;
				return Complex.map(keyClass.getCommonClass(o.getKeyType()), valueClass.getCommonClass(o.getValueType()));
			}else {
				return Complex.value(Map.class).getCommonClass(other);
			}
		}
		
		@Override
		public String toString() {
			return "Complex[" + getFriendlyClassName() + "]";
		}
		
	}
	
}
