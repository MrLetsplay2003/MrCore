package me.mrletsplay.mrcore.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
		super();
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
	 * @param o The value to add
	 */
	public void add(Object o) {
		this.values.add(o);
	}

	/**
	 * Gets an element from this JSON array
	 * @param index The index of the element
	 * @return The value of the property
	 * @throws JSONException If the given index is out of range
	 */
	public Object get(int index) {
		if(index < 0 || index > values.size()) throw new JSONException("Index out of range");
		return values.get(index);
	}

	@SuppressWarnings("unchecked")
	private <T> T get(int index, JSONType type) {
		Object value = get(index);
		if(!JSONType.isOfType(value, type)) throw new JSONException("Value cannot be converted to expected type");
		return (T) value;
	}

	/**
	 * @param index The index to get
	 * See {@link #get(int)}
	 * @throws JSONException If
	 */
	public String getString(int index) {
		return get(index, JSONType.STRING);
	}

	/**
	 * See {@link #get(int)}
	 */
	public Boolean getBoolean(int index) {
		return get(index, JSONType.BOOLEAN);
	}

	/**
	 * See {@link #get(int)}
	 */
	public Number getNumber(int index) {
		return get(index, JSONType.NUMBER);
	}

	/**
	 * See {@link #get(int)}
	 */
	public int getInt(int index) {
		return this.<Number>get(index, JSONType.INTEGER).intValue();
	}

	/**
	 * See {@link #get(int)}
	 */
	public long getLong(int index) {
		return this.<Number>get(index, JSONType.INTEGER).longValue();
	}

	/**
	 * See {@link #get(int)}
	 */
	public double getDouble(int index) {
		return this.<Number>get(index, JSONType.DECIMAL).doubleValue();
	}

	/**
	 * See {@link #get(int)}
	 */
	public JSONObject getJSONObject(int index) {
		return get(index, JSONType.OBJECT);
	}

	/**
	 * See {@link #get(int)}
	 */
	public JSONArray getJSONArray(int index) {
		return get(index, JSONType.ARRAY);
	}

	public boolean isOfType(int index, JSONType type) {
		Object value = get(index);
		return JSONType.isOfType(value, type);
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
