package me.mrletsplay.mrcore.config.v2;

import java.util.List;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.json.JSONArray;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.misc.Complex;

public interface ConfigProperty {
	
	public ConfigSection getSection();

	public String getName();
	
	public ConfigValueType getValueType();
	
	public Object getValue();
	
	public default boolean isNull() {
		return getValueType().equals(ConfigValueType.NULL);
	}
	
	public default boolean isUndefined() {
		return getValueType().equals(ConfigValueType.UNDEFINED);
	}
	
	public default boolean isSubsection() {
		return getValueType().equals(ConfigValueType.SECTION);
	}
	
	public default <T> T getValue(Complex<T> asType) {
		if(isUndefined()) throw new ConfigException("Value is not defined");
		if(isNull()) return null;
		if(!getValueType().getValueClass().equals(asType)) throw new IncompatibleTypeException("Invalid class provided, must be " + getValueType().getValueClass());
		return asType.cast(getValue()).get();
	}
	
	public default Object getJSONValue() {
		if(isUndefined()) throw new ConfigException("Value is not defined");
		if(isNull()) return null;
		return toJSONCompliant(getValue());
	}
	
	public static Object toJSONCompliant(Object o) {
		if(o == null) return null;
		if(o instanceof ConfigSection) {
			return new JSONObject(((ConfigSection)o).toMap().entrySet().stream()
					.collect(Collectors.toMap(en -> en.getKey(), en -> toJSONCompliant(en.getValue()))));
		}else if(o instanceof List) {
			return new JSONArray(((List<?>)o).stream()
					.map(en -> toJSONCompliant(en))
					.collect(Collectors.toList()));
		}else if(o instanceof Character) {
			return o.toString();
		}
		return o;
	}
	
}
