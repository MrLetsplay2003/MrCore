package me.mrletsplay.mrcore.config.v2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import me.mrletsplay.mrcore.misc.Complex;

public interface ConfigSection {
	
	// Must be implemented

	public CustomConfig getConfig();
	
	public ConfigSection getParent();
	
	public String getName();
	
	public Map<String, ConfigProperty> getProperties();
	
	public Map<String, String> getComments();
	
	public Map<String, ConfigSection> getSubsections();
	
	public void set(String key, Object value);
	
	public ConfigProperty getProperty(String key);
	
	public String saveToString();
	
	// Default methods

	public default <T> T getGeneric(Class<T> clazz, String key, T defaultValue, boolean applyDefault) {
		return getComplex(Complex.value(clazz), key, defaultValue, applyDefault);
	}

	public default <T> List<T> getGenericList(Class<T> clazz, String key, List<T> defaultValue, boolean applyDefault) {
		return getComplex(Complex.list(clazz), key, defaultValue, applyDefault);
	}

	public default <K, V> Map<K, V> getGenericMap(Class<K> keyClass, Class<V> valueClass, String key, Map<K, V> defaultValue, boolean applyDefault) {
		return getComplex(Complex.map(keyClass, valueClass), key, defaultValue, applyDefault);
	}
	
	public static <T> Optional<T> primitiveCast(Object o, Class<T> typeClass) {
		return null;
	}
	
	public default <T> T getComplex(Complex<T> complex, String key, T defaultValue, boolean applyDefault) {
		ConfigProperty prop = getProperty(key);
		if(prop.isUndefined()) {
			if(applyDefault) set(key, defaultValue);
			return defaultValue;
		}else {
			Optional<T> value = complex.cast(prop.getValue(), ConfigSection::primitiveCast);
			if(!value.isPresent()) throw new IncompatibleTypeException("Incompatible types, " + value.getClass() + " is not a compatible with " + complex.getFriendlyClassName());
			return value.get();
		}
	}
	
	public default String getString(String key, String defaultValue, boolean applyDefault) {
		return getGeneric(String.class, key, defaultValue, applyDefault);
	}
	
	public default boolean getBoolean(String key, boolean defaultValue, boolean applyDefault) {
		return getGeneric(Boolean.class, key, defaultValue, applyDefault);
	}
	
	public default byte getByte(String key, byte defaultValue, boolean applyDefault) {
		return getGeneric(Byte.class, key, defaultValue, applyDefault);
	}
	
	public default short getShort(String key, short defaultValue, boolean applyDefault) {
		return getGeneric(Short.class, key, defaultValue, applyDefault);
	}
	
	public default int getInt(String key, int defaultValue, boolean applyDefault) {
		return getGeneric(Integer.class, key, defaultValue, applyDefault);
	}
	
	public default long getLong(String key, long defaultValue, boolean applyDefault) {
		return getGeneric(Long.class, key, defaultValue, applyDefault);
	}
	
	public default float getFloat(String key, float defaultValue, boolean applyDefault) {
		return getGeneric(Float.class, key, defaultValue, applyDefault);
	}
	
	public default double getDouble(String key, double defaultValue, boolean applyDefault) {
		return getGeneric(Double.class, key, defaultValue, applyDefault);
	}
	
	public default List<?> getList(String key, List<?> defaultValue, boolean applyDefault) {
		return getGeneric(List.class, key, defaultValue, applyDefault);
	}
	
	public default Map<?, ?> getMap(String key, Map<?, ?> defaultValue, boolean applyDefault) {
		return getGeneric(Map.class, key, defaultValue, applyDefault);
	}
	
	public default <T> T getGeneric(Class<T> clazz, String key) {
		return getGeneric(clazz, key, null, false);
	}
	
	public default <T> T getComplex(Complex<T> complex, String key) {
		return getComplex(complex, key, null, false);
	}
	
	public default String getString(String key) {
		return getString(key, null, false);
	}
	
	public default boolean getBoolean(String key) {
		return getBoolean(key, false, false);
	}
	
	public default byte getByte(String key) {
		return getByte(key, (byte) 0, false);
	}
	
	public default short getShort(String key) {
		return getShort(key, (short) 0, false);
	}
	
	public default int getInt(String key) {
		return getInt(key, 0, false);
	}
	
	public default long getLong(String key) {
		return getLong(key, 0l, false);
	}
	
	public default float getFloat(String key) {
		return getFloat(key, 0f, false);
	}
	
	public default double getDouble(String key) {
		return getDouble(key, 0d, false);
	}
	
	public default List<?> getList(String key) {
		return getList(key, new ArrayList<>(), false);
	}
	
	public default Map<?, ?> getMap(String key) {
		return getMap(key, new HashMap<>(), false);
	}
	
}
