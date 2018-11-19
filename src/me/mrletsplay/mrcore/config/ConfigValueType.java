package me.mrletsplay.mrcore.config;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.config.mapper.ObjectMapper;
import me.mrletsplay.mrcore.config.mapper.ObjectMappingException;
import me.mrletsplay.mrcore.misc.Complex;
import me.mrletsplay.mrcore.misc.ErroringNullableOptional;
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
		List<ObjectMapper<?, ?>> tlms = forSection.getConfig().getMappers().entrySet().stream()
				.filter(en -> en.getKey().canMap(o, forSection::castType))
				.sorted(Comparator.comparingInt(Map.Entry::getValue))
				.map(Map.Entry::getKey)
				.collect(Collectors.toList());
		for(ObjectMapper<?, ?> om : tlms) {
			try {
				Object c = om.mapRawObject(forSection, o, forSection::castType);
				if(isConfigPrimitive(om.getMappedClass())) return ErroringNullableOptional.ofErroring(c); // ct -> tlm -> cc
				NullableOptional<?> tto = mapLowLevelType(forSection, c); // First try ct -> tlm -> llm -> ct
				if(tto.isPresent()) return tto;
			}catch(ObjectMappingException e) {
				e.printStackTrace();
				continue;
			}
		}
		return mapLowLevelType(forSection, o); // Then just return cc -> llm -> ct
	}
	
	public static NullableOptional<?> mapLowLevelType(ConfigSection section, Object o) {
		List<ObjectMapper<?, ?>> llms = section.getConfig().getLowLevelMappers().entrySet().stream()
				.filter(en -> en.getKey().canMap(o, section::castType))
				.sorted(Comparator.comparingInt(Map.Entry::getValue))
				.map(Map.Entry::getKey)
				.collect(Collectors.toList());
		for(ObjectMapper<?, ?> tom : llms) {
			try {
				Object c2 = tom.mapRawObject(section, o, section::castType);
				if(isConfigPrimitive(tom.getMappedClass())) return NullableOptional.of(c2);
			}catch(ObjectMappingException e) {
				e.printStackTrace();
				continue;
			}
		}
		return NullableOptional.empty();
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
