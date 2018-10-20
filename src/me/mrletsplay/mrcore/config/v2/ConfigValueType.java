package me.mrletsplay.mrcore.config.v2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.misc.NullableOptional;

public enum ConfigValueType {

	/**
	 * Property is not set (may evaluate to default value)
	 */
	UNDEFINED(null),
	
	/**
	 * Java Type: {@code null}
	 */
	NULL(null),
	
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
	 * Java Type: {@code Number} -> {@code Byte}, {@code Short}, {@code Integer}, {@code Long}
	 */
	NUMBER(Number.class),
	
	/**
	 * Java Type: {@code Number} -> {@code Float}, {@code Double}
	 */
	DECIMAL(Number.class),
	
	/**
	 * Java Type: {@link ConfigSection}
	 */
	SECTION(ConfigSection.class),
	
	/**
	 * Java Type: {@link List}
	 */
	LIST(List.class);
	
	private final Class<?> valueClass;
	private final List<Class<?>> explicitValueTypes;
	
	private ConfigValueType(Class<?> valueType, Class<?>... explicitValueTypes) {
		this.valueClass = valueType;
		this.explicitValueTypes = Arrays.asList(explicitValueTypes);
	}
	
	public Class<?> getValueClass() {
		return valueClass;
	}
	
	public boolean isValidTypeClass(Class<?> typeClass) {
		if(valueClass == null) return false;
		return explicitValueTypes.isEmpty() ? valueClass.isAssignableFrom(typeClass) : explicitValueTypes.contains(typeClass);
	}
	
	public static NullableOptional<?> createCompatible(ConfigSection forSection, Object o) {
		ConfigValueType type = getRawTypeOf(o);
		if(type != null) return NullableOptional.of(o);
		List<ObjectMapper<?, ?>> path = calculateCompatiblePath(forSection, o.getClass(), new ArrayList<>());
		if(path == null) return NullableOptional.empty();
		Object val = o;
		for(ObjectMapper<?, ?> mapper : path) {
			val = mapper.mapRawObject(forSection, val);
		}
		return NullableOptional.of(val);
	}
	
	private static List<ObjectMapper<?, ?>> calculateCompatiblePath(ConfigSection section, Class<?> clazz, List<Class<?>> classes) {
		if(isConfigPrimitive(clazz)) return new ArrayList<>();
		classes.add(clazz);
		Comparator<Map.Entry<ObjectMapper<?, ?>, Integer>> c = Comparator.comparingInt(en -> en.getKey().getClassDepth(clazz));
		c = c.thenComparingInt(Map.Entry::getValue);
		List<ObjectMapper<?, ?>> mappers = section.getConfig().getMappers().entrySet().stream()
				.filter(m -> !classes.contains(m.getKey().getMappedClass()) && m.getKey().canMap(clazz))
				.sorted(c)
				.map(Map.Entry::getKey)
				.collect(Collectors.toList());
		List<ObjectMapper<?, ?>> pth = null;
		for(ObjectMapper<?, ?> mapper : mappers) {
			if(isConfigPrimitive(mapper.getMappedClass())) return new ArrayList<>(Arrays.asList(mapper));
			List<ObjectMapper<?, ?>> path = calculateCompatiblePath(section, mapper.getMappedClass(), classes);
			if(path != null && pth == null /* || (pth != null && path.size() < pth.size())*/) {
				path.add(0, mapper);
				pth = path;
				break; // Only get the path with the highest priority, not the smallest one
			}
		}
		return pth;
	}
	
	public static boolean isConfigPrimitive(Class<?> typeClass) {
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
