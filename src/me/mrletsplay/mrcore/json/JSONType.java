package me.mrletsplay.mrcore.json;

public enum JSONType {

	/**
	 * Java type: {@code null}
	 */
	NULL,
	
	/**
	 * Java type: {@link Number}
	 */
	NUMBER,
	
	/**
	 * Java type: {@link Integer}
	 */
	INTEGER,
	
	/**
	 * Java type: {@link Double}
	 */
	DOUBLE,
	
	/**
	 * Java type: {@link Long}
	 */
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
		if(value instanceof Double) return JSONType.DOUBLE;
		if(value instanceof Integer) return JSONType.INTEGER;
		if(value instanceof Long) return JSONType.LONG;
		if(value instanceof Number) return JSONType.NUMBER;
		return null;
	}
	
	public static Object castObjectToJSONType(Object value, JSONType type) {
		switch(type) {
			case ARRAY:
				return (JSONArray) value;
			case BOOLEAN:
				return (Boolean) value;
			case DOUBLE:
				return ((Number) value).doubleValue();
			case INTEGER:
				return ((Number) value).intValue();
			case LONG:
				return ((Number) value).longValue();
			case NULL:
				return null;
			case NUMBER:
				return (Number) value;
			case OBJECT:
				return (JSONObject) value;
			case STRING:
				return (String) value;
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
			return (JSONArray) value;
		}else if(clazz.equals(Boolean.class) || clazz.equals(boolean.class)) {
			return (Boolean) value;
		}else if(clazz.equals(Number.class)) {
			return (Number) value;
		}else if(clazz.equals(Double.class) || clazz.equals(double.class)) {
			return ((Number) value).doubleValue();
		}else if(clazz.equals(Integer.class) || clazz.equals(int.class)) {
			return ((Number) value).intValue();
		}else if(clazz.equals(Long.class) || clazz.equals(long.class)) {
			return ((Number) value).longValue();
		}else if(clazz.equals(JSONObject.class)) {
			return (JSONObject) value;
		}else if(clazz.equals(String.class)) {
			return strict ? (String) value : value.toString();
		}
		throw new IllegalArgumentException("Invalid class provided");
	}
	
}
