package me.mrletsplay.mrcore.config.mapper;

import me.mrletsplay.mrcore.config.ConfigException;

public class ObjectMappingException extends ConfigException {

	private static final long serialVersionUID = 5211832083030427610L;
	
	public ObjectMappingException() {
		super();
	}
	
	public ObjectMappingException(String message) {
		super(message);
	}
	
	public ObjectMappingException(Throwable cause) {
		super(cause);
	}
	
	public ObjectMappingException(String message, Throwable cause) {
		super(message, cause);
	}

}
