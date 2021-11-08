package me.mrletsplay.mrcore.json.converter;

public enum DeserializationOption {
	
	/**
	 * When a class is explicitly specified in the {@link JSONConverter#CLASS_NAME_FIELD class name field} but it doesn't exist, don't throw an exception
	 */
	IGNORE_MISSING_CLASSES,
	
	/**
	 * Assume all enum values ({@link JSONEnum}s) are stored as strings (e.g. because of the {@link SerializationOption#SHORT_ENUMS SHORT_ENUMS} option)
	 */
	SHORT_ENUMS,
	
	/**
	 * Ignore all explicitly defined classes in the {@link JSONConverter#CLASS_NAME_FIELD class name field}
	 */
	IGNORE_CLASSES,
	;

}
