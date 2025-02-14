package me.mrletsplay.mrcore.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Implementation of a JSON array according to <a href="http://www.json.org/">The JSON Data Interchange Standard</a><br>
 * This also extends the ArrayList class to provide iterator/stream support
 * @author MrLetsplay2003
 */
public class JSONArray implements Iterable<Object> {

	private List<Object> values = new ArrayList<>();

	/**
	 * Creates an empty JSON array
	 */
	public JSONArray() {

	}

	/**
	 * Creates a JSON array which has identical elements to the JSONArray parameter<br>
	 * Giving null as a parameter will result in an empty JSONArray, no exception will be thrown
	 * @param fromArray The JSONArray to copy
	 */
	public JSONArray(JSONArray fromArray) {
		this.values = new ArrayList<>(fromArray != null ? fromArray.values : new ArrayList<>());
	}

	/**
	 * Creates a JSON array from the given collection
	 * @param collection The collection to create from
	 */
	public JSONArray(Collection<?> collection) {
		this();

		if(collection == null) return;

		for(Object value : collection) {
			if(value instanceof Collection<?>) value = new JSONArray((Collection<?>) value);
			if(value instanceof Map<?, ?>) value = new JSONObject((Map<?, ?>) value);
			if(JSONType.typeOf(value) == null) throw new JSONException("Illegal value type " + value.getClass().getName());
			values.add(value);
		}
	}

	/**
	 * Creates a JSON array from the given source string<br>
	 * The string has to be a valid JSON array
	 * @param source The source string to read from
	 * @throws JSONParseException If a parsing error occurs or the string doesn't represent a JSON array
	 */
	public JSONArray(String source) {
		if(source == null) {
			this.values = new ArrayList<>();
			return;
		}

		JSONArray parsed = (JSONArray) JSONParser.parse(source);
		this.values = parsed.values;
	}

	/**
	 * Adds an element to this JSON array
	 * @param value The value to add
	 * @throws JSONException If the value is not a valid JSON type
	 */
	public void add(Object value) {
		if(JSONType.typeOf(value) == null) throw new JSONException("Invalid value type");
		this.values.add(value);
	}

	/**
	 * Removes an element from this JSON array
	 * @param index The index of the element to remove
	 * @throws IndexOutOfBoundsException If the {@code index} is not in the range 0 to {@link #size()} - 1
	 */
	public void remove(int index) {
		this.values.remove(index);
	}

	/**
	 * Checks whether a value exists within this JSON array
	 * @param index The index of the value
	 * @return true if the a value exists at the given index, false otherwise
	 */
	public boolean has(int index) {
		return index >= 0 && index < values.size();
	}

	/**
	 * Gets an element from this JSON array
	 * @param index The index of the element
	 * @return The value of the property
	 * @throws JSONException If the given index is out of range
	 */
	public Object get(int index) {
		if(!has(index)) throw new JSONException("Index out of range");
		return values.get(index);
	}

	@SuppressWarnings("unchecked")
	private <T> T get(int index, JSONType type) {
		Object value = get(index);
		if(!JSONType.isOfType(value, type)) throw new JSONException(String.format("Value of type %s cannot be converted to expected type %s", value.getClass(), type));
		return (T) value;
	}

	/**
	 * @param index The index to get
	 * @return The value at that index
	 * @throws JSONException If the value doesn't exist or the type cannot be converted to a string
	 */
	public String getString(int index) {
		return get(index, JSONType.STRING);
	}

	/**
	 * @param index The index to get
	 * @return The value at that index
	 * @throws JSONException If the value doesn't exist or the type cannot be converted to a boolean
	 */
	public Boolean getBoolean(int index) {
		return get(index, JSONType.BOOLEAN);
	}

	/**
	 * @param index The index to get
	 * @return The value at that index
	 * @throws JSONException If the value doesn't exist or the type cannot be converted to a number
	 */
	public Number getNumber(int index) {
		return get(index, JSONType.NUMBER);
	}

