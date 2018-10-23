package me.mrletsplay.mrcore.config.v2;

import me.mrletsplay.mrcore.config.impl.ConfigVersion;

public class IncompatibleConfigVersionException extends ConfigException {

	private static final long serialVersionUID = 3106349501269549012L;

	private String givenVersion, requiredVersion;
	
	public IncompatibleConfigVersionException(String givenVersion, String requiredVersion) {
		super("Incompatible config versions. Got \"" + givenVersion + "\", need \"" + requiredVersion + "\"");
		this.givenVersion = givenVersion;
		this.requiredVersion = requiredVersion;
	}
	
	public String getGivenVersion() {
		return givenVersion;
	}
	
	public ConfigVersion getGivenDefaultVersion() {
		return ConfigVersion.getByName(givenVersion);
	}
	
	public String getRequiredVersion() {
		return requiredVersion;
	}
	
	public ConfigVersion getRequiredDefaultVersion() {
		return ConfigVersion.getByName(requiredVersion);
	}
	
}
