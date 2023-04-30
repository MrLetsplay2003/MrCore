package me.mrletsplay.mrcore.json;

public enum JSONType {

	/**
	 * Java type: {@code null}
	 */
	NULL,

	/**
	 * Any number, either an {@link #INTEGER} or a {@link #DECIMAL}<br>
	 * Java type: {@link Number}
	 */
	NUMBER,

	/**
	 * A number without a fractional part<br>
	 * Java type: {@link Long}
	 */
	INTEGER,

	/**
	 * Java type: {@link Double}
	 * @deprecated This is now {@link #DECIMAL}
	 */
	@Deprecated
	DOUBLE,

	/**
	 * A number with a fractional part<br>
	 * Java type: {@link Double}
	 */
	DECIMAL,

	/**
	 * Java type: {@link Long}
	 * @deprecated There is no longer a distinction between {@link #INTEGER} and {@link #LONG}
	 */
	@Deprecated
	LONG,

	/**
	 * Java type: {@link Boolean}
	 */
	BOOLEAN,

	/**
	 * Java type: {@link JSONObject}
	 */
	OBJECT,

	/**
	 * Java type: {@link JSONArray}
	 */
	ARRAY,

	/**
	 * Java type: {@link String}
	 */
	STRING,
	;

	public Object cast(Object value) {
		return castObjectToJSONType(value, this);
	}

	public static JSONType typeOf(Object value) {
		if(value == null) return JSONType.NULL;
		if(value instanceof JSONObject) return JSONType.OBJECT;
		if(value instanceof JSONArray) return JSONType.ARRAY;
		if(value instanceof String) return JSONType.STRING;
		if(value instanceof Boolean) return JSONType.BOOLEAN;
		if(value instanceof Float || value instanceof Double) return JSONType.DECIMAL;
		if(value instanceof Integer || value instanceof Long) return JSONType.INTEGER;
		return null;
	}

	public static boolean isOfType(Object value, JSONType type) {
		switch (type) {
			case OBJECT:
				return value instanceof JSONObject;
			case ARRAY:
				return value instanceof JSONArray;
			case STRING:
				return value instanceof String;
			case BOOLEAN:
				return value instanceof Boolean;
			case DECIMAL:
			case DOUBLE:
				return value instanceof Double;
			case INTEGER:
			case LONG:
				return value instanceof Long || value instanceof Integer;
			case NUMBER:
				return value instanceof Number;
			case NULL:
				return value == null;
		}

		return false;
	}

	public static Object castObjectToJSONType(Object value, JSONType type) {
		switch(type) {
			case ARRAY:
				return value;
			case BOOLEAN:
				return value;
			case DOUBLE:
				return ((Number) value).doubleValue();
			case INTEGER:
				return ((Number) value).intValue();
			case LONG:
				return ((Number) value).longValue();
			case NULL:
				return null;
			case NUMBER:
				return value;
			case OBJECT:
				return value;
			case STRING:
				return value;
			default:
				throw new IllegalStateException("Type not handled!");
		}
	}

	public static Object castJSONValueTo(Object value, Class<?> clazz) {
		return castJSONValueTo(value, clazz, true);
	}

	public static Object castJSONValueTo(Object value, Class<?> clazz, boolean strict) {
		if(!strict && clazz.equals(Object.class)) return value;
		if(clazz.equals(JSONArray.class)) {
			return value;
		}else if(clazz.equals(Boolean.class) || clazz.equals(boolean.class)) {
			return value;
		}else if(clazz.equals(Number.class)) {
			return value;
		}else if(clazz.equals(Double.class) || clazz.equals(double.class)) {
			return ((Number) value).doubleValue();
		}else if(clazz.equals(Integer.class) || clazz.equals(int.class)) {
			return ((Number) value).intValue();
		}else if(clazz.equals(Long.class) || clazz.equals(long.class)) {
			return ((Number) value).longValue();
		}else if(clazz.equals(JSONObject.class)) {
			return value;
		}else if(clazz.equals(String.class)) {
			return strict ? (String) value : value.toString();
		}
		throw new IllegalArgumentException("Invalid class provided");
	}

}
