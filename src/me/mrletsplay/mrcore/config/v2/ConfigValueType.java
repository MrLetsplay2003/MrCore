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
	 * Java Type: {@code boolean}
	 */
	BOOLEAN(Boolean.class),
	
	/**
	 * Java Type: {@code Number} -> {@code byte}, {@code short}, {@code int}, {@code long}
	 */
	NUMBER(Number.class),
	
	/**
	 * Java Type: {@code Number} -> {@code float}, {@code double}
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
	
	
}
