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

	public static Object castObjectToJSONType(Object value, JSONType type) {
		// TODO: check casts
		switch(type) {
			case ARRAY:
				return value;
			case BOOLEAN:
				return value;
			case DOUBLE:
			case DECIMAL:
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

	@SuppressWarnings("unchecked")
	public static <T> T castJSONValueTo(Object value, Class<T> clazz) {
		// TODO: generic type
		if(clazz.isInstance(value)) return (T) value;

		if(clazz.equals(Float.class) || clazz.equals(float.class)) {
			return (T) (Float) ((Number) value).floatValue();
		}else if(clazz.equals(Double.class) || clazz.equals(double.class)) {
			return (T) (Double) ((Number) value).doubleValue();
		}else if(clazz.equals(Integer.class) || clazz.equals(int.class)) {
			return (T) (Integer) ((Number) value).intValue();
		}else if(clazz.equals(Long.class) || clazz.equals(long.class)) {
			return (T) (Long) ((Number) value).longValue();
		}

		throw new IllegalArgumentException("Invalid class provided");
	}

}
