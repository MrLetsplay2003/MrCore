package me.mrletsplay.mrcore.config.v2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.misc.Complex;
import me.mrletsplay.mrcore.misc.NullableOptional;

public enum ConfigValueType {

	/**
	 * Property is not set (may evaluate to default value)
	 */
	UNDEFINED((Complex<?>) null),
	
	/**
	 * Java Type: {@code null}
	 */
	NULL((Complex<?>) null),
	
	/**
	 * Java Type: {@code String}
	 */
	STRING(String.class),
	
	/**
	 * Java Type: {@code char}
	 */
	CHARACTER(Character.class),
	
	/**
	 * Java Type: {@code Boolean}
	 */
	BOOLEAN(Boolean.class),
	
	/**
	 * Java Type: {@code Number} (stored as {@code Long}) -> {@code Byte}, {@code Short}, {@code Integer}, {@code Long}
	 */
	NUMBER(Number.class, Byte.class, Short.class, Integer.class, Long.class),
	
	/**
	 * Java Type: {@code Number} (stored as {@code Double}) -> {@code Float}, {@code Double}
	 */
	DECIMAL(Number.class, Float.class, Double.class),
	
	/**
	 * Java Type: {@link ConfigSection}
	 */
	SECTION(ConfigSection.class),
	
	/**
	 * Java Type: {@link List}
	 */
	LIST(List.class);
	
	private final Complex<?> valueClass;
	private final List<Complex<?>> explicitValueTypes;
	
	private ConfigValueType(Complex<?> valueType, Complex<?>... explicitValueTypes) {
		this.valueClass = valueType;
		this.explicitValueTypes = Arrays.asList(explicitValueTypes);
	}
	
	private ConfigValueType(Class<?> valueType, Class<?>... explicitValueTypes) {
		this.valueClass = Complex.value(valueType);
		this.explicitValueTypes = Arrays.asList(explicitValueTypes).stream().map(Complex::value).collect(Collectors.toList());
	}
	
	public Complex<?> getValueClass() {
		return valueClass;
	}
	 
	public boolean isValidTypeClass(Complex<?> typeClass) {
		if(valueClass == null) return false;
		return explicitValueTypes.isEmpty() ? valueClass.isAssignableFrom(typeClass) : explicitValueTypes.stream().anyMatch(t -> t.isAssignableFrom(typeClass));
	}
	
	public static NullableOptional<?> createCompatible(ConfigSection forSection, Object o) {
		ConfigValueType type = getRawTypeOf(o);
		if(type != null) return NullableOptional.of(o);
		List<List<ObjectMapper<?, ?>>> paths = calculateCompatiblePaths(forSection, o, Complex.typeOf(o), new ArrayList<>());
		if(paths.isEmpty()) return NullableOptional.empty();
		for(List<ObjectMapper<?, ?>> path : paths) {
			Object val = o;
			try {
				for(ObjectMapper<?, ?> mapper : path) {
					val = mapper.mapRawObject(forSection, val, forSection::castType);
				}
			}catch(ObjectMappingException e) {
				continue;
			}
			return NullableOptional.of(val);
		}
		return NullableOptional.empty();
	}
	
	private static List<List<ObjectMapper<?, ?>>> calculateCompatiblePaths(ConfigSection section, Object o, Complex<?> clazz, List<Complex<?>> classes) {
		if(isConfigPrimitive(clazz)) return new ArrayList<>();
		classes.add(clazz);
		Comparator<Map.Entry<ObjectMapper<?, ?>, Integer>> c = Comparator.comparingInt(en -> en.getKey().getClassDepth(clazz));
		c = c.thenComparingInt(Map.Entry::getValue);
		List<ObjectMapper<?, ?>> mappers = section.getConfig().getMappers().entrySet().stream()
				.filter(m -> !classes.contains(m.getKey().getMappedClass()) && m.getKey().canMap(o, section::castType))
				.sorted(c)
				.map(Map.Entry::getKey)
				.collect(Collectors.toList());
		List<List<ObjectMapper<?, ?>>> pths = new ArrayList<>();
		for(ObjectMapper<?, ?> mapper : mappers) {
			if(isConfigPrimitive(mapper.getMappedClass())) pths.add(new ArrayList<>(Arrays.asList(mapper)));
			Object res;
			try {
				res = mapper.mapRawObject(section, o, section::castType);
			}catch(ObjectMappingException e) {
				continue;
			}
			List<List<ObjectMapper<?, ?>>> paths = calculateCompatiblePaths(section, res, mapper.getMappedClass(), classes);
			if(!paths.isEmpty()) {
				for(List<ObjectMapper<?, ?>> pt : paths) {
					pt.add(0, mapper);
				}
				pths.addAll(paths);
			}
		}
		return pths;
	}
	
	public static boolean isConfigPrimitive(Complex<?> typeClass) {
		return Arrays.stream(ConfigValueType.values()).anyMatch(v -> v.isValidTypeClass(typeClass));
	}
	
	public static ConfigValueType getRawTypeOf(Object o) {
		if(o == null) return ConfigValueType.NULL;
		if(o instanceof Number) {
			if(o instanceof Float || o instanceof Double) {
				return ConfigValueType.DECIMAL;
			}else if(o instanceof Byte || o instanceof Short || o instanceof Integer || o instanceof Long) {
				return ConfigValueType.NUMBER;
			}
		}else if(o instanceof String) {
			return ConfigValueType.STRING;
		}else if(o instanceof Character) {
			return ConfigValueType.CHARACTER;
		}else if(o instanceof Boolean) {
			return ConfigValueType.BOOLEAN;
		}else if(o instanceof ConfigSection) {
			return ConfigValueType.SECTION;
		}else if(o instanceof List) {
			return ConfigValueType.LIST;
		}
		return null;
	}
	
}
