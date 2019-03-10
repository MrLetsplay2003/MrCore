package me.mrletsplay.mrcore.json.converter;

import me.mrletsplay.mrcore.json.JSONObject;

public interface JSONConvertible {

	public default JSONObject toJSON() {
		return JSONConverter.encodeObject(this);
	}
	
	public default void preSerialize(JSONObject object) {};
	
	public default void preDeserialize(JSONObject object) {};
	
}
