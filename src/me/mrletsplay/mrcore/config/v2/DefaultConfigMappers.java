package me.mrletsplay.mrcore.config.v2;

import me.mrletsplay.mrcore.config.impl.DefaultConfigSectionImpl;
import me.mrletsplay.mrcore.json.JSONObject;

public class DefaultConfigMappers {

	public static final ObjectMapper<JSONObject, ConfigSection> JSON_MAPPER = ObjectMapper.create(JSONObject.class, ConfigSection.class,
			(c, j) -> {
				ConfigSection s = new DefaultConfigSectionImpl(c.getConfig());
				s.loadFromJSON(j);
				return s;
			}, (c, s) -> {
				return s.toJSON();
			});
	
}
