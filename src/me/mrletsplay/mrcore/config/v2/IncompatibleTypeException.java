package me.mrletsplay.mrcore.config.v2;

public class IncompatibleTypeException extends ConfigException {

	private static final long serialVersionUID = 5211832083030427610L;
	
	public IncompatibleTypeException() {
		super();
	}
	
	public IncompatibleTypeException(String message) {
		super(message);
	}
	
	public IncompatibleTypeException(Throwable cause) {
		super(cause);
	}
	
	public IncompatibleTypeException(String message, Throwable cause) {
		super(message, cause);
	}

}
