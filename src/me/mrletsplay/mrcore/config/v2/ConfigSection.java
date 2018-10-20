package me.mrletsplay.mrcore.config.v2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.json.JSONArray;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.misc.ClassUtils;
import me.mrletsplay.mrcore.misc.Complex;
import me.mrletsplay.mrcore.misc.NullableOptional;

public interface ConfigSection {
	
	// Must be implemented

	/**
	 * Returns the CustomConfig this section belongs to.<br>
	 * Will return the same instance, if this instance is already a CustomConfig
	 * @return The CustomConfig this section belongs to
	 */
	public CustomConfig getConfig();
	
	/**
	 * Returns a map containing all the properties & subsections of this section.<br>
	 * The values of this map may contain all {@link ConfigValueType valid value types}, including other ConfigSection instances.<br>
	 * Implementations may return an unmodifiable map
	 * @return A map containing all the properties & subsections of this section
	 */
	public Map<String, ConfigProperty> getAllProperties();
	
	/**
	 * Returns a map containing all comments of this section.<br>
	 * The header comment is represented by the {@code null} key.<br>
	 * Implementations may return an unmodifiable map
	 * @return A map containing all comments of this section
	 */
	public Map<String, String> getComments();
	
	/**
	 * Returns an exisiting subsection of this section or creates it, if it doesn't exist.<br>
	 * The {@code name} parameter does not allow subpaths (e.g. "{@code somepath.somesubpath}")
	 * @param name The name of the subsection
	 * @return The subsection by that name
	 * @throws ConfigException If the value specified by that key represents a property, not a subsection
	 */
	public ConfigSection getOrCreateSubsection(String name) throws ConfigException;
	
	/**
	 * Sets a key in this section to a value (represented by a {@link ConfigValueType valid value type}).<br>
	 * The {@code key} parameter allows for subpaths (e.g. "{@code somepath.somesubpath}")
	 * @param key The key of the property
	 * @param value The value of the property
	 */
	public void set(String key, Object value);
	
	/**
	 * Gets a property by a specific key.<br>
	 * The {@code key} parameter allows for subpaths (e.g. "{@code somepath.somesubpath}")
	 * @param key The key of the property
	 * @return The property by that name, {@code null} if none
	 * @throws ConfigException If the value specified by that key represents a subsection, not a property
	 */
	public ConfigProperty getProperty(String key) throws ConfigException;
	
	/**
	 * Sets a comment in this section.<br>
	 * The {@code key} parameter allows for subpaths (e.g. "{@code somepath.somesubpath}")
	 * @param key The key of the property
	 * @param value The value of the property
	 */
	public void setComment(String key, String value);
	
	/**
	 * Gets a comment by a specific key.<br>
	 * The {@code key} parameter allows for subpaths (e.g. "{@code somepath.somesubpath}")
	 * @param key The key of the comment
	 * @return The comment by that key, {@code null} if none
	 */
	public String getComment(String key);
	
	// Default methods
	
	/**
	 * Gets a subsection by the specified name.<br>
	 * This implementation uses {@link #getAllProperties()} to retrieve the specified section.<br>
	 * To create a subsection, use {@link #getOrCreateSubsection(String)}
	 * @param name The name of the subsection
	 * @return The subsection by that name, null if none
	 */
	public default ConfigSection getSubsection(String name) throws ConfigException {
		ConfigProperty p = getAllProperties().get(name);
		return p != null && p.isSubsection() ? (ConfigSection) p.getValue() : null;
	}
	
	
	/**
	 * Returns a map containing all the properties (excluding subsections) of this section.<br>
	 * This implementation uses {@link #getAllProperties()} to retrieve the properties.<br>
	 * Implementations may return an unmodifiable map
	 * @return A map containing all the properties (excluding subsections) of this section 
	 */
	public default Map<String, ConfigProperty> getProperties() {
		return getAllProperties().entrySet().stream()
				.filter(en -> en.getValue() instanceof ConfigProperty)
				.collect(Collectors.toMap(en -> en.getKey(), en -> (ConfigProperty) en.getValue()));
	}
	
	/**
	 * Returns a map containing all the raw properties (excluding subsections) of this section.<br>
	 * Raw properties are - different to {@link ConfigProperty} properties returned by {@link #getProperties()} - the raw values of this section, represented by {@link ConfigValueType valid value types}.<br>
	 * This implementation uses {@link #getProperties()} to retrieve the properties.<br>
	 * Implementations may return an unmodifiable map
	 * @return A map containing all the properties (excluding subsections) of this section 
	 */
	public default Map<String, Object> getRawProperties() {
		return getProperties().entrySet().stream()
				.collect(Collectors.toMap(en -> en.getKey(), en -> en.getValue().getValue()));
	}
	
	/**
	 * Returns a map containing all the subsections of this section.<br>
	 * This implementation uses {@link #getAllProperties()} to retrieve the subsections.<br>
	 * Implementations may return an unmodifiable map
	 * @return A map containing all the properties (excluding subsections) of this section 
	 */
	public default Map<String, ConfigSection> getSubsections() {
		return getAllProperties().entrySet().stream()
				.filter(en -> en.getValue() instanceof ConfigSection)
				.collect(Collectors.toMap(en -> en.getKey(), en -> (ConfigSection) en.getValue()));
	}
	
