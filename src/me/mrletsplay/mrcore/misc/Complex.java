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
	
	public NullableOptional<T> cast(Object o, CastingFunction castingFunction);
	
	public default boolean isInstance(Object o, CastingFunction castingFunction) {
		return cast(o, castingFunction).isPresent();
	}
	
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
	
	public static class ComplexValue<T> implements Complex<T> {
		
		private Class<T> typeClass;
		
		private ComplexValue(Class<T> typeClass) {
			this.typeClass = typeClass;
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
			if(!(o instanceof List<?>)) return NullableOptional.empty();
			List<?> oList = (List<?>) o;
			for(Object e : oList) {
				NullableOptional<T> c = typeClass.cast(e, castingFunction);
				if(!c.isPresent()) return NullableOptional.empty();
				toList.add(c.get());
			}
			return NullableOptional.of(toList);
		}

		@Override
		public String getFriendlyClassName() {
			return "List<" + typeClass.getFriendlyClassName() + ">";
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
			if(!(o instanceof Map<?, ?>)) return NullableOptional.empty();
			Map<?, ?> oMap = (Map<?, ?>) o;
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
		public String getFriendlyClassName() {
			return "Map<" + keyClass.getFriendlyClassName() + ", " + valueClass.getFriendlyClassName() + ">";
		}
		
	}
	
}
