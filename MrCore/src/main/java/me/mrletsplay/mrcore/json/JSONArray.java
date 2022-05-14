package me.mrletsplay.mrcore.json;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Implementation of a JSON array according to <a href="http://www.json.org/">The JSON Data Interchange Standard</a><br>
 * This also extends the ArrayList class to provide iterator/stream support
 * @author MrLetsplay2003
 */
public class JSONArray extends ArrayList<Object> {

	private static final long serialVersionUID = 7419047721431768026L;

	/**
	 * Creates an empty JSON array
	 */
	public JSONArray() {
		super();
	}

	/**
	 * Creates a JSON array from the given collection
	 * @param collection The collection to create from
	 */
	public JSONArray(Collection<?> collection) {
		super(collection);
	}

	/**
	 * Creates a JSON array from the given source string<br>
	 * The string has to be a valid JSON array
	 * @param source The source string to read from
	 * @throws JSONParseException If a parsing error occurs
	 * @throws ClassCastException If the given string does not represent a JSON array
	 */
	public JSONArray(String source) {
		super(source != null ? (JSONArray) JSONParser.parse(source) : new ArrayList<>());
	}

	/**
	 * Gets an element from this JSON array<br>
	 * If the given index can't be found, an {@link IndexOutOfBoundsException} is thrown as defined by {@link ArrayList#get(int)}<br>
	 * <br>
	 * The get[type] methods return their type respectively<br>
	 * If a type can not be converted, a {@link ClassCastException} is thrown
	 * @param index The index of the element
	 * @return The value of the property
	 * @throws IndexOutOfBoundsException as defined by {@link ArrayList#get(int)}
	 */
	@Override
	public Object get(int index) {
		return super.get(index);
	}
	
	/**
	 * See {@link #get(int)}
	 */
	public String getString(int index) {
		return (String) get(index);
	}

	/**
	 * See {@link #get(int)}
	 */
	public Boolean getBoolean(int index) {
		return (Boolean) get(index);
	}

	/**
	 * See {@link #get(int)}
	 */
	public Number getNumber(int index) {
		return (Number) get(index);
	}

	/**
	 * See {@link #get(int)}
	 */
	public int getInt(int index) {
		return getNumber(index).intValue();
	}

	/**
	 * See {@link #get(int)}
	 */
	public long getLong(int index) {
		return getNumber(index).longValue();
	}

	/**
	 * See {@link #get(int)}
	 */
	public double getDouble(int index) {
		return getNumber(index).doubleValue();
	}

	/**
	 * See {@link #get(int)}
	 */
	public JSONObject getJSONObject(int index) {
		return (JSONObject) get(index);
	}

	/**
	 * See {@link #get(int)}
	 */
	public JSONArray getJSONArray(int index) {
		return (JSONArray) get(index);
	}

	public boolean isOfType(int index, JSONType type) {
		switch (type) {
			case OBJECT:
				return get(index) instanceof JSONObject;
			case ARRAY:
				return get(index) instanceof JSONArray;
			case STRING:
				return get(index) instanceof String;
			case BOOLEAN:
				return get(index) instanceof Boolean;
			case DOUBLE:
			case INTEGER:
			case LONG:
			case NUMBER:
				return get(index) instanceof Number;
			case NULL:
				return get(index) == null;
		}
		return false;
	}

	/**
	 * Converts this JSON object into a JSON string according to <a href="http://www.json.org/">The JSON Data Interchange Standard</a>
	 * @throws JSONException If a conversion error occurs (e.g. a property has an invalid type)
	 */
	@Override
	public String toString() {
		return JSONFormatter.formatArray(this, 0, false).toString();
	}
	
	/**
	 * A more human-readable form of {@link #toString()}
	 * @throws JSONException If a conversion error occurs (e.g. a property has an invalid type)
	 */
	public String toFancyString() {
		return JSONFormatter.formatArray(this, 0, true).toString();
	}
	
}
