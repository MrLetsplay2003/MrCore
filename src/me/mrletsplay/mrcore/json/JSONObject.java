package me.mrletsplay.mrcore.json;

import java.util.LinkedHashMap;
import java.util.Map;

import me.mrletsplay.mrcore.misc.NullableOptional;

/**
 * Implementation of a JSON object according to <a href="http://www.json.org/">The JSON Data Interchange Standard</a>
 * @author MrLetsplay2003
 */
public class JSONObject extends LinkedHashMap<String, Object> {
	
	private static final long serialVersionUID = -7624968898431467371L;
	
	/**
	 * Creates an empty JSONObject
	 */
	public JSONObject() {
		super();
	}
	
	/**
	 * Creates a JSON object from the given source string<br>
	 * The string has to be a valid JSON object
	 * @param source The source string to read from
	 * @throws JSONParseException If a parsing error occurs
	 * @throws ClassCastException If the given string does not represent a JSON object
	 */
	public JSONObject(String source) {
		this((JSONObject) JSONParser.parse(source));
	}
	
	/**
	 * Creates a JSON object which has identical properties to the JSONObject parameter<br>
	 * Giving null as a parameter will result in an empty JSONObject, no exception will be thrown
	 * @param fromObject The JSONObject to copy
	 */
	public JSONObject(JSONObject fromObject) {
		super(fromObject != null ? fromObject : new LinkedHashMap<>());
	}
	
	/**
	 * Creates a JSON object which has identical properties to the Map parameter<br>
	 * Giving null as a parameter will result in an empty JSONObject, no exception will be thrown
	 * @param fromMap  The Map to copy
	 */
	public JSONObject(Map<String, ?> fromMap) {
		super(fromMap != null ? fromMap : new LinkedHashMap<>());
	}
	
	/**
	 * Sets the given key to a value<br>
	 * Already set properties will be overridden<br>
	 * This method only allows for Strings, Numbers, other JSON objects or JSON arrays to be set<br>
	 * If another type of value is set, the implementation will throw an error when calling {@link #toString()}
	 * @param key The key of the property
	 * @param value The value to set it to
	 */
	public void set(String key, Object value) {
		put(key, value);
	}
	
	/**
	 * Checks whether a specific property exists within this JSON object
	 * @param key The key of the property
	 * @return true if the given property exists, false otherwise
	 */
	public boolean has(String key) {
		return containsKey(key);
	}
	
	/**
	 * Gets a property from this JSON object<br>
	 * If the given key can't be found, a {@link JSONException} is thrown<br>
	 * <br>
	 * The get[type] methods return their type respectively<br>
	 * If a type can not be converted, a {@link ClassCastException} is thrown
	 * @param key The key of the property
	 * @return The value of the property
	 * @throws JSONException If the given key is not found
	 */
	public Object get(String key) {
		if(!containsKey(key)) throw new JSONException("Object doesn't have the property \""+key+"\"");
		return super.get(key);
	}
	
	/**
	 * @see #get(String)
	 */
	public String getString(String key) {
		return (String) get(key);
	}

	/**
	 * @see #get(String)
	 */
	public Boolean getBoolean(String key) {
		return (Boolean) get(key);
	}

	/**
	 * @see #get(String)
	 */
	public Number getNumber(String key) {
		return (Number) get(key);
	}

	/**
	 * @see #get(String)
	 */
	public int getInt(String key) {
		return getNumber(key).intValue();
	}

	/**
	 * @see #get(String)
	 */
	public long getLong(String key) {
		return getNumber(key).longValue();
	}

	/**
	 * @see #get(String)
	 */
	public double getDouble(String key) {
		return getNumber(key).doubleValue();
	}

	/**
	 * @see #get(String)
	 */
	public JSONObject getJSONObject(String key) {
		return (JSONObject) get(key);
	}

	/**
	 * @see #get(String)
	 */
	public JSONArray getJSONArray(String key) {
		return (JSONArray) get(key);
	}
	
	/**
	 * Gets a property from this JSON object wrapped inside a {@link NullableOptional}<br>
	 * If the given key can't be found, {@link NullableOptional#empty()} is returned<br>
	 * <br>
	 * The opt[type] methods return their type respectively<br>
	 * If a type can not be converted, a {@link ClassCastException} is thrown
	 * @param key The key of the property
	 * @return The value of the property wrapped inside a {@link NullableOptional}
	 */
	public NullableOptional<Object> opt(String key) {
		if(!containsKey(key)) return NullableOptional.empty();
		return NullableOptional.of(get(key));
	}
	
	/**
	 * @see #opt(String)
	 */
	public NullableOptional<String> optString(String key) {
		return opt(key).map(e -> (String) e);
	}

	/**
	 * @see #opt(String)
	 */
	public NullableOptional<Boolean> optBoolean(String key) {
		return opt(key).map(e -> (Boolean) e);
	}

	/**
	 * @see #opt(String)
	 */
	public NullableOptional<Number> optNumber(String key) {
		return opt(key).map(e -> (Number) e);
	}

	/**
	 * @see #opt(String)
	 */
	public NullableOptional<Integer> optInt(String key) {
		return optNumber(key).map(n -> n.intValue());
	}

	/**
	 * @see #opt(String)
	 */
	public NullableOptional<Long> optLong(String key) {
		return optNumber(key).map(n -> n.longValue());
	}

	/**
	 * @see #opt(String)
	 */
	public NullableOptional<Double> optDouble(String key) {
		return optNumber(key).map(n -> n.doubleValue());
	}

	/**
	 * @see #opt(String)
	 */
	public NullableOptional<JSONObject> optJSONObject(String key) {
		return opt(key).map(e -> (JSONObject) e);
	}

	/**
	 * @see #opt(String)
	 */
	public NullableOptional<JSONArray> optJSONArray(String key) {
		return opt(key).map(e -> (JSONArray) e);
	}

	public boolean isOfType(String key, JSONType type) {
		switch (type) {
			case OBJECT:
				return has(key) && get(key) instanceof JSONObject;
			case ARRAY:
				return has(key) && get(key) instanceof JSONArray;
			case STRING:
				return has(key) && get(key) instanceof String;
			case BOOLEAN:
				return has(key) && get(key) instanceof Boolean;
			case DOUBLE:
			case INTEGER:
			case LONG:
			case NUMBER:
				return has(key) && get(key) instanceof Number;
			case NULL:
				return has(key) && get(key) == null;
		}
		return false;
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
