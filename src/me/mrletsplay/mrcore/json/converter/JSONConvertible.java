package me.mrletsplay.mrcore.json.converter;

import me.mrletsplay.mrcore.json.JSONObject;

public interface JSONConvertible {

	public default JSONObject toJSON() {
		return toJSON(true);
	}

	public default JSONObject toJSON(boolean includeClass) {
		return JSONConverter.encodeObject(this, includeClass);
	}
	
	public default void preSerialize(JSONObject object) {};
	
	public default void preDeserialize(JSONObject object) {};
	
}
