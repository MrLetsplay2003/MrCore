package me.mrletsplay.mrcore.misc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface Complex<T> {
	
	public static <T> Optional<T> defaultCast(Object o, Class<T> typeClass) {
		if(o == null) return Optional.ofNullable(null);
		if(!typeClass.isInstance(o)) return Optional.empty();
		return Optional.of(typeClass.cast(o));
	}
	
	public default Optional<T> cast(Object o) {
		return cast(o, Complex::defaultCast);
	}
	
	public Optional<T> cast(Object o, CastingFunction castingFunction);
	
	public String getFriendlyClassName();
	
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

		@Override
		public Optional<T> cast(Object o, CastingFunction castingFunction) {
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
		public Optional<List<T>> cast(Object o, CastingFunction castingFunction) {
			return cast(o, castingFunction, new ArrayList<>());
		}

		public Optional<List<T>> cast(Object o, CastingFunction castingFunction, List<T> toList) {
			if(o == null) return Optional.ofNullable(null);
			if(!(o instanceof List<?>)) return Optional.empty();
			List<?> oList = (List<?>) o;
			for(Object e : oList) {
				Optional<T> c = typeClass.cast(e, castingFunction);
				if(!c.isPresent()) return Optional.empty();
				toList.add(c.get());
			}
			return Optional.of(toList);
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
		public Optional<Map<K, V>> cast(Object o, CastingFunction castingFunction) {
			return cast(o, castingFunction, new HashMap<>());
		}

		public Optional<Map<K, V>> cast(Object o, CastingFunction castingFunction, Map<K, V> toMap) {
			if(o == null) return Optional.ofNullable(null);
			if(!(o instanceof Map<?, ?>)) return Optional.empty();
			Map<?, ?> oMap = (Map<?, ?>) o;
			for(Map.Entry<?, ?> e : oMap.entrySet()) {
				Optional<K> k = keyClass.cast(e.getKey(), castingFunction);
				if(!k.isPresent()) return Optional.empty();
				Optional<V> v = valueClass.cast(e.getValue(), castingFunction);
				if(!v.isPresent()) return Optional.empty();
				toMap.put(k.get(), v.get());
			}
			return Optional.of(toMap);
		}

		@Override
		public String getFriendlyClassName() {
			return "Map<" + keyClass.getFriendlyClassName() + ", " + valueClass.getFriendlyClassName() + ">";
		}
		
	}
	
}
