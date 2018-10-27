package me.mrletsplay.mrcore.config.v2;

import java.util.Map;

import me.mrletsplay.mrcore.config.impl.DefaultConfigSectionImpl;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.misc.Complex;

public class DefaultConfigMappers {

	public static final ObjectMapper<JSONObject, ConfigSection> JSON_MAPPER = ObjectMapper.create(JSONObject.class, ConfigSection.class,
			(c, j) -> {
				ConfigSection s = new DefaultConfigSectionImpl(c.getConfig());
				s.loadFromJSON(j);
				return s;
			}, (c, s) -> {
				return s.toJSON();
			});
	
	public static final ObjectMapper<Map<String, Object>, JSONObject> MAP_MAPPER = ObjectMapper.create(Complex.map(String.class, Object.class), Complex.value(JSONObject.class),
			(c, j) -> {
				return new JSONObject(j); // TODO
			}, (c, s) -> {
				return s;
			});
	
	public static final ObjectMapper<ConfigSection, Map<String, Object>> SECTION_MAPPER = ObjectMapper.create(Complex.value(ConfigSection.class), Complex.map(String.class, Object.class),
			(c, j) -> {
				return j.toMap();
			}, (c, s) -> {
				ConfigSection s1 = new DefaultConfigSectionImpl(c.getConfig());
				s1.loadFromMap(s);
				return s1;
			});
	
}
