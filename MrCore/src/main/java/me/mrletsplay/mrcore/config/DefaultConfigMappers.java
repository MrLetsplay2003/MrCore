package me.mrletsplay.mrcore.config;

import java.util.List;
import java.util.Map;

import me.mrletsplay.mrcore.config.impl.yaml.YAMLConfigSection;
import me.mrletsplay.mrcore.config.mapper.ObjectMapper;
import me.mrletsplay.mrcore.json.JSONArray;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.misc.Complex;

public class DefaultConfigMappers {

	public static final ObjectMapper<JSONObject, ConfigSection> JSON_OBJECT_MAPPER = ObjectMapper.create(JSONObject.class, ConfigSection.class,
			(c, j) -> {
				ConfigSection s = new YAMLConfigSection(c.getConfig());
				s.loadFromJSON(j);
				return s;
			}, (c, s) -> {
				return s.toJSON();
			});

	public static final ObjectMapper<JSONArray, List<Object>> JSON_ARRAY_MAPPER = ObjectMapper.create(Complex.value(JSONArray.class), Complex.list(Object.class),
			(c, j) -> {
				return j.toList();
			}, (c, l) -> {
				return (JSONArray) ConfigProperty.toJSONCompliant(l);
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
