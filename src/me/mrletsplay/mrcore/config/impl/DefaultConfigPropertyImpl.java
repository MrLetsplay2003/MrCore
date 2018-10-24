package me.mrletsplay.mrcore.config.impl;

import java.util.List;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.config.impl.DefaultConfigParser.ConfigSectionDescriptor;
import me.mrletsplay.mrcore.config.v2.ConfigException;
import me.mrletsplay.mrcore.config.v2.ConfigProperty;
import me.mrletsplay.mrcore.config.v2.ConfigSection;
import me.mrletsplay.mrcore.config.v2.ConfigValueType;
import me.mrletsplay.mrcore.misc.NullableOptional;

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
	
	public static DefaultConfigPropertyImpl create(ConfigSection section, String name, Object value) {
		if(value instanceof ConfigSectionDescriptor) {
			ConfigSectionDescriptor d = (ConfigSectionDescriptor) value;
			ConfigSection s = new DefaultConfigSectionImpl(section.getConfig());
			s.loadFromMap(d.toPropertyMap());
			s.loadCommentsFromMap(d.toCommentMap());
			return new DefaultConfigPropertyImpl(section, name, ConfigValueType.SECTION, s);
		}
		if(value instanceof List<?>) {
			List<?> l = ((List<?>) value).stream().map(o -> create(section, null, o).getValue()).collect(Collectors.toList());
			return new DefaultConfigPropertyImpl(section, name, ConfigValueType.LIST, l);
		}
		NullableOptional<?> v = ConfigValueType.createCompatible(section, value);
		if(!v.isPresent()) throw new ConfigException("Unsupported type: " + value.getClass().getName());
		value = v.get();
		ConfigValueType type = ConfigValueType.getRawTypeOf(value);
		return new DefaultConfigPropertyImpl(section, name, type, value);
	}
	
	@Override
	public String toString() {
		return "[P: " + value + "]";
	}

}
