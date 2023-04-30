package me.mrletsplay.mrcore.json;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import me.mrletsplay.mrcore.misc.NullableOptional;

/**
 * Implementation of a JSON object according to <a href="http://www.json.org/">The JSON Data Interchange Standard</a>
 * @author MrLetsplay2003
 */
public class JSONObject {

	private Map<String, Object> values;

	/**
	 * Creates an empty JSONObject
	 */
	public JSONObject() {
		this.values = new LinkedHashMap<>();
	}

	/**
	 * Creates a JSON object from the given source string<br>
	 * The string has to be a valid JSON object
	 * @param source The source string to read from
	 * @throws JSONParseException If a parsing error occurs or the string doesn't represent a JSON object
	 */
	public JSONObject(String source) {
		if(source == null) {
			this.values = new LinkedHashMap<>();
			return;
		}

		JSONObject parsed = (JSONObject) JSONParser.parse(source, JSONType.OBJECT);
		this.values = parsed.values;
	}

	/**
	 * Creates a JSON object which has identical properties to the JSONObject parameter<br>
	 * Giving null as a parameter will result in an empty JSONObject, no exception will be thrown
	 * @param fromObject The JSONObject to copy
	 */
	public JSONObject(JSONObject fromObject) {
//		this.values = new LinkedHashMap<>(fromObject != null ? fromObject : new LinkedHashMap<>());
	}

	/**
	 * Creates a JSON object which has identical properties to the Map parameter<br>
	 * Giving null as a parameter will result in an empty JSONObject, no exception will be thrown
	 * @param fromMap  The Map to copy
	 */
	public JSONObject(Map<?, ?> fromMap) {
		this();

		if(fromMap == null) return;

		fromMap.forEach((Object key, Object value) -> {
			if(!(key instanceof String)) throw new JSONException("Key is not a string");
			if(value instanceof Collection<?>) value = new JSONArray((Collection<?>) value);
			if(value instanceof Map<?, ?>) value = new JSONObject((Map<?, ?>) value);
			if(JSONType.typeOf(value) == null) throw new JSONException("Illegal value type " + value.getClass().getName());
			values.put((String) key, value);
		});
	}

	/**
	 * Sets the given key to a value<br>
	 * Already set properties will be overridden<br>
	 * This method only allows for Strings, Integers, Longs, Floats, Doubles, other JSON objects or JSON arrays to be set<br>
	 * @param key The key of the property
	 * @param value The value to set it to
	 * @throws JSONException If the value is not a valid JSON type
	 */
	public void set(String key, Object value) {
		if(JSONType.typeOf(value) == null) throw new JSONException("Invalid value type");
		values.put(key, value);
	}

	/**
	 * Functionally equivalent to {@link #set(String, Object)}
	 * @param key
	 * @param value
	 * @see #set(String, Object)
	 */
	public void put(String key, Object value) {
		set(key, value);
	}

	/**
	 * Checks whether a specific property exists within this JSON object
	 * @param key The key of the property
	 * @return true if the given property exists, false otherwise
	 */
	public boolean has(String key) {
		return values.containsKey(key);
	}

	/**
	 * Gets a property from this JSON object<br>
	 * If the given key can't be found, a {@link JSONException} is thrown<br>
	 * @param key The key to get
	 * @return The value of the property
	 * @throws JSONException If the given key is not found
	 */
	public Object get(String key) {
		if(!has(key)) throw new JSONException("Object doesn't have the property \"" + key + "\"");
		return values.get(key);
	}

	@SuppressWarnings("unchecked")
	private <T> T get(String key, JSONType type) {
		Object value = get(key);
		if(!JSONType.isOfType(value, type)) throw new JSONException("Value cannot be converted to expected type");
		return (T) value;
	}

	/**
	 * @param key The key to get
	 * @return The value of the property
	 * @throws JSONException If the type cannot be converted to a string
	 */
	public String getString(String key) {
		return get(key, JSONType.STRING);
	}

	/**
	 * @param key The key to get
	 * @return The value of the property
	 * @throws JSONException If the key doesn't exist or the type cannot be converted to a boolean
	 */
	public Boolean getBoolean(String key) {
		return get(key, JSONType.BOOLEAN);
	}

	/**
	 * @param key The key to get
	 * @return The value of the property
	 * @throws JSONException If the key doesn't exist or the type cannot be converted to a number
	 */
	public Number getNumber(String key) {
		return get(key, JSONType.NUMBER);
	}

	/**
	 * @param key The key to get
	 * @return The value of the property
	 * @throws JSONException If the key doesn't exist or the type cannot be converted to an integer
	 * @apiNote This method will not check for integer over-/underflows, use {@link #getLong(String)} to manually check for that
	 */
	public int getInt(String key) {
		return this.<Number>get(key, JSONType.INTEGER).intValue();
	}

	/**
	 * @param key The key to get
	 * @return The value of the property
	 * @throws JSONException If the key doesn't exist or the type cannot be converted to a long
	 */
	public long getLong(String key) {
		return this.<Number>get(key, JSONType.INTEGER).longValue();
	}

