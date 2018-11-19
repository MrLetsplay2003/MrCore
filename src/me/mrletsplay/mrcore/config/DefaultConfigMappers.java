package me.mrletsplay.mrcore.config;

import java.util.Map;

import me.mrletsplay.mrcore.config.impl.DefaultConfigSectionImpl;
import me.mrletsplay.mrcore.config.mapper.ObjectMapper;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.misc.Complex;

public class DefaultConfigMappers {

	public static final ObjectMapper<JSONObject, ConfigSection> JSON_OBJECT_MAPPER = ObjectMapper.create(JSONObject.class, ConfigSection.class,
			(c, j) -> {
				ConfigSection s = new DefaultConfigSectionImpl(c.getConfig());
				s.loadFromJSON(j);
				return s;
			}, (c, s) -> {
				return s.toJSON();
			});

	public static final ObjectMapper<JSONObject, ConfigSection> JSON_ARRAY_MAPPER = ObjectMapper.create(JSONObject.class, ConfigSection.class,
			(c, j) -> {
				ConfigSection s = new DefaultConfigSectionImpl(c.getConfig());
				s.loadFromJSON(j);
				return s;
			}, (c, s) -> {
				return s.toJSON();
			});
	
	public static final ObjectMapper<Map<String, Object>, ConfigSection> MAP_MAPPER = ObjectMapper.create(Complex.map(String.class, Object.class), Complex.value(ConfigSection.class),
			(c, m) -> {
				ConfigSection s = c.getConfig().createEmptySection();
				s.loadFromMap(m);
				return s;
			}, (c, s) -> {
				return s.toMap();
			});
	
}
