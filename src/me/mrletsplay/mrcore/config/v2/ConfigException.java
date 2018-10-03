package me.mrletsplay.mrcore.config.v2;

public class ConfigException extends RuntimeException {

	private static final long serialVersionUID = 891151520473594789L;
	
	public ConfigException() {
		super();
	}
	
	public ConfigException(String message) {
		super(message);
	}
	
	public ConfigException(String message, int rawLine, int rawIndex) {
		super(message + " in line " + (rawLine + 1) + " near index " + (rawIndex + 1));
	}
	
	public ConfigException(String message, Throwable cause, int rawLine, int rawIndex) {
		super(message + " in line " + (rawLine + 1) + " near index " + (rawIndex + 1), cause);
	}
	
	public ConfigException(Throwable cause) {
		super(cause);
	}
	
	public ConfigException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
