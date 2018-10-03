package me.mrletsplay.mrcore.config.v2;

import java.util.List;

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
	
	private ConfigValueType(Class<?> valueType) {
		this.valueClass = valueType;
	}
	
	public Class<?> getValueClass() {
		return valueClass;
	}
	
	public static ConfigValueType getTypeOf(Object o) {
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
		}else if(/*o instanceof Map ||*/ o instanceof ConfigSection) {
			return ConfigValueType.SECTION;
		}else if(o instanceof List) {
			return ConfigValueType.LIST;
		}
		return null;
	}
	
}