	/**
	 * Returns a map containing all the (raw) properties as well as subsections (represented by other {@link Map}s) of this section.<br>
	 * The Map returned by this function may be passed to {@link #loadFromMap(Map)}
	 * @return A map containing all the (raw) properties as well as subsections of this section
	 */
	public default Map<String, Object> toMap() {
		Map<String, Object> map = new LinkedHashMap<>(getRawProperties());
		for(Entry<String, ConfigSection> sub : getSubsections().entrySet()) {
			map.put(sub.getKey(), sub.getValue().toMap());
		}
		return map;
	}
	
	/**
	 * Sets all the values & subsections (represented by other {@link Map}s) of the specified map in this subsection.<br>
	 * Any map created by {@link #toMap()} may be passed to this function
	 * @param map A map containing all the values & subsections
	 */
	public default void loadFromMap(Map<String, Object> map) {
		map.forEach(this::set);
	}
	
	/**
	 * Returns a JSONObject containing all the (raw) properties as well as subsections (represented by other {@link JSONObject}s) of this section.<br>
	 * Lists will be converted to {@link JSONArray}s.<br>
	 * The JSONObject returned by this function may be passed to {@link #loadFromJSON(JSONObject)}
	 * @return A JSONObject containing all the (raw) properties as well as subsections of this section
	 */
	public default JSONObject toJSON() {
		Map<String, Object> props = getProperties().entrySet().stream()
				.collect(Collectors.toMap(en -> en.getKey(), en -> en.getValue().getJSONValue()));
		JSONObject o = new JSONObject(props);
		for(Entry<String, ConfigSection> sub : getSubsections().entrySet()) {
			o.put(sub.getKey(), sub.getValue().toJSON());
		}
		return o;
	}
	
	/**
	 * Sets all the values & subsections (represented by other {@link JSONObject}s) of the specified JSONObject in this subsection.<br>
	 * Any JSONObject created by {@link #toJSON()} may be passed to this function
	 * @param map A JSONObject containing all the values & subsections
	 */
	public default void loadFromJSON(JSONObject json) {
		json.forEach(this::set);
	}
	
	public default <T> T getGeneric(Class<T> clazz, String key, T defaultValue, boolean applyDefault) {
		return getComplex(Complex.value(clazz), key, defaultValue, applyDefault);
	}

	public default <T> List<T> getGenericList(Class<T> clazz, String key, List<T> defaultValue, boolean applyDefault) {
		return getComplex(Complex.list(clazz), key, defaultValue, applyDefault);
	}

	public default <T> Map<String, T> getGenericMap(Class<T> valueClass, String key, Map<String, T> defaultValue, boolean applyDefault) {
		return getComplex(Complex.map(String.class, valueClass), key, defaultValue, applyDefault);
	}
	
	public static <T> NullableOptional<T> defaultCast(Object o, Class<T> typeClass) {
		if(ClassUtils.isPrimitiveTypeClass(typeClass)) throw new IllegalArgumentException("Primitive types are not allowed");
		if(o == null) return NullableOptional.of(null);
		if(Number.class.isAssignableFrom(typeClass)) {
			if(!(o instanceof Number)) return NullableOptional.empty();
			Number n = (Number) o;
			if(typeClass.equals(Byte.class)) {
				return NullableOptional.of(typeClass.cast(n.byteValue()));
			}else if(typeClass.equals(Short.class)) {
				return NullableOptional.of(typeClass.cast(n.shortValue()));
			}else if(typeClass.equals(Integer.class)) {
				return NullableOptional.of(typeClass.cast(n.intValue()));
			}else if(typeClass.equals(Long.class)) {
				return NullableOptional.of(typeClass.cast(n.longValue()));
			}else if(typeClass.equals(Float.class)) {
				return NullableOptional.of(typeClass.cast(n.floatValue()));
			}else if(typeClass.equals(Double.class)) {
				return NullableOptional.of(typeClass.cast(n.doubleValue()));
			}else {
				return NullableOptional.empty();
			}
		}else if(typeClass.equals(Map.class)) {
			if(!(o instanceof ConfigSection)) return NullableOptional.empty();
			return NullableOptional.of(typeClass.cast(((ConfigSection) o).toMap()));
		}else if(typeClass.equals(JSONObject.class)) {
			if(!(o instanceof ConfigSection)) return NullableOptional.empty();
			return NullableOptional.of(typeClass.cast(new JSONObject(((ConfigSection) o).toMap())));
		}else if(typeClass.equals(String.class)
				|| typeClass.equals(Boolean.class)
				|| typeClass.equals(Character.class)
				|| typeClass.equals(List.class) || typeClass.equals(ConfigSection.class)) {
			if(!typeClass.isInstance(o)) return NullableOptional.empty();
			return NullableOptional.of(typeClass.cast(o));
		}else {
			return NullableOptional.empty();
		}
	}
	
	public default <T> T getComplex(Complex<T> complex, String key, T defaultValue, boolean applyDefault) {
		ConfigProperty prop = getProperty(key);
		if(prop.isUndefined()) {
			if(applyDefault) set(key, defaultValue);
			return defaultValue;
		}else {
			NullableOptional<T> value = complex.cast(prop.getValue(), ConfigSection::defaultCast);
			if(!value.isPresent()) throw new IncompatibleTypeException("Incompatible types, " + prop.getValue().getClass().getName() + " is not a compatible with " + complex.getFriendlyClassName());
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
