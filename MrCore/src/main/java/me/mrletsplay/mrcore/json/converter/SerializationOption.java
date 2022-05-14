package me.mrletsplay.mrcore.json.converter;

public enum SerializationOption {
	
	/**
	 * Serialize all enum values ({@link JSONEnum}s) as strings
	 */
	SHORT_ENUMS,
	
	/**
	 * Serialize enum values including all of their fields which would otherwise be ignored
	 */
	EXTENDED_ENUMS,
	
	/**
	 * Don't include the fully qualified class name in the {@link JSONConverter#CLASS_NAME_FIELD class name field}
	 */
	DONT_INCLUDE_CLASS,
	;

}
