package me.mrletsplay.mrcore.config.impl;

import java.util.Map;

import me.mrletsplay.mrcore.config.v2.ConfigProperty;
import me.mrletsplay.mrcore.config.v2.ConfigSection;
import me.mrletsplay.mrcore.config.v2.ConfigValueType;
import me.mrletsplay.mrcore.misc.Complex;

public class DefaultConfigPropertyImpl implements ConfigProperty {

	private ConfigSection section;
	private String name;
	private ConfigValueType valueType;
	private Object value;
	
	public DefaultConfigPropertyImpl(ConfigSection section, String name, ConfigValueType valueType, Object value) {
		this.section = section;
		this.name = name;
		this.valueType = valueType;
		this.value = value;
	}

	@Override
	public ConfigSection getSection() {
		return section;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public ConfigValueType getValueType() {
		return valueType;
	}

	@Override
	public Object getValue() {
		return value;
	}
	
	public static Object create(ConfigSection section, String name, Object value) {
		if(value instanceof Map) {
			ConfigSection s = new DefaultConfigSectionImpl(section.getConfig(), section, name);
			s.loadFromMap(Complex.map(String.class, Object.class).cast(value).orElseThrow(() -> new IllegalArgumentException("Unsupported type")));
			return s;
		}
		ConfigValueType type = ConfigValueType.getTypeOf(value);
		if(type == null) throw new IllegalArgumentException("Unsupported type");
		return new DefaultConfigPropertyImpl(section, name, type, value);
	}

}
