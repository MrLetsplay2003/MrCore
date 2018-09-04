package me.mrletsplay.mrcore.misc;

public class FriendlyException extends RuntimeException {

	private static final long serialVersionUID = -4265858806922130917L;

	public FriendlyException(Throwable cause) {
		super(cause);
	}
	
	public FriendlyException(String reason) {
		super(reason);
	}
	
	public FriendlyException(String reason, Throwable cause) {
		super(reason, cause);
	}
	
}
