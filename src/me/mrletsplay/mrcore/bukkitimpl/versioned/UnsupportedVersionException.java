package me.mrletsplay.mrcore.bukkitimpl.versioned;

import me.mrletsplay.mrcore.misc.FriendlyException;

public class UnsupportedVersionException extends FriendlyException {

	private static final long serialVersionUID = -6779430170990863868L;

	public UnsupportedVersionException(String reason, Throwable cause) {
		super(reason, cause);
	}

	public UnsupportedVersionException(String reason) {
		super(reason);
	}

	public UnsupportedVersionException(Throwable cause) {
		super(cause);
	}

}
