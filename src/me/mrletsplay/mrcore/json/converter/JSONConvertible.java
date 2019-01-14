package me.mrletsplay.mrcore.json.converter;

import me.mrletsplay.mrcore.json.JSONObject;

public interface JSONConvertible {

	public default JSONObject toJSON() {
		return JSONConverter.encodeObject(this);
	}
	
}
