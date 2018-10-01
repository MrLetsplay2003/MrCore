package me.mrletsplay.mrcore.config.v2;

public interface ConfigProperty {

	public String getKey();
	
	public ConfigValueType getValueType();
	
	public Object getValue();
	
	public default boolean isNull() {
		return getValueType().equals(ConfigValueType.NULL);
	}
	
	public default boolean isUndefined() {
		return getValueType().equals(ConfigValueType.UNDEFINED);
	}
	
	public default <T> T getValue(Class<T> asType) {
		if(isUndefined()) throw new ConfigException("Value is undefined");
		if(isNull()) return null;
		if(!getValueType().getValueClass().equals(asType)) throw new IncompatibleTypeException("Invalid class provided, must be " + getValueType().getValueClass());
		return asType.cast(getValue());
	}
	
}