	/**
	 * Note: This method will not check for integer over-/underflows, use {@link #getLong(int)} to manually check for that
	 * @param index The index to get
	 * @return The value at that index
	 * @throws JSONException If the value doesn't exist or the type cannot be converted to an integer
	 */
	public Integer getInt(int index) {
		Number n = get(index, JSONType.INTEGER);
		return n == null ? null : n.intValue();
	}

	/**
	 * @param index The index to get
	 * @return The value at that index
	 * @throws JSONException If the value doesn't exist or the type cannot be converted to a long
	 */
	public Long getLong(int index) {
		Number n = get(index, JSONType.INTEGER);
		return n == null ? null : n.longValue();
	}

	/**
	 * Note: This method may result in precision loss. Use {@link #getDouble(int)} for maximum precision
	 * @param index The index to get
	 * @return The value at that index
	 * @throws JSONException If the value doesn't exist or the type cannot be converted to a float
	 */
	public Float getFloat(int index) {
		Number n = get(index, JSONType.DECIMAL);
		return n == null ? null : n.floatValue();
	}

	/**
	 * @param index The index to get
	 * @return The value at that index
	 * @throws JSONException If the value doesn't exist or the type cannot be converted to a double
	 */
	public Double getDouble(int index) {
		Number n = get(index, JSONType.DECIMAL);
		return n == null ? null : n.doubleValue();
	}

	/**
	 * @param index The index to get
	 * @return The value at that index
	 * @throws JSONException If the value doesn't exist or the type cannot be converted to a JSON object
	 */
	public JSONObject getJSONObject(int index) {
		return get(index, JSONType.OBJECT);
	}

	/**
	 * @param index The index to get
	 * @return The value at that index
	 * @throws JSONException If the value doesn't exist or the type cannot be converted to a JSON array
	 */
	public JSONArray getJSONArray(int index) {
		return get(index, JSONType.ARRAY);
	}

	/**
	 * @param index The index to check
	 * @return The type of the value at that index
	 * @throws JSONException If the value doesn't exist
	 */
	public JSONType typeOf(int index) {
		Object value = get(index);
		return JSONType.typeOf(value);
	}

	/**
	 * Checks whether a value is of the given type
	 * @param index The index of the value
	 * @param type The type to check for
	 * @return true if the value exists and is of the given type, false otherwise
	 */
	public boolean isOfType(int index, JSONType type) {
		if(!has(index)) return false;
		return JSONType.isOfType(get(index), type);
	}

	/**
	 * @return A deep copy of this JSONArray
	 */
	public JSONArray copy() {
		JSONArray copy = new JSONArray();
		for(Object o : values) {
			if(o instanceof JSONArray) o = ((JSONArray) o).copy();
			if(o instanceof JSONObject) o = ((JSONObject) o).copy();
			copy.values.add(o);
		}
		return copy;
	}

	public List<Object> toList() {
		List<Object> list = new ArrayList<>();
		for(Object value : values) {
			if(value instanceof JSONObject) value = ((JSONObject) value).toMap();
			if(value instanceof JSONArray) value = ((JSONArray) value).toList();
			list.add(value);
		}
		return list;
	}

	public boolean isEmpty() {
		return values.isEmpty();
	}

	public int size() {
		return values.size();
	}

	@Override
	public Iterator<Object> iterator() {
		return values.iterator();
	}

	public Stream<Object> stream() {
		return values.stream();
	}

	/**
	 * Returns the underlying list of this JSON array.<br>
	 * Modifications to the list will be reflected in this JSON array and vice-versa.<br>
	 * There are no type safety checks when modifying the underlying list!
	 * @return The underlying list
	 */
	public List<Object> getRaw() {
		return values;
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

	@Override
	public int hashCode() {
		return Objects.hash(values);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JSONArray other = (JSONArray) obj;
		return Objects.equals(values, other.values);
	}

}
