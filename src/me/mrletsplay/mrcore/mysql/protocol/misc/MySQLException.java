package me.mrletsplay.mrcore.mysql.protocol.misc;

public class MySQLException extends RuntimeException {

	private static final long serialVersionUID = 4086430481013127797L;
	
	public MySQLException() {
		super();
	}
	
	public MySQLException(String message) {
		super(message);
	}
	
	public MySQLException(Throwable cause) {
		super(cause);
	}
	
	public MySQLException(String message, Throwable cause) {
		super(message, cause);
	}

}
