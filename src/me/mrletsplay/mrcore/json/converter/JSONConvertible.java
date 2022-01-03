package me.mrletsplay.mrcore.json.converter;

import java.util.Arrays;
import java.util.EnumSet;

import me.mrletsplay.mrcore.json.JSONObject;

public interface JSONConvertible {

	public default JSONObject toJSON() {
		return toJSON(EnumSet.noneOf(SerializationOption.class));
	}

	@Deprecated
	public default JSONObject toJSON(boolean includeClass) {
		return JSONConverter.encodeObject(this, includeClass);
	}

	public default JSONObject toJSON(EnumSet<SerializationOption> options) {
		return JSONConverter.encodeObject(this, options);
	}

	public default JSONObject toJSON(SerializationOption... options) {
		return toJSON(EnumSet.copyOf(Arrays.asList(options)));
	}
	
	public default void preSerialize(JSONObject object) {};
	
	public default void preDeserialize(JSONObject object) {};
	
}
