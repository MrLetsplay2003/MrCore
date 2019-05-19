package me.mrletsplay.mrcore.json.converter;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
		return encodeObject(convertible, true);
	}
	
	public static JSONObject encodeObject(JSONConvertible convertible, boolean includeClass) {
		return (JSONObject) encode0(convertible, includeClass);
	}
	
	private static Object encode0(Object value, boolean includeClass) {
		if(value == null) return null;
		if(value instanceof List<?>) {
			List<?> l = (List<?>) value;
			JSONArray arr = new JSONArray();
			for(Object o : l) {
				arr.add(encode0(o, includeClass));
			}
			return arr;
		}else if(value instanceof JSONConvertible) {
			JSONObject obj = new JSONObject();
			((JSONConvertible) value).preSerialize(obj);
			for(Field f : getFields(value.getClass())) {
				JSONValue v = f.getAnnotation(JSONValue.class);
				if(v == null || !v.encode()) continue;
				String fName = v.value().isEmpty() ? f.getName() : v.value();
				f.setAccessible(true);
				try {
					obj.set(fName, encode0(f.get(value), includeClass));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					throw new FriendlyException(e);
				}
			}
			for(Method m : getMethods(value.getClass())) {
				JSONValue v = m.getAnnotation(JSONValue.class);
				if(v == null || !v.encode()) continue;
				if(m.getParameterCount() != 0) throw new IllegalArgumentException("Method has parameters");
				String fName = v.value().isEmpty() ? m.getName() : v.value();
				m.setAccessible(true);
				try {
					obj.set(fName, encode0(m.invoke(value), includeClass));
				} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
					throw new FriendlyException(e);
				}
			}
			if(includeClass) obj.set("_class", value.getClass().getName());
			return obj;
		}else if(value instanceof JSONPrimitiveConvertible) {
			return ((JSONPrimitiveConvertible) value).toJSONPrimitive();
		}else if(value instanceof Object[]) {
			return encodeArray0((Object[]) value, includeClass);
		}else {
			JSONType t = JSONType.typeOf(value);
			if(t == null) throw new IllegalArgumentException("Object of type " + value.getClass() + " is not a valid JSON value");
			return value;
		}
	}
	
	private static Set<Field> getFields(Class<?> clz) {
		if(!JSONConvertible.class.isAssignableFrom(clz)) return Collections.emptySet();
		Set<Field> fs = new HashSet<>();
		Class<?> cls = clz;
		while(!cls.equals(Object.class)) {
			fs.addAll(Arrays.asList(cls.getDeclaredFields()));
			cls = cls.getSuperclass();
			if(cls == null) break;
		}
		return fs;
	}
	
	private static Set<Method> getMethods(Class<?> clz) {
		if(!JSONConvertible.class.isAssignableFrom(clz)) return Collections.emptySet();
		Set<Method> fs = new HashSet<>();
		Class<?> cls = clz;
		while(!cls.equals(Object.class)) {
			fs.addAll(Arrays.asList(cls.getDeclaredMethods()));
			Arrays.stream(cls.getInterfaces()).forEach(i -> fs.addAll(getMethods(i)));
			cls = cls.getSuperclass();
			if(cls == null) break;
		}
		return fs;
	}
	
	private static JSONArray encodeArray0(Object[] array, boolean includeClass) {
		JSONArray arr = new JSONArray();
		for(Object v : array) arr.add(encode0(v, includeClass));
		return arr;
	}
	
	public static <T extends JSONConvertible> T decodeObject(JSONObject object, Class<T> clazz, ClassLoader loader) {
		return clazz.cast(decode0(object, clazz, loader));
	}
	
	public static <T extends JSONConvertible> T decodeObject(JSONObject object, Class<T> clazz) {
		return decodeObject(object, clazz, JSONConverter.class.getClassLoader());
	}
	
	private static <T extends JSONConvertible> T createObject0(JSONObject object, Class<T> clazz, ClassLoader loader) {
		List<Constructor<?>> constrs = Arrays.stream(clazz.getDeclaredConstructors()).filter(c -> c.isAnnotationPresent(JSONConstructor.class)).collect(Collectors.toList());
		if(constrs.isEmpty()) throw new IllegalArgumentException("No constructor available for class " + clazz.getName());
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
				JSONPrimitiveListType prParam = p.getAnnotation(JSONPrimitiveListType.class);
				if(lParam != null || clParam != null || prParam != null) {
					params.add(decodeList0((JSONArray) pValue, loader, lParam, clParam, prParam));
				}else {
					params.add(decode0(pValue, p.getType(), loader));
				}
			}
			c.setAccessible(true);
			try {
				t = clazz.cast(c.newInstance(params.toArray(new Object[params.size()])));
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				continue;
			}
		}
		if(t == null) throw new IllegalArgumentException("No suitable/working constructor found for class " + clazz.getName());
		return t;
	}
	
	private static <T extends JSONPrimitiveConvertible> T createPrimitive0(Class<T> clazz) {
		List<Constructor<?>> constrs = Arrays.stream(clazz.getDeclaredConstructors()).filter(c -> c.isAnnotationPresent(JSONConstructor.class)).collect(Collectors.toList());
		if(constrs.isEmpty()) throw new IllegalArgumentException("No constructor available for class " + clazz.getName());
		T t = null;
		for(Constructor<?> c : constrs) {
			if(c.getParameterCount() > 0) continue;
			c.setAccessible(true);
			try {
				t = clazz.cast(c.newInstance());
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				continue;
			}
		}
		if(t == null) throw new IllegalArgumentException("No suitable/working constructor found for class " + clazz.getName());
		return t;
	}
	
	private static Object decode0(Object value, Class<?> clazz, ClassLoader loader) {
		if(value == null) return null;
		if(JSONConvertible.class.isAssignableFrom(clazz)) {
			JSONObject o = (JSONObject) value;
			Class<? extends JSONConvertible> jClass = clazz.asSubclass(JSONConvertible.class);
			if(o.isOfType("_class", JSONType.STRING)) {
				try {
					Class<?> nClass = Class.forName(o.getString("_class"), true, loader);
					if(JSONConvertible.class.isAssignableFrom(nClass)) {
						jClass = nClass.asSubclass(JSONConvertible.class);
					}
				} catch (ClassNotFoundException ignored) {}
			}
			JSONConvertible t = createObject0(o, jClass, loader);
			t.preDeserialize(o);
			for(Field f : getFields(jClass)) {
				JSONValue v = f.getAnnotation(JSONValue.class);
				JSONListType lT = f.getAnnotation(JSONListType.class);
				JSONComplexListType clT = f.getAnnotation(JSONComplexListType.class);
				JSONPrimitiveListType prT = f.getAnnotation(JSONPrimitiveListType.class);
				if(v == null || !v.decode()) continue;
				String fName = v.value().isEmpty() ? f.getName() : v.value();
				if(o.has(fName)) {
					f.setAccessible(true);
					try {
						if(lT != null || clT != null || prT != null) {
							f.set(t, decodeList0(o.getJSONArray(fName), loader, lT, clT, prT));
						}else {
							f.set(t, decode0(o.get(fName), f.getType(), loader));
						}
					} catch (IllegalAccessException ignored) {}
				}
			}
			return t;
		}else if(JSONPrimitiveConvertible.class.isAssignableFrom(clazz)) {
			JSONPrimitiveConvertible p = createPrimitive0(clazz.asSubclass(JSONPrimitiveConvertible.class));
			p.deserialize(value);
			return p;
		}else if(clazz.isArray()) {
			JSONArray arr = (JSONArray) value;
			Object a = Array.newInstance(clazz.getComponentType(), arr.size());
			for(int i = 0; i < arr.size(); i++) {
				Array.set(a, i, decode0(arr.get(i), clazz.getComponentType(), loader));
			}
			return a;
		}else {
			NullableOptional<Object> v = MiscUtils.callSafely(() -> JSONType.castJSONValueTo(value, clazz, false));
			if(!v.isPresent()) throw new IllegalArgumentException("Invalid JSON value type: " + clazz);
			return v.get();
		}
	}
	
	private static List<Object> decodeList0(JSONArray value, ClassLoader loader, JSONListType type, JSONComplexListType complexType, JSONPrimitiveListType primitiveType) {
		if(value == null) return null;
		List<Object> list = new ArrayList<>();
		for(Object o : value) {
			if(o == null) list.add(null);
			if(type != null) {
				NullableOptional<Object> v = MiscUtils.callSafely(() -> type.value().cast(o));
				if(!v.isPresent()) throw new IllegalArgumentException("Invalid JSON value type, cannot cast " + o.getClass().getName() + " to " + type.value());
				list.add(v.get());
			}else if(complexType != null){
				Object obj = decode0(o, complexType.value(), loader);
				list.add(obj);
			}else {
				Object obj = decode0(o, primitiveType.value(), loader);
				list.add(obj);
			}
		}
		return list;
	}
	
}
