package me.mrletsplay.mrcore.json.converter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.json.JSONArray;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.json.JSONType;
import me.mrletsplay.mrcore.misc.FriendlyException;
import me.mrletsplay.mrcore.misc.MiscUtils;
import me.mrletsplay.mrcore.misc.NullableOptional;

public class JSONConverter {

	private JSONConverter() {}
	
	public static JSONObject encodeObject(JSONConvertible convertible) {
		return (JSONObject) encode0(convertible);
	}
	
	private static Object encode0(Object value) {
		if(value == null) return null;
		if(value instanceof List<?>) {
			List<?> l = (List<?>) value;
			JSONArray arr = new JSONArray();
			for(Object o : l) {
				arr.add(encode0(o));
			}
			return arr;
		}else if(value instanceof JSONConvertible) {
			JSONObject obj = new JSONObject();
			((JSONConvertible) value).preSerialize(obj);
			for(Field f : value.getClass().getDeclaredFields()) {
				JSONValue v = f.getAnnotation(JSONValue.class);
				if(v == null || !v.encode()) continue;
				String fName = v.value().isEmpty() ? f.getName() : v.value();
				f.setAccessible(true);
				try {
					obj.set(fName, encode0(f.get(value)));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					throw new FriendlyException(e);
				}
			}
			for(Method m : value.getClass().getDeclaredMethods()) {
				JSONValue v = m.getAnnotation(JSONValue.class);
				if(v == null || !v.encode()) continue;
				if(m.getParameterCount() != 0) throw new IllegalArgumentException("Method has parameters");
				String fName = v.value().isEmpty() ? m.getName() : v.value();
				m.setAccessible(true);
				try {
					obj.set(fName, encode0(m.invoke(value)));
				} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
					throw new FriendlyException(e);
				}
			}
			return obj;
		}else {
			JSONType t = JSONType.typeOf(value);
			if(t == null) throw new IllegalArgumentException("Object of type " + value.getClass() + " is not a valid JSON value");
			return value;
		}
	}
	
	public static <T extends JSONConvertible> T decodeObject(JSONObject object, Class<T> clazz) {
		return clazz.cast(decode0(object, clazz));
	}
	
	private static <T extends JSONConvertible> T createObject0(JSONObject object, Class<T> clazz) {
		List<Constructor<?>> constrs = Arrays.stream(clazz.getDeclaredConstructors()).filter(c -> c.isAnnotationPresent(JSONConstructor.class)).collect(Collectors.toList());
		if(constrs.isEmpty()) throw new IllegalArgumentException("No constructor available for class " + clazz);
		T t = null;
		c: for(Constructor<?> c : constrs) {
			List<Object> params = new ArrayList<>();
			for(Parameter p : c.getParameters()) {
				JSONParameter param = p.getAnnotation(JSONParameter.class);
				if(param == null) throw new IllegalArgumentException("JSON constructor parameter \"" + p + "\" isn't annotated");
				String vName = param.value();
				if(param.mustBePresent() && !object.has(vName)) continue c;
				Object pValue = object.has(vName) ? object.get(vName) : null;
				JSONListType lParam = p.getAnnotation(JSONListType.class);
				JSONComplexListType clParam = p.getAnnotation(JSONComplexListType.class);
				if(lParam != null || clParam != null) {
					params.add(decodeList0((JSONArray) pValue, lParam, clParam));
				}else {
					params.add(decode0(pValue, p.getType()));
				}
			}
			c.setAccessible(true);
			try {
				t = clazz.cast(c.newInstance(params.toArray(new Object[params.size()])));
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
				continue;
			}
		}
		if(t == null) throw new IllegalArgumentException("No suitable constructor found for class " + clazz.getName());
		return t;
	}
	
	private static Object decode0(Object value, Class<?> clazz) {
		if(value == null) return null;
		if(JSONConvertible.class.isAssignableFrom(clazz)) {
			JSONObject o = (JSONObject) value;
			JSONConvertible t = createObject0(o, clazz.asSubclass(JSONConvertible.class));
			t.preDeserialize(o);
			for(Field f : clazz.getDeclaredFields()) {
				JSONValue v = f.getAnnotation(JSONValue.class);
				JSONListType lT = f.getAnnotation(JSONListType.class);
				JSONComplexListType clT = f.getAnnotation(JSONComplexListType.class);
				if(v == null || !v.decode()) continue;
				String fName = v.value().isEmpty() ? f.getName() : v.value();
				if(o.has(fName)) {
					f.setAccessible(true);
					try {
						if(lT != null || clT != null) {
							f.set(t, decodeList0(o.getJSONArray(fName), lT, clT));
						}else {
							f.set(t, decode0(o.get(fName), f.getType()));
						}
					} catch (IllegalAccessException ignored) {}
				}
			}
			return t;
		}else {
			NullableOptional<Object> v = MiscUtils.callSafely(() -> JSONType.castJSONValueTo(value, clazz, false));
			if(!v.isPresent()) throw new IllegalArgumentException("Invalid JSON value type: " + clazz);
			return v.get();
		}
	}
	
	private static List<Object> decodeList0(JSONArray value, JSONListType type, JSONComplexListType complexType) {
		if(value == null) return null;
		List<Object> list = new ArrayList<>();
		for(Object o : value) {
			if(o == null) list.add(null);
			if(type != null) {
				NullableOptional<Object> v = MiscUtils.callSafely(() -> type.value().cast(o));
				if(!v.isPresent()) throw new IllegalArgumentException("Invalid JSON value type, cannot cast " + o.getClass().getName() + " to " + type.value());
				list.add(v.get());
			}else {
				Object obj = decode0(o, complexType.value());
				list.add(obj);
			}
		}
		return list;
	}
	
}
