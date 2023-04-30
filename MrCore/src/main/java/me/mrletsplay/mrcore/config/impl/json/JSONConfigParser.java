package me.mrletsplay.mrcore.config.impl.json;

import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.mrcore.config.ConfigSection;
import me.mrletsplay.mrcore.json.JSONArray;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.json.JSONType;

public class JSONConfigParser {

	public static ConfigSection parseSection(JSONCustomConfig cfg, JSONObject object) {
		ConfigSection s = new JSONConfigSection(cfg);

		for(String key : object.keys()) {
			Object o = object.get(key);
			JSONType type = JSONType.typeOf(o);
			switch(type) {
				case ARRAY:
					s.set(key, parseList(cfg, (JSONArray) o));
					break;
				case OBJECT:
					s.set(key, parseSection(cfg, (JSONObject) o));
					break;
				case BOOLEAN:
				case DECIMAL:
				case INTEGER:
				case NULL:
				case NUMBER:
				case STRING:
				default:
					s.set(key, o);
					break;
			}
		}

		s.loadFromJSON(object);

		return s;
	}

	public static List<Object> parseList(JSONCustomConfig cfg, JSONArray array) {
		List<Object> list = new ArrayList<>();

		for(int i = 0; i < array.size(); i++) {
			Object o = array.get(i);
			JSONType type = JSONType.typeOf(o);
			switch(type) {
				case ARRAY:
					list.add(parseList(cfg, (JSONArray) o));
					break;
				case OBJECT:
					list.add(parseSection(cfg, (JSONObject) o));
					break;
				case BOOLEAN:
				case DECIMAL:
				case INTEGER:
				case NULL:
				case NUMBER:
				case STRING:
				default:
					list.add(o);
					break;
			}
		}

		return list;
	}

}