	/**
	 * @param key The key to get
	 * @return The value of the property
	 * @throws JSONException If the key doesn't exist or the type cannot be converted to a double
	 */
	public double getDouble(String key) {
		return this.<Number>get(key, JSONType.DECIMAL).doubleValue();
	}

	/**
	 * @param key The key to get
	 * @return The value of the property
	 * @throws JSONException If the key doesn't exist or the type cannot be converted to a JSON object
	 */
	public JSONObject getJSONObject(String key) {
		return get(key, JSONType.OBJECT);
	}

	/**
	 * @param key The key to get
	 * @return The value of the property
	 * @throws JSONException If the key doesn't exist or the type cannot be converted to a JSON array
	 */
	public JSONArray getJSONArray(String key) {
		return get(key, JSONType.ARRAY);
	}

	/**
	 * Gets a property from this JSON object wrapped inside a {@link NullableOptional}<br>
	 * If the given key can't be found, {@link NullableOptional#empty()} is returned
	 * @param key The key to get
	 * @return The value of the property wrapped inside a {@link NullableOptional}
	 */
	public NullableOptional<Object> opt(String key) {
		if(!has(key)) return NullableOptional.empty();
		return NullableOptional.of(get(key));
	}

	private <T> NullableOptional<T> opt(String key, JSONType type) {
		if(!has(key)) return NullableOptional.empty();
		return NullableOptional.of(get(key, type));
	}

	/**
	 * @param key The key to get
	 * @return The value of the property wrapped inside a {@link NullableOptional}
	 * @throws JSONException If the type cannot be converted to a string
	 */
	public NullableOptional<String> optString(String key) {
		return opt(key, JSONType.STRING);
	}

	/**
	 * @param key The key to get
	 * @return The value of the property wrapped inside a {@link NullableOptional}
	 * @throws JSONException If the type cannot be converted to a boolean
	 */
	public NullableOptional<Boolean> optBoolean(String key) {
		return opt(key, JSONType.BOOLEAN);
	}

	/**
	 * @param key The key to get
	 * @return The value of the property wrapped inside a {@link NullableOptional}
	 * @throws JSONException If the type cannot be converted to a number
	 */
	public NullableOptional<Number> optNumber(String key) {
		return opt(key, JSONType.NUMBER);
	}

	/**
	 * @param key The key to get
	 * @return The value of the property wrapped inside a {@link NullableOptional}
	 * @throws JSONException If the type cannot be converted to an integer
	 */
	public NullableOptional<Integer> optInt(String key) {
		return this.<Number>opt(key, JSONType.INTEGER).map(n -> n.intValue());
	}

	/**
	 * @param key The key to get
	 * @return The value of the property wrapped inside a {@link NullableOptional}
	 * @throws JSONException If the type cannot be converted to a long
	 */
	public NullableOptional<Long> optLong(String key) {
		return this.<Number>opt(key, JSONType.INTEGER).map(n -> n.longValue());
	}

	/**
	 * @param key The key to get
	 * @return The value of the property wrapped inside a {@link NullableOptional}
	 * @throws JSONException If the type cannot be converted to a double
	 */
	public NullableOptional<Double> optDouble(String key) {
		return this.<Number>opt(key, JSONType.DECIMAL).map(n -> n.doubleValue());
	}

	/**
	 * @param key The key to get
	 * @return The value of the property wrapped inside a {@link NullableOptional}
	 * @throws JSONException If the type cannot be converted to a JSON object
	 */
	public NullableOptional<JSONObject> optJSONObject(String key) {
		return this.opt(key, JSONType.OBJECT);
	}

	/**
	 * @param key The key to get
	 * @return The value of the property wrapped inside a {@link NullableOptional}
	 * @throws JSONException If the type cannot be converted to a JSON array
	 */
	public NullableOptional<JSONArray> optJSONArray(String key) {
		return this.opt(key, JSONType.ARRAY);
	}

	public boolean isOfType(String key, JSONType type) {
		if(!has(key)) return false;
		Object value = get(key);
		return JSONType.isOfType(value, type);
	}

	public Map<String, Object> toMap() {
		Map<String, Object> map = new LinkedHashMap<>();
		values.forEach((key, value) -> {
			if(value instanceof JSONObject) value = ((JSONObject) value).toMap();
			if(value instanceof JSONArray) value = ((JSONArray) value).toList();
			map.put(key, value);
		});
		return map;
	}

	public boolean isEmpty() {
		return values.isEmpty();
	}

	public Set<String> keys() {
		return values.keySet();
	}

	/**
	 * Converts this JSON object into a JSON string according to <a href="http://www.json.org/">The JSON Data Interchange Standard</a>
	 * @throws JSONException If a conversion error occurs (e.g. a property has an invalid type)
	 */
	@Override
	public String toString() {
		return JSONFormatter.formatObject(this, false).toString();
	}

	/**
	 * A more human-readable form of {@link #toString()}
	 * @throws JSONException If a conversion error occurs (e.g. a property has an invalid type)
	 */
	public String toFancyString() {
		return JSONFormatter.formatObject(this, true).toString();
	}

}
